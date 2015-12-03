package hu.thatsnomoon.apollo2spring.strategy;

import eu.loxon.centralcontrol.ActionCostResponse;
import eu.loxon.centralcontrol.ObjectType;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;
import hu.thatsnomoon.apollo2spring.configuration.ApolloConfiguration;
import hu.thatsnomoon.apollo2spring.model.BuilderUnit;
import hu.thatsnomoon.apollo2spring.model.Coordinate;
import hu.thatsnomoon.apollo2spring.service.ApolloClientService;
import hu.thatsnomoon.apollo2spring.utils.WsCoordinateUtils;
import java.util.HashMap;
import java.util.List;

/**
 * This strategy contains the logic for building a durable cell of tunnels.
 *
 * @author Soma
 */
public class CellStrategy implements Strategy {

    /**
     * Stores the next target cell for each cell.
     *
     * Key: where the builder unit is on the PATH/ROUTE. Value: where the
     * builder unit should move on, to stay on the PATH/ROUTE.
     */
    private static HashMap<Coordinate, Coordinate> routeTable;

    private final ApolloClientService apolloClient;

    public CellStrategy(ApolloClientService apolloClient, WsDirection dir, WsCoordinate pos) {
        this.apolloClient = apolloClient;
        this.routeTable = new HashMap<>();

        HashMap<Coordinate, Coordinate> temp = createRouteTable(new Coordinate(pos), 100, 100);

        for (Coordinate c : temp.keySet()) {
            WsCoordinate key = c.getWsCoordinate();
            WsCoordinate value = temp.get(c).getWsCoordinate();

            WsCoordinate newKey = rotate(key, degreeFromDirectoin(dir));
            WsCoordinate newValue = rotate(value, degreeFromDirectoin(dir));

            routeTable.put(new Coordinate(newKey), new Coordinate(newValue));
        }
    }

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

            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < Strategy.ROUND_TIME) {
                List<Scouting> neighbourCellList = this.apolloClient.watch(builderUnit.getId()).getScout();

                Coordinate moveTargetCoordinate = routeTable.get(new Coordinate(builderUnit.getPosition()));

                WsDirection moveDirection = WsCoordinateUtils.moveDirection(builderUnit.getPosition(), moveTargetCoordinate.getWsCoordinate());

                Scouting moveTarget = WsCoordinateUtils.getDirectionScoutingFromWatch(neighbourCellList, builderUnit.getPosition(), moveDirection);

                if (moveTarget.getObject().equals(ObjectType.ROCK) && remainingActionPoints >= actionCostResponse.getDrill()) {
                    // Structure cell
                    this.apolloClient.structureTunnel(builderUnit.getId(), moveDirection);
                    remainingActionPoints -= actionCostResponse.getDrill();
                    System.out.println("Robot drilled.");
                    System.out.println("Position of the drilling: " + moveTarget.getCord().getX() + ", " + moveTarget.getCord().getY());
                    continue;
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
            }

        } catch (Exception e) {
        }

    }

    public static HashMap<Coordinate, Coordinate> createRouteTable(Coordinate firstCell, int xBorder, int yBorder) {
        HashMap<Coordinate, Coordinate> localRoute = new HashMap<>();

        int counter = 0;
        int x = 0, y = 0;

        while (counter < xBorder * yBorder) {
            WsCoordinate from = WsCoordinateUtils.createFromXY(x, y);

            // Vertical border
            if (x == 0) {
                if (y % 2 == 0) {
                    // UP
                    y++;
                } else {
                    // RIGHT
                    x++;
                }

                WsCoordinate to = WsCoordinateUtils.createFromXY(x, y);
                localRoute.put(new Coordinate(from), new Coordinate(to));
                counter++;
                continue;
            }

            // Horizontal border
            if (y == 0) {
                if (x % 2 == 0) {
                    // UP
                    y++;
                } else {
                    // RIGHT
                    x++;
                }

                WsCoordinate to = WsCoordinateUtils.createFromXY(x, y);
                localRoute.put(new Coordinate(from), new Coordinate(to));
                counter++;
                continue;
            }

            // Diagonal turning
            if (x == y) {
                if (x % 2 == 0) {
                    // LEFT
                    x--;
                } else {
                    // DOWN
                    y--;
                }
                WsCoordinate to = WsCoordinateUtils.createFromXY(x, y);
                localRoute.put(new Coordinate(from), new Coordinate(to));
                counter++;
                continue;
            }

            // Vertical movement
            if (x > y) {
                if (x % 2 == 0) {
                    // UP
                    y++;
                } else {
                    // DOWN
                    y--;
                }
                WsCoordinate to = WsCoordinateUtils.createFromXY(x, y);
                localRoute.put(new Coordinate(from), new Coordinate(to));
                counter++;
                continue;
            }

            // Horizontal movement
            if (x < y) {
                if (y % 2 == 0) {
                    // LEFT
                    x--;
                } else {
                    // RIGHT
                    x++;
                }
                WsCoordinate to = WsCoordinateUtils.createFromXY(x, y);
                localRoute.put(new Coordinate(from), new Coordinate(to));
                counter++;
                continue;
            }

        }

        // CONVERTING TO WORLD COORDINATE
        HashMap<Coordinate, Coordinate> result = new HashMap<>();

        for (Coordinate c : localRoute.keySet()) {
            result.put(new Coordinate(localeToWorld(c.getWsCoordinate(), firstCell.getWsCoordinate())),
                    new Coordinate(localeToWorld(localRoute.get(c).getWsCoordinate(), firstCell.getWsCoordinate())));
        }

        return result;
    }

    public static WsCoordinate localeToWorld(WsCoordinate local, WsCoordinate transition) {
        WsCoordinate world = new WsCoordinate();

        world.setX(local.getX() + transition.getX());
        world.setY(local.getY() + transition.getY());

        return world;
    }

    /**
     * Rotate coordinates
     *
     * @see
     * https://www.siggraph.org/education/materials/HyperGraph/modeling/mod_tran/2drota.htm
     * @param pos
     * @param degree
     * @return
     */
    public static WsCoordinate rotate(WsCoordinate pos, double degree) {
        WsCoordinate result = new WsCoordinate();

        double rad = degree / 180 * Math.PI;

        int x = (int) Math.round((pos.getX() * Math.cos(rad) - pos.getY() * Math.sin(rad)));
        int y = (int) Math.round((pos.getY() * Math.cos(rad) + pos.getX() * Math.sin(rad)));

        result.setX(x);
        result.setY(y);

        return result;
    }

    /**
     *
     * @param dir
     * @return
     */
    public static int degreeFromDirectoin(WsDirection dir) {
        if (dir.equals(WsDirection.LEFT)) {
            return 90;
        }
        if (dir.equals(WsDirection.DOWN)) {
            return 180;
        }
        if (dir.equals(WsDirection.RIGHT)) {
            return 270;
        }
        return 0;

    }
}
