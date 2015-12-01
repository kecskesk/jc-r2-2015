package hu.thatsnomoon.apollo2spring.strategy;

import hu.thatsnomoon.apollo2spring.model.BuilderUnit;

/**
 *
 * @author Dombi Soma
 */


public interface Strategy {

    /**
     * Time in milisecs while a round lasts
     */
    static long ROUND_TIME = 1000;

    /**
     * One step means one round of the given builder unit. The step shouldn't last more than the ROUND_TIME value in milisecs.
     * @param builderUnit The builder Unit to step with.
     */
    void step(BuilderUnit builderUnit);

    /**
     * Shows that the strategy is ready or not.
     * For exmple if you implement a GoTo strategy, then it should return false while
     * the builder unit is not at the target position, and returns
     * true when the builder unit is on the target coordinate.
     * @return True is the strategy is ended.
     */
    boolean isEnded();
}
