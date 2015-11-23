package hu.thatsnomoon.apollo2spring;

import eu.loxon.centralcontrol.WsCoordinate;
import java.util.Objects;

/**
 *
 * @author David
 */
public class Coordinate {

    private final WsCoordinate wsCoordinate;

    public Coordinate() {
        this.wsCoordinate = new WsCoordinate();
    }

    public Coordinate(WsCoordinate wsCoordinate) {
        this.wsCoordinate = wsCoordinate;
    }

    public WsCoordinate getWsCoordinate() {
        return wsCoordinate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.wsCoordinate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coordinate other = (Coordinate) obj;
        if (this.wsCoordinate == null || other.wsCoordinate == null) {
            return false;
        }
        return (this.wsCoordinate.getX() == other.wsCoordinate.getX()
                && this.wsCoordinate.getY() == other.wsCoordinate.getY());
    }

}
