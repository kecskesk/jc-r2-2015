package hu.thatsnomoon.apollo2spring.utils;

import eu.loxon.centralcontrol.CommonResp;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author NB57
 */
public class WsCoordinateUtils {

    public static final WsDirection[] UP_ORDER = {WsDirection.UP, WsDirection.RIGHT, WsDirection.LEFT, WsDirection.DOWN};
    public static final WsDirection[] DOWN_ORDER = {WsDirection.DOWN, WsDirection.LEFT, WsDirection.RIGHT, WsDirection.UP};
    public static final WsDirection[] RIGHT_ORDER = {WsDirection.RIGHT, WsDirection.UP, WsDirection.DOWN, WsDirection.LEFT};
    public static final WsDirection[] LEFT_ORDER = {WsDirection.LEFT, WsDirection.DOWN, WsDirection.UP, WsDirection.RIGHT};

    public static final WsDirection[][] LIST_OF_ORDERS = {UP_ORDER, DOWN_ORDER, RIGHT_ORDER, LEFT_ORDER};

    public static final Map<WsDirection, WsCoordinate> directionCoordinate;

    static {
        WsCoordinate up = new WsCoordinate();
        up.setY(1);
        up.setX(0);

        WsCoordinate down = new WsCoordinate();
        down.setY(-1);
        down.setX(0);

        WsCoordinate right = new WsCoordinate();
        right.setY(0);
        right.setX(1);

        WsCoordinate left = new WsCoordinate();
        left.setY(0);
        left.setX(-1);

        directionCoordinate = new HashMap<>();
        directionCoordinate.put(WsDirection.UP, up);
        directionCoordinate.put(WsDirection.DOWN, down);
        directionCoordinate.put(WsDirection.RIGHT, right);
        directionCoordinate.put(WsDirection.LEFT, left);
    }

    public static WsCoordinate positionOfBuilderUnit(List<Scouting> neighbors) {

        int avgX = 0;
        int avgY = 0;
        for (Scouting cell : neighbors) {
            avgX += cell.getCord().getX();
            avgY += cell.getCord().getY();
        }

        WsCoordinate pos = new WsCoordinate();
        pos.setX(avgX / 4);
        pos.setY(avgY / 4);

        return pos;
    }

    @Deprecated
    public static List<Scouting> sortScoutings(List<Scouting> neighbors, WsDirection dir) {
        List<Scouting> result = new ArrayList<>();

        // Calculating the posiotion of the robot.
        // It is the average of the scanned cell's coordinates.
        int avgX = 0;
        int avgY = 0;
        for (Scouting cell : neighbors) {
            avgX += cell.getCord().getX();
            avgY += cell.getCord().getY();
        }
        WsCoordinate pos = new WsCoordinate();
        pos.setX(avgX / 4);
        pos.setY(avgY / 4);

        for (WsDirection[] order : LIST_OF_ORDERS) {
            if (order[0] == dir) {
                for (WsDirection tempDir : order) {
                    for (Scouting scouting : neighbors) {
                        if (moveDirection(pos, scouting.getCord()) == tempDir) {
                            result.add(scouting);
                        }
                    }
                }
            }
        }

        return result;
    }

    public static String commonRespToString(CommonResp response) {
        StringBuilder sb = new StringBuilder();
        sb.append("Robot:\t").append(response.getBuilderUnit());
        sb.append("\n");
        sb.append("Action points:\t").append(response.getActionPointsLeft());
        sb.append("\n");
        sb.append("Explosives:\t").append(response.getExplosivesLeft());
        sb.append("\n");
        sb.append("Turns left:\t").append(response.getTurnsLeft());
        sb.append("\n");
        sb.append("Scores:");
        sb.append("\n");
        sb.append("\tReward\t").append(response.getScore().getReward());
        sb.append("\n");
        sb.append("\tBonus\t").append(response.getScore().getBonus());
        sb.append("\n");
        sb.append("\tPenalty\t").append(response.getScore().getPenalty());
        sb.append("\n");
        sb.append("\tTotal\t").append(response.getScore().getTotal());
        sb.append("\n");
        sb.append("Type:\t").append(response.getType());
        sb.append("\n");
        sb.append("Message:\t").append(response.getMessage());
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Prints a CommonResp object in readable format
     *
     * @param response to print
     */
    public static void print(CommonResp response) {
        System.out.println(commonRespToString(response));
    }

    /**
     * Compares two WsCoordinets if they are at the same coordinate
     *
     * @param first
     * @param second
     * @return true if the two Cells are at the same coordinate, false otherwise
     */
    public static boolean isSameCells(WsCoordinate first, WsCoordinate second) {
        return first.getX() == second.getX() && first.getY() == second.getY();
    }

    /**
     * Compares two WsCoordinets if they are each others neighbor
     *
     * @param from
     * @param to
     * @return true if the two Cells are neighbors, false otherwise
     */
    public static boolean isNeighborCells(WsCoordinate from, WsCoordinate to) {
        if (from.getX() == to.getX()) {
            if ((from.getY() - 1) == to.getY() || (from.getY() + 1) == to.getY()) {
                return true;
            }
        } else if (from.getY() == to.getY()) {
            if ((from.getX() - 1) == to.getX() || (from.getX() + 1) == to.getX()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Calculates the direction between two cells.
     *
     * @param from
     * @param to
     * @return the direction in that points from the first cell to the second,
     * if cells are not neighbors, it returns null
     */
    public static WsDirection moveDirection(WsCoordinate from, WsCoordinate to) {
        if (isNeighborCells(from, to)) {
            if (from.getX() == to.getX()) {
                if (from.getY() < to.getY()) {
                    // UP
                    return WsDirection.UP;
                } else if (from.getY() > to.getY()) {
                    // DOWN
                    return WsDirection.DOWN;
                } else {
                    System.out.println("The X and Y coordinates of the shuttle and the exit position are the same!!!");
                }
            } else if (from.getY() == to.getY()) {
                if (from.getX() < to.getX()) {
                    // RIGHT
                    return WsDirection.RIGHT;
                } else if (from.getX() > to.getX()) {
                    // LEFT
                    return WsDirection.LEFT;
                } else {
                    System.out.println("The X and Y coordinates of the shuttle and the exit position are the same!!!");
                }
            }
        }
        throw new RuntimeException("Tried to step on NOT neighbouring cell.");
    }

    /**
     * Calculates the coordinate from origin coordinate and direction.
     *
     * @param from
     * @param direction
     * @return the coordinate
     */
    public static WsCoordinate directionToCoordinate(WsCoordinate from, WsDirection direction) {
        WsCoordinate coordinate = new WsCoordinate();
        coordinate.setX(from.getX() + directionCoordinate.get(direction).getX());
        coordinate.setY(from.getY() + directionCoordinate.get(direction).getY());
        return coordinate;
    }

    public static Scouting getDirectionScoutingFromWatch(List<Scouting> scoutings, WsCoordinate pos, WsDirection dir) {
        if (dir == WsDirection.UP) {
            for (Scouting s : scoutings) {
                if (s.getCord().getY() - 1 == pos.getY()) {
                    return s;
                }
            }
        }
        if (dir == WsDirection.DOWN) {
            for (Scouting s : scoutings) {
                if (s.getCord().getY() + 1 == pos.getY()) {
                    return s;
                }
            }
        }
        if (dir == WsDirection.RIGHT) {
            for (Scouting s : scoutings) {
                if (s.getCord().getX() - 1 == pos.getX()) {
                    return s;
                }
            }
        }
        if (dir == WsDirection.LEFT) {
            for (Scouting s : scoutings) {
                if (s.getCord().getX() + 1 == pos.getX()) {
                    return s;
                }
            }
        }
        return null;
    }

    public static WsDirection oppositeDirection(WsDirection dir) {
        if (dir == WsDirection.DOWN) {
            return WsDirection.UP;
        }
        if (dir == WsDirection.UP) {
            return WsDirection.DOWN;
        }
        if (dir == WsDirection.LEFT) {
            return WsDirection.RIGHT;
        }
        return WsDirection.LEFT;
    }

    public static WsCoordinate createFromXY(int x, int y) {
        WsCoordinate result = new WsCoordinate();
        result.setX(x);
        result.setY(y);
        return result;
    }
}
