package hu.thatsnomoon.apollo2spring.strategy;

import eu.loxon.centralcontrol.ActionCostResponse;
import eu.loxon.centralcontrol.ObjectType;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.WsDirection;
import hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException;
import hu.thatsnomoon.apollo2spring.model.BuilderUnit;
import hu.thatsnomoon.apollo2spring.model.Coordinate;
import hu.thatsnomoon.apollo2spring.service.ApolloClientService;
import hu.thatsnomoon.apollo2spring.utils.WsCoordinateUtils;
import java.util.List;

/**
 *
 * @author David
 */
public class ShuttleStrategy implements Strategy {

    private final ApolloClientService apolloClient;

    private boolean ready = false;

    public ShuttleStrategy(ApolloClientService apolloClient) {
        this.apolloClient = apolloClient;
    }

    @Override
    public void step(BuilderUnit builderUnit) {
        System.out.println("--------------------------------------");
        System.out.println("Turn of robot " + builderUnit.getId());
        System.out.println("Position of the robot: " + builderUnit.getPosition().getX() + ", " + builderUnit.getPosition().getY());

        try {

            ActionCostResponse actionCostResponse = this.apolloClient.getActionCost();
            WsCoordinateUtils.print(actionCostResponse.getResult());
            int remainingActionPoints = actionCostResponse.getAvailableActionPoints();
            int remainingExplosives = actionCostResponse.getAvailableExplosives();

            long startTime = System.currentTimeMillis();

            while (System.currentTimeMillis() - startTime < Strategy.ROUND_TIME) {

                Coordinate shuttlePosition = new Coordinate(apolloClient.getSpaceShuttlePos().getCord()); // Flowchart - 1.
                if (shuttlePosition.equals(new Coordinate(builderUnit.getPosition()))) {
                    // Get type of exit
                    ObjectType exitCellType = null;
                    Coordinate exitPos = new Coordinate(this.apolloClient.getSpaceShuttleExitPos().getCord());
                    if (remainingActionPoints < actionCostResponse.getWatch()) {
                        break;
                    }
                    List<Scouting> scoutList = this.apolloClient.watch(builderUnit.getId()).getScout();
                    remainingActionPoints -= actionCostResponse.getWatch();

                    for (Scouting scout : scoutList) {
                        if (new Coordinate(scout.getCord()).equals(exitPos)) {
                            exitCellType = scout.getObject();
                        }
                    }

                    WsDirection exitDirection = WsCoordinateUtils.moveDirection(builderUnit.getPosition(), exitPos.getWsCoordinate());

                    // Flowchart - 2.
                    if (exitCellType == ObjectType.TUNNEL && remainingActionPoints >= actionCostResponse.getMove()) {

                        // Move into cell + refresh builderUnit coordinate
                        this.apolloClient.moveBuilderUnit(builderUnit.getId(), exitDirection);
                        remainingActionPoints -= actionCostResponse.getMove();
                        System.out.println("Robot moved.(Fc2)");
                        System.out.println("New position of the robot: " + builderUnit.getPosition().getX() + ", " + builderUnit.getPosition().getY());

                        this.ready = true;

                        break;
                    } else if (exitCellType == ObjectType.ROCK && remainingActionPoints >= actionCostResponse.getDrill()) {
                        // Structure cell
                        this.apolloClient.structureTunnel(builderUnit.getId(), exitDirection);
                        remainingActionPoints -= actionCostResponse.getDrill();
                        System.out.println("Robot drilled.(Fc2)");
                        System.out.println("Position of the drilling: " + exitPos.getWsCoordinate().getX() + ", " + exitPos.getWsCoordinate().getY());
                        break;
                    } else if (exitCellType == ObjectType.GRANITE && remainingActionPoints >= actionCostResponse.getExplode() && remainingExplosives > 0) {
                        // Explode cell
                        this.apolloClient.explodeCell(builderUnit.getId(), exitDirection);
                        remainingActionPoints -= actionCostResponse.getExplode();
                        remainingExplosives--;
                        System.out.println("Robot exploded.(Fc2)");
                        System.out.println("Position of the explose: " + exitPos.getWsCoordinate().getX() + ", " + exitPos.getWsCoordinate().getY());
                        continue;
                    } else {
                        break;
                    }
                } else {
                    // Not in the shuttle
                    this.ready = true;
                    break;
                }
            }
        } catch (SoapResponseInvalidException e) {
            System.out.println("----------------- SoapResponseInvalidException has been thrown:");
            WsCoordinateUtils.print(e.getResponse());
            e.printStackTrace();
        }
    }

    @Override
    public boolean isEnded() {
        return this.ready;
    }

}
