package hu.thatsnomoon.apollo2spring.strategy;

import eu.loxon.centralcontrol.ObjectType;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;
import hu.thatsnomoon.apollo2spring.BuilderUnit;
import hu.thatsnomoon.apollo2spring.Coordinate;
import hu.thatsnomoon.apollo2spring.Strategy;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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

    private enum Actions {

        MOVE,
        STRUCTURE,
        EXPLODE
    }

    private final WsDirection defaultDirection;

    private final Set<Actions> forbiddenActions;

    private final Deque<Coordinate> route;

    private final Set<Coordinate> ignoredCoordinates;

    private Coordinate lastStructuredCoordinate;

    public DefaultStrategy() {
        this.defaultDirection = WsDirection.DOWN;
        this.forbiddenActions = new HashSet<>();
        this.route = new ArrayDeque<>();
        this.ignoredCoordinates = new HashSet<>();
    }

    public DefaultStrategy(WsDirection defaultDirection) {
        this.defaultDirection = defaultDirection;
        this.forbiddenActions = new HashSet<>();
        this.route = new ArrayDeque<>();
        this.ignoredCoordinates = new HashSet<>();
    }

    /**
     * Steps in a round with the given builderUnit
     *
     * @param builderUnit
     */
    @Override
    public void step(BuilderUnit builderUnit) {
        long startTime = System.currentTimeMillis();

        this.forbiddenActions.clear();
        this.ignoredCoordinates.clear();
        this.lastStructuredCoordinate = null;

        while (System.currentTimeMillis() - startTime < ROUND_TIME) {
            // Todo
            Coordinate shuttlePosition = new Coordinate(new WsCoordinate());

            // Flowchart - 1.
            if (shuttlePosition.equals(new Coordinate(builderUnit.getPosition()))) {
                //Todo: watch
                ObjectType exitCellType = ObjectType.TUNNEL;

                // Flowchart - 2.
                if (exitCellType == ObjectType.TUNNEL) {
                    // todo Move into cell
                    continue;
                } else if (exitCellType == ObjectType.ROCK) {
                    // todo Structure cell
                    continue;
                } else if (exitCellType == ObjectType.GRANITE) {
                    // todo Explode cell
                    continue;
                } else {
                    break;
                }
            }

            // Flowchart - 3.
            if (this.lastStructuredCoordinate != null) {
                Coordinate previousCell = new Coordinate(builderUnit.getPosition());

                // todo Move into last lastStructuredCoordinate
                this.route.add(previousCell);
                this.lastStructuredCoordinate = null;
                continue;
            }

            // Todo: watch neighbour cells
            // Todo: create map neighbour cells method in other utility class
            List<Scouting> neighbourCellList = new ArrayList<>();
            Map<ObjectType, List<Scouting>> neighbourCells = new HashMap<>();

            for (ObjectType objectType : ObjectType.values()) {
                neighbourCells.put(objectType, new ArrayList<>());
            }

            for (Scouting cell : neighbourCellList) {
                neighbourCells.get(cell.getObject()).add(cell);
            }

            // Flowchart - 4.
            if (neighbourCells.get(ObjectType.ROCK).size() > 0) {
                Coordinate coordinate = new Coordinate(neighbourCells.get(ObjectType.ROCK).get(0).getCord());
                // todo Structure cell
                this.lastStructuredCoordinate = coordinate;
                continue;
            }

            // Flowchart - 5.
            if (neighbourCells.get(ObjectType.GRANITE).size() > 0) {
                Coordinate coordinate = new Coordinate(neighbourCells.get(ObjectType.GRANITE).get(0).getCord());
                // todo Explode cell
                continue;
            }

            // Flowchart - 6.
            if (neighbourCells.get(ObjectType.TUNNEL).size() > 0) {
                Coordinate coordinate = new Coordinate(neighbourCells.get(ObjectType.TUNNEL).get(0).getCord());

                // todo Check if this is ours or not

                if (!route.contains(coordinate) && !ignoredCoordinates.contains(coordinate)) {
                    Coordinate previousCell = new Coordinate(builderUnit.getPosition());
                    // todo Move into that cell
                    route.add(previousCell);

                    continue;
                }
            }

            // End the turn if the robot can't do anything and the route is empty
            if (route.isEmpty()) {
                break;
            }

            Scouting lastCellScout = null;

            for (Scouting scout : neighbourCells.get(ObjectType.TUNNEL)) {
                if (new Coordinate(scout.getCord()).equals(this.route.getLast())) {
                    lastCellScout = scout;
                }
            }

            // Flowchart - 7.
            if (lastCellScout != null && lastCellScout.getObject().equals(ObjectType.TUNNEL)) {
                Coordinate coordinate = new Coordinate(lastCellScout.getCord());

                // todo Check if this is ours or not

                if (!route.contains(coordinate) && !ignoredCoordinates.contains(coordinate)) {
                    Coordinate previousCell = new Coordinate(builderUnit.getPosition());
                    // todo Move into that cell
                    route.add(previousCell);

                    continue;
                }
            }

        }
    }
}
