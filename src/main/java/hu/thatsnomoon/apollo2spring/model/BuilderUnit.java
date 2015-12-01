package hu.thatsnomoon.apollo2spring.model;

import hu.thatsnomoon.apollo2spring.strategy.Strategy;
import eu.loxon.centralcontrol.WsCoordinate;
import java.util.List;

/**
 *
 * @author Dombi Soma
 */
public class BuilderUnit {

    private WsCoordinate position;
    private int id;
    private List<Strategy> strategies;

    public BuilderUnit(WsCoordinate position, int id, List<Strategy> strategies) {
        this.position = position;
        this.id = id;
        this.strategies = strategies;
    }

    public void step() {
        if (strategies.size() > 1 && strategies.get(0).isEnded()) {
            strategies.remove(0);
        }

        if (strategies.size() > 0) {
            strategies.get(0).step(this);
        }
    }

    public WsCoordinate getPosition() {
        return position;
    }

    public void setPosition(WsCoordinate position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Strategy> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<Strategy> strategies) {
        this.strategies = strategies;
    }
}
