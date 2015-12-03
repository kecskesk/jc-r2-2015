package hu.thatsnomoon.apollo2spring.strategy;

import eu.loxon.centralcontrol.ActionCostResponse;
import eu.loxon.centralcontrol.WsCoordinate;
import hu.thatsnomoon.apollo2spring.model.BuilderUnit;
import hu.thatsnomoon.apollo2spring.model.Coordinate;
import hu.thatsnomoon.apollo2spring.service.ApolloClientService;
import hu.thatsnomoon.apollo2spring.utils.WsCoordinateUtils;
import java.util.HashMap;
import java.util.HashSet;

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
    private static HashMap<Coordinate, Coordinate> routeTable = new HashMap<>();

    private final ApolloClientService apolloClient;

    public CellStrategy(ApolloClientService apolloClient) {
        this.apolloClient = apolloClient;
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
        } catch (Exception e) {
        }

    }

    public static HashMap<Coordinate, Coordinate> createRouteTable(Coordinate shuttlePos, int xBorder, int yBorder) {
        HashMap<Coordinate, Coordinate> localRoute = new HashMap<>();

        int counter = 0;
        int x = 0, y = 0;
//        x = shuttlePos.getWsCoordinate().getX();
//        y = shuttlePos.getWsCoordinate().getY();

        while (counter < xBorder * yBorder) {
            WsCoordinate from = WsCoordinateUtils.createFromXY(x, y);
            System.out.println(counter);
            System.out.println("X : " + x + "Y : " + y);
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
            result.put(new Coordinate(localeToWorld(c.getWsCoordinate(), shuttlePos.getWsCoordinate())),
                    new Coordinate(localeToWorld(localRoute.get(c).getWsCoordinate(), shuttlePos.getWsCoordinate())));
        }

        for (Coordinate c : result.keySet()) {
            System.out.println(c + " || " + result.get(c));
        }
        return result;
    }

    public static WsCoordinate localeToWorld(WsCoordinate local, WsCoordinate transition) {
        WsCoordinate world = new WsCoordinate();

        world.setX(local.getX() + transition.getX());
        world.setY(local.getY() + transition.getY());

        return world;
    }

}
