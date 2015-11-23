package hu.thatsnomoon.apollo2spring.strategy;

import error.SoapResponseInvalidException;
import eu.loxon.centralcontrol.ActionCostResponse;
import eu.loxon.centralcontrol.ObjectType;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.WsDirection;
import hu.thatsnomoon.apollo2spring.ApolloClient;
import hu.thatsnomoon.apollo2spring.ApolloConfiguration;
import hu.thatsnomoon.apollo2spring.BuilderUnit;
import hu.thatsnomoon.apollo2spring.Coordinate;
import hu.thatsnomoon.apollo2spring.Strategy;
import hu.thatsnomoon.apollo2spring.WsCoordinateUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author David
 */
public class DefaultStrategy implements Strategy {

    private final ApolloClient apolloClient;

    private final WsDirection defaultDirection;

    private final List<Coordinate> route;

    private final Set<Coordinate> ignoredCoordinates;

    private Coordinate lastStructuredCoordinate;

    public DefaultStrategy(ApolloClient apolloClient) {
        this.apolloClient = apolloClient;
        this.defaultDirection = WsDirection.DOWN;
        this.route = new ArrayList<>();
        this.ignoredCoordinates = new HashSet<>();
    }

    public DefaultStrategy(ApolloClient apolloClient, WsDirection defaultDirection) {
        this.apolloClient = apolloClient;
        this.defaultDirection = defaultDirection;
        this.route = new ArrayList<>();
        this.ignoredCoordinates = new HashSet<>();
    }

    /**
     * Steps in a round with the given builderUnit
     *
     * @param builderUnit
     */
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

            this.ignoredCoordinates.clear();
            this.lastStructuredCoordinate = null;

            while (System.currentTimeMillis() - startTime < ROUND_TIME) {

                Coordinate shuttlePosition = new Coordinate(apolloClient.getSpaceShuttlePos().getCord());

                // Flowchart - 1.
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
                        builderUnit.setPosition(exitPos.getWsCoordinate());
                        remainingActionPoints -= actionCostResponse.getMove();
                        System.out.println("Robot moved.(Fc2)");
                        System.out.println("New position of the robot: " + builderUnit.getPosition().getX() + ", " + builderUnit.getPosition().getY());
                        continue;
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
                }

                // Flowchart - 3.
                if (this.lastStructuredCoordinate != null && remainingActionPoints >= actionCostResponse.getMove()) {
                    Coordinate previousCell = new Coordinate(builderUnit.getPosition());
                    WsDirection moveDirection = WsCoordinateUtils.moveDirection(builderUnit.getPosition(), lastStructuredCoordinate.getWsCoordinate());

                    // Move into last lastStructuredCoordinate
                    this.apolloClient.moveBuilderUnit(builderUnit.getId(), moveDirection);
                    remainingActionPoints -= actionCostResponse.getMove();
                    builderUnit.setPosition(lastStructuredCoordinate.getWsCoordinate());
                    this.route.add(previousCell);
                    this.lastStructuredCoordinate = null;
                    System.out.println("Robot moved.");
                    System.out.println("New position of the robot: " + builderUnit.getPosition().getX() + ", " + builderUnit.getPosition().getY());

                    if (remainingActionPoints < actionCostResponse.getDrill() && remainingActionPoints < actionCostResponse.getExplode()) {
                        break;
                    }

                    continue;
                }

                // Break if we can't do anything
                if (remainingActionPoints < actionCostResponse.getDrill()
                        && remainingActionPoints < actionCostResponse.getExplode()) {
                    break;
                }

                // Watch neighbour cells
                // Todo: create map neighbour cells method in other utility class
                if (remainingActionPoints < actionCostResponse.getWatch()) {
                    break;
                }

                List<Scouting> neighbourCellList = this.apolloClient.watch(builderUnit.getId()).getScout();
                neighbourCellList = WsCoordinateUtils.sortScoutings(neighbourCellList, defaultDirection);

                remainingActionPoints -= actionCostResponse.getWatch();
                Map<ObjectType, List<Scouting>> neighbourCells = new HashMap<>();

                for (ObjectType objectType : ObjectType.values()) {
                    neighbourCells.put(objectType, new ArrayList<>());
                }

                for (Scouting cell : neighbourCellList) {
                    neighbourCells.get(cell.getObject()).add(cell);
                }

                // Flowchart - 4.
                if (neighbourCells.get(ObjectType.ROCK).size() > 0 && remainingActionPoints >= actionCostResponse.getDrill()) {
                    Coordinate coordinate = new Coordinate(neighbourCells.get(ObjectType.ROCK).get(0).getCord());
                    WsDirection direction = WsCoordinateUtils.moveDirection(builderUnit.getPosition(), coordinate.getWsCoordinate());
                    // Structure cell
                    this.apolloClient.structureTunnel(builderUnit.getId(), direction);
                    remainingActionPoints -= actionCostResponse.getDrill();
                    this.lastStructuredCoordinate = coordinate;
                    System.out.println("Robot drilled.(Fc4)");
                    System.out.println("Position of the drilling: " + coordinate.getWsCoordinate().getX() + ", " + coordinate.getWsCoordinate().getY());
                    continue;
                }

                // Flowchart - 5.
                if (neighbourCells.get(ObjectType.GRANITE).size() > 0 && remainingActionPoints >= actionCostResponse.getExplode() && remainingExplosives > 0) {
                    Coordinate coordinate = new Coordinate(neighbourCells.get(ObjectType.GRANITE).get(0).getCord());
                    WsDirection direction = WsCoordinateUtils.moveDirection(builderUnit.getPosition(), coordinate.getWsCoordinate());
                    // Explode cell
                    this.apolloClient.explodeCell(builderUnit.getId(), direction);
                    remainingActionPoints -= actionCostResponse.getExplode();
                    remainingExplosives--;
                    System.out.println("Robot exploded.(Fc5)");
                    System.out.println("Position of the explose: " + coordinate.getWsCoordinate().getX() + ", " + coordinate.getWsCoordinate().getY());
                    continue;
                }

                // Flowchart - 6.
                if (neighbourCells.get(ObjectType.TUNNEL).size() > 0 && remainingActionPoints >= actionCostResponse.getMove()) {
                    Coordinate coordinate = new Coordinate(neighbourCells.get(ObjectType.TUNNEL).get(0).getCord());

                    Scouting nextCell = null;

                    for (Scouting cell : neighbourCells.get(ObjectType.TUNNEL)) {
                        if (!route.contains(coordinate)
                                && !ignoredCoordinates.contains(coordinate)
                                && cell.getTeam().equals(ApolloConfiguration.user)) {
                            nextCell = cell;
                        }
                    }

                    // Checks if this is ours or not
                    if (nextCell != null) {
                        Coordinate previousCell = new Coordinate(builderUnit.getPosition());
                        WsDirection direction = WsCoordinateUtils.moveDirection(builderUnit.getPosition(), coordinate.getWsCoordinate());

                        // Move into that cell
                        this.apolloClient.moveBuilderUnit(builderUnit.getId(), direction);
                        remainingActionPoints -= actionCostResponse.getMove();
                        builderUnit.setPosition(coordinate.getWsCoordinate());
                        route.add(previousCell);
                        System.out.println("Robot moved. (Fc6.)");
                        System.out.println("New position of the robot: " + builderUnit.getPosition().getX() + ", " + builderUnit.getPosition().getY());

                        continue;
                    }
                }

                // End the turn if the robot can't do anything and the route is empty
                if (route.isEmpty()) {
                    break;
                }

                Scouting lastCellScout = null;

                for (Scouting scout : neighbourCells.get(ObjectType.TUNNEL)) {
                    if (new Coordinate(scout.getCord()).equals(this.route.get(this.route.size() - 1))) {
                        lastCellScout = scout;
                    }
                }

                // Flowchart - 7.
                if (lastCellScout != null && lastCellScout.getObject().equals(ObjectType.TUNNEL) && remainingActionPoints >= actionCostResponse.getMove()) {
                    Coordinate coordinate = new Coordinate(lastCellScout.getCord());
                    WsDirection direction = WsCoordinateUtils.moveDirection(builderUnit.getPosition(), coordinate.getWsCoordinate());

                    Coordinate previousCell = new Coordinate(builderUnit.getPosition());
                    // Move into that cell
                    this.apolloClient.moveBuilderUnit(builderUnit.getId(), direction);
                    remainingActionPoints -= actionCostResponse.getMove();
                    builderUnit.setPosition(coordinate.getWsCoordinate());
                    ignoredCoordinates.add(previousCell);
                    this.route.remove(this.route.size() - 1);
                    System.out.println("Robot moved (Fc7.)");
                    System.out.println("New position of the robot: " + builderUnit.getPosition().getX() + ", " + builderUnit.getPosition().getY());

                    continue;
                }

            }
        } catch (SoapResponseInvalidException e) {
            System.out.println("----------------- SoapResponseInvalidException has been thrown:");
            WsCoordinateUtils.print(e.getResponse());
        }
    }
}
