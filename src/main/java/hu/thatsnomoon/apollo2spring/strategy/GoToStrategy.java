package hu.thatsnomoon.apollo2spring.strategy;

import eu.loxon.centralcontrol.ActionCostResponse;
import eu.loxon.centralcontrol.ObjectType;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;
import hu.thatsnomoon.apollo2spring.Application;
import hu.thatsnomoon.apollo2spring.configuration.ApolloConfiguration;
import hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException;
import hu.thatsnomoon.apollo2spring.model.BuilderUnit;
import hu.thatsnomoon.apollo2spring.model.Coordinate;
import hu.thatsnomoon.apollo2spring.service.ApolloClientService;
import hu.thatsnomoon.apollo2spring.utils.WsCoordinateUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This strategy contains the logic to shepherd the builder unit to a given coordinate.
 *
 * @author NB57
 */
public class GoToStrategy implements Strategy {

    private final ApolloClientService apolloClient;

    private final WsCoordinate DESTINATION;

    private Set<Coordinate> blockedCells;

    private boolean ready = false;

    public GoToStrategy(ApolloClientService apolloClient, WsCoordinate DESTINATION) {
        this.apolloClient = apolloClient;
        this.DESTINATION = DESTINATION;
    }

    /**
     * Steps in a round with the given builderUnit
     *
     * NOTE : This implementation trusts the builder unit's position provided by the builder unit itself.
     *
     * @param builderUnit
     */
    @Override
    public void step(BuilderUnit builderUnit) {
        System.out.println("--------------------------------------");
        System.out.println("Turn of robot " + builderUnit.getId());
        System.out.println("Pos{}ition of the robot: " + builderUnit.getPosition().getX() + ", " + builderUnit.getPosition().getY());
        try {

            ActionCostResponse actionCostResponse = this.apolloClient.getActionCost();
            WsCoordinateUtils.print(actionCostResponse.getResult());
            int remainingActionPoints = actionCostResponse.getAvailableActionPoints();
            int remainingExplosives = actionCostResponse.getAvailableExplosives();

            // Reinitializing blocked list
            blockedCells = new HashSet<>();

            long startTime = System.currentTimeMillis();

            while (System.currentTimeMillis() - startTime < Strategy.ROUND_TIME) {
                // If we are at the destination, there is no need for further action.
                if (new Coordinate(builderUnit.getPosition()).equals(new Coordinate(DESTINATION))) {
                    this.ready = true;
                    break;
                } else {

                    List<Scouting> neighbourCellList = this.apolloClient.watch(builderUnit.getId()).getScout();

                    List<WsDirection> sortedDirections = sortMoveDirections(builderUnit);

                    Scouting moveTarget = null;
                    WsDirection moveDirection = null;

                    for (WsDirection tempDirection : sortedDirections) {
                        Scouting tempMoveTarget = WsCoordinateUtils.getDirectionScoutingFromWatch(
                                neighbourCellList,
                                builderUnit.getPosition(),
                                tempDirection);
                        if (!shouldAvoid(moveTarget, remainingExplosives)) {
                            if (sortedDirections.indexOf(tempDirection) > 2) {
                                blockedCells.add(new Coordinate(builderUnit.getPosition()));
                            }
                            moveTarget = tempMoveTarget;
                            moveDirection = tempDirection;
                            break;
                        }
                    }

                    // If we are blocked from every neighbour, then wait for next turn
                    if (moveTarget == null) {
                        break;
                    }

                    // If ours, than move into that cell
                    if (moveTarget.getObject().equals(ObjectType.TUNNEL)
                            && moveTarget.getTeam().equals(ApolloConfiguration.user)
                            && remainingActionPoints >= actionCostResponse.getMove()) {
                        this.apolloClient.moveBuilderUnit(builderUnit.getId(), moveDirection);
                        remainingActionPoints -= actionCostResponse.getMove();
                        System.out.println("Robot moved.");
                        System.out.println("New position of the robot: " + builderUnit.getPosition().getX() + ", " + builderUnit.getPosition().getY());

                        continue;
                    }

                    // If ROCK, structure tunnel
                    if (moveTarget.getObject().equals(ObjectType.ROCK) && remainingActionPoints >= actionCostResponse.getDrill()) {
                        // Structure cell
                        this.apolloClient.structureTunnel(builderUnit.getId(), moveDirection);
                        remainingActionPoints -= actionCostResponse.getDrill();
                        System.out.println("Robot drilled.");
                        System.out.println("Position of the drilling: " + moveTarget.getCord().getX() + ", " + moveTarget.getCord().getY());
                        continue;
                    }

                    // If GRANITE or ENEMY TUNNEL blow up
                    if ((moveTarget.getObject().equals(ObjectType.GRANITE)
                            || (moveTarget.getObject().equals(ObjectType.TUNNEL)
                            && !moveTarget.getTeam().equals(ApolloConfiguration.user)))
                            && remainingActionPoints >= actionCostResponse.getExplode()
                            && remainingExplosives > 0) {
                        // Explode cell
                        this.apolloClient.explodeCell(builderUnit.getId(), moveDirection);
                        remainingActionPoints -= actionCostResponse.getExplode();
                        remainingExplosives--;
                        System.out.println("Robot exploded.");
                        System.out.println("Position of the explosion: " + moveTarget.getCord().getX() + ", " + moveTarget.getCord().getY());
                        continue;
                    }

                    // If we can't move, just end the turn
                    break;
                }

            }
        } catch (SoapResponseInvalidException e) {
            System.out.println("----------------- SoapResponseInvalidException has been thrown:");
            WsCoordinateUtils.print(e.getResponse());
            e.printStackTrace();
        }
    }

    private WsDirection primaryMoveDirection(BuilderUnit builderUnit) {
        final WsCoordinate CURRENT = builderUnit.getPosition();

        if (DESTINATION.getX() < CURRENT.getX()) {
            return WsDirection.LEFT;
        }

        if (DESTINATION.getX() > CURRENT.getX()) {
            return WsDirection.RIGHT;
        }

        if (DESTINATION.getY() < CURRENT.getY()) {
            return WsDirection.DOWN;
        }

        if (DESTINATION.getY() > CURRENT.getY()) {
            return WsDirection.UP;
        }

        // Otherwise the positions are equal, and that should be handled by the Strategy.
        throw new RuntimeException("The Destination and the builder units position is the same.");

    }

    private WsDirection secondaryMoveDirection(BuilderUnit builderUnit) {
        final WsCoordinate CURRENT = builderUnit.getPosition();

        if (DESTINATION.getX() == CURRENT.getX()) {
            return WsDirection.RIGHT;
        }

        if (DESTINATION.getY() < CURRENT.getY()) {
            return WsDirection.DOWN;
        }

        if (DESTINATION.getY() > CURRENT.getY()) {
            return WsDirection.UP;
        }

        if (DESTINATION.getX() != CURRENT.getX()) {
            return WsDirection.UP;
        }

        // Otherwise the positions are equal, and that should be handled by the Strategy.
        throw new RuntimeException("The Destination and the builder units position is the same.");
    }

    private WsDirection tertiaryMoveDirection(BuilderUnit builderUnit) {
        return WsCoordinateUtils.oppositeDirection(this.secondaryMoveDirection(builderUnit));
    }

    private WsDirection quaternaryMoveDirection(BuilderUnit builderUnit) {
        return WsCoordinateUtils.oppositeDirection(this.primaryMoveDirection(builderUnit));
    }

    public List<WsDirection> sortMoveDirections(BuilderUnit builderUnit) {
        List<WsDirection> sortedDirections = new ArrayList<>();

        sortedDirections.add(primaryMoveDirection(builderUnit));
        sortedDirections.add(secondaryMoveDirection(builderUnit));
        sortedDirections.add(tertiaryMoveDirection(builderUnit));
        sortedDirections.add(quaternaryMoveDirection(builderUnit));

        return sortedDirections;
    }

    // If we should avoid the cell it returns true.
    public boolean shouldAvoid(Scouting scouting, int remainingExplosives) {
        if (scouting.getObject() == ObjectType.SHUTTLE
                || scouting.getObject() == ObjectType.BUILDER_UNIT
                || scouting.getObject() == ObjectType.OBSIDIAN) {
            return true;
        }

        if (scouting.getObject() == ObjectType.GRANITE && remainingExplosives < 1) {
            return true;
        }

        if (blockedCells.contains(new Coordinate(scouting.getCord()))) {
            return true;
        }

        return false;
    }
}
