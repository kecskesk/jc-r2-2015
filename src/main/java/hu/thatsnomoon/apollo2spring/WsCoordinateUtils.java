package hu.thatsnomoon.apollo2spring;

import eu.loxon.centralcontrol.CommonResp;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;

/**
 *
 * @author NB57
 */


public class WsCoordinateUtils {
    /**
     * Prints a CommonResp object in readable format
     *
     * @param response to print
     */
    public static void print(CommonResp response) {
        System.out.println("------------------------------------------------------");
        System.out.println("Robot:\t" + response.getBuilderUnit());
        System.out.println("Action points:\t" + response.getActionPointsLeft());
        System.out.println("Explosives:\t" + response.getExplosivesLeft());
        System.out.println("Turns left:\t" + response.getTurnsLeft());
        System.out.println("Scores:");
        System.out.println("\tReward\t" + response.getScore().getReward());
        System.out.println("\tBonus\t" + response.getScore().getBonus());
        System.out.println("\tPenalty\t" + response.getScore().getPenalty());
        System.out.println("\tTotal\t" + response.getScore().getTotal());

    }

    /**
     * Compares two WsCoordinets if they are each others neighbor
     *
     * @param from
     * @param to
     * @return true if the two Cells are neighbors, false otherwise
     */
    public static boolean isNeighborCells(WsCoordinate from, WsCoordinate to) {
        return (from.getX() + 1) == to.getX()
                || (from.getX() - 1) == to.getX()
                || (from.getY() + 1) == to.getY()
                || (from.getY() - 1) == to.getY();
    }

    /**
     * Calculates the direction between two cells.
     *
     * @param from
     * @param to
     * @return  the direction in that points from the first cell to the second, if
     *          cells are not neighbors, it returns null
     */
    public static WsDirection moveDirection(WsCoordinate from, WsCoordinate to) {
        if (isNeighborCells(from, to)) {
            if (from.getX() == to.getX()) {
                if (from.getY() > to.getY()) {
                    // UP
                    return WsDirection.UP;
                } else if (from.getY() < to.getY()) {
                    // DOWN
                    return WsDirection.DOWN;
                } else {
                    System.out.println("The X and Y coordinates of the shuttle and the exit position are the same!!!");
                }
            } else if (from.getY() == to.getY()) {
                if (from.getX() > to.getX()) {
                    // RIGHT
                    return WsDirection.RIGHT;
                } else if (from.getX() < to.getX()) {
                    // LEFT
                    return WsDirection.LEFT;
                } else {
                    System.out.println("The X and Y coordinates of the shuttle and the exit position are the same!!!");
                }
            }
        }
        return null;
    }

    /**
     * Calculates the neighbor cell's coordinates in a given direction
     *
     * @param pos
     * @param dir
     * @return
     */
    public static WsCoordinate neighbourCellCoordinate(WsCoordinate pos, WsDirection dir) {
        WsCoordinate neighbour = new WsCoordinate();

        neighbour.setX(pos.getX());
        neighbour.setY(pos.getY());

        if (dir == WsDirection.UP) {
            neighbour.setY(pos.getY() + 1);
        }
        if (dir == WsDirection.DOWN) {
            neighbour.setY(pos.getY() - 1);
        }
        if (dir == WsDirection.RIGHT) {
            neighbour.setX(pos.getX() + 1);
        }
        if (dir == WsDirection.LEFT) {
            neighbour.setX(pos.getX() - 1);
        }

        return neighbour;
    }
}
