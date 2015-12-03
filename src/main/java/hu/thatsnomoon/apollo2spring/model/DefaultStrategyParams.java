package hu.thatsnomoon.apollo2spring.model;

/**
 * DefaultStrategy parameter class
 * @author kkrisz
 */

public class DefaultStrategyParams {
    private int id;
    private String direction;

    public DefaultStrategyParams(int id, String direction) {
        this.id = id;
        this.direction = direction;
    }

    public DefaultStrategyParams() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
