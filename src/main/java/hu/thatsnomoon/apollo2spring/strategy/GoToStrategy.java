package hu.thatsnomoon.apollo2spring.strategy;

import com.sun.xml.internal.ws.model.wsdl.WSDLDirectProperties;
import eu.loxon.centralcontrol.ActionCostResponse;
import eu.loxon.centralcontrol.ObjectType;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;
import eu.loxon.centralcontrol.WsScore;
import hu.thatsnomoon.apollo2spring.configuration.ApolloConfiguration;
import hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException;
import hu.thatsnomoon.apollo2spring.model.BuilderUnit;
import hu.thatsnomoon.apollo2spring.model.Coordinate;
import hu.thatsnomoon.apollo2spring.service.ApolloClientService;
import hu.thatsnomoon.apollo2spring.utils.WsCoordinateUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This strategy contains the logic to shepherd the builder unit to a given
 * coordinate.
 *
 * @author NB57
 */
public class GoToStrategy implements Strategy {

    private final ApolloClientService apolloClient;

    private final WsCoordinate DESTINATION;

    private Set<Coordinate> blockedCells;

    public GoToStrategy(ApolloClientService apolloClient, WsCoordinate DESTINATION) {
        this.apolloClient = apolloClient;
        this.DESTINATION = DESTINATION;
    }

    /**
     * Steps in a round with the given builderUnit
     *
     * NOTE : This implementation trusts the builder unit's position provided by
     * the builder unit itself.
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
                    continue;
                } else {

                    List<Scouting> neighbourCellList = this.apolloClient.watch(builderUnit.getId()).getScout();
                    WsDirection moveDirection = primaryMoveDirection(builderUnit);
                    Scouting moveTarget;
                    moveTarget = WsCoordinateUtils.getDirectionScoutingFromWatch(
                            neighbourCellList,
                            builderUnit.getPosition(),
                            moveDirection);

                    if (shouldAvoid(moveTarget)) {
                        moveTarget = WsCoordinateUtils.getDirectionScoutingFromWatch(
                                neighbourCellList,
                                builderUnit.getPosition(),
                                secondaryMoveDirection(builderUnit));
                        moveDirection = secondaryMoveDirection(builderUnit);
                    }

                    if (shouldAvoid(moveTarget)) {
                        moveTarget = WsCoordinateUtils.getDirectionScoutingFromWatch(
                                neighbourCellList,
                                builderUnit.getPosition(),
                                tertiaryMoveDirection(builderUnit));
                        moveDirection = tertiaryMoveDirection(builderUnit);

                        blockedCells.add(new Coordinate(builderUnit.getPosition()));

                    }
                    // Backstep logic
                    if (shouldAvoid(moveTarget)) {
                        break;
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
                    if ((moveTarget.getObject().equals(ObjectType.GRANITE) || (moveTarget.getObject().equals(ObjectType.TUNNEL) && !moveTarget.getTeam().equals(ApolloConfiguration.user)))
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

                }

            }
        } catch (SoapResponseInvalidException e) {
            System.out.println("----------------- SoapResponseInvalidException has been thrown:");
            WsCoordinateUtils.print(e.getResponse());
            e.printStackTrace();
        }
    }

    public WsDirection primaryMoveDirection(BuilderUnit builderUnit) {
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

    public WsDirection secondaryMoveDirection(BuilderUnit builderUnit) {
        final WsCoordinate CURRENT = builderUnit.getPosition();

        if (DESTINATION.getY() < CURRENT.getY()) {
            return WsDirection.DOWN;
        }

        if (DESTINATION.getY() > CURRENT.getY()) {
            return WsDirection.UP;
        }

        if (DESTINATION.getX() < CURRENT.getX()) {
            return WsDirection.LEFT;
        }

        if (DESTINATION.getX() > CURRENT.getX()) {
            return WsDirection.RIGHT;
        }

        // Otherwise the positions are equal, and that should be handled by the Strategy.
        throw new RuntimeException("The Destination and the builder units position is the same.");
    }

    public WsDirection tertiaryMoveDirection(BuilderUnit builderUnit) {
        return WsCoordinateUtils.oppositeDirection(this.secondaryMoveDirection(builderUnit));
    }

    // If we should avoid the cell it returns true.
    public boolean shouldAvoid(Scouting scouting) {
        if (scouting.getObject() == ObjectType.SHUTTLE
                || scouting.getObject() == ObjectType.BUILDER_UNIT
                || scouting.getObject() == ObjectType.OBSIDIAN) {
            return true;
        }

        if (blockedCells.contains(new Coordinate(scouting.getCord()))) {
            return true;
        }

        return false;
    }
}
