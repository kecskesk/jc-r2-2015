package hu.thatsnomoon.apollo2spring.model;

import hu.thatsnomoon.apollo2spring.strategy.Strategy;
import eu.loxon.centralcontrol.WsCoordinate;

/**
 *
 * @author Dombi Soma
 */
public class BuilderUnit {

    private WsCoordinate position;
    private int id;
    private Strategy strategy;

    public BuilderUnit(WsCoordinate position, int id, Strategy strategy) {
        this.position = position;
        this.id = id;
        this.strategy = strategy;
    }

    public void step(){
        strategy.step(this);
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

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
