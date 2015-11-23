package hu.thatsnomoon.apollo2spring;

/**
 *
 * @author Dombi Soma
 */


public interface Strategy {

    /**
     * Time in milisecs while a round lasts
     */
    static long ROUND_TIME = 1000;

    void step(BuilderUnit builderUnit);
}
