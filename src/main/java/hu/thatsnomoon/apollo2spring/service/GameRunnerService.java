package hu.thatsnomoon.apollo2spring.service;

import eu.loxon.centralcontrol.IsMyTurnResponse;
import eu.loxon.centralcontrol.StartGameResponse;
import hu.thatsnomoon.apollo2spring.component.ApolloStandingComponent;
import hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException;
import hu.thatsnomoon.apollo2spring.model.BuilderUnit;
import hu.thatsnomoon.apollo2spring.strategy.DefaultStrategy;
import hu.thatsnomoon.apollo2spring.strategy.Strategy;
import hu.thatsnomoon.apollo2spring.utils.WsCoordinateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public class GameRunnerService {

    public static final int MIN_INTERVAL = 200;

    @Autowired
    private ApolloClientService apolloClient;

    @Autowired
    private ApolloStandingComponent standingComponent;

    private boolean stopRunning;

    private static final Logger logger = Logger.getLogger(GameRunnerService.class);

    @Async
    public void runGame() {

        this.stopRunning = false;

        logger.info("Spring loaded, trying to start the game...");

        StartGameResponse startGameResponse;

        try {
            startGameResponse = apolloClient.startGame();
        } catch (SoapResponseInvalidException e) {
            logger.error("Couldn't start game. The game runner now stops.", e);
            return;
        }

        logger.info("Game has been started.");
        logger.debug("First state is the following:");
        logger.debug(WsCoordinateUtils.commonRespToString(startGameResponse.getResult()));

        int lastRobotId = -1;

        while (!stopRunning) {
            try {
                Thread.sleep(MIN_INTERVAL);
            } catch (InterruptedException ex) {
                logger.warn("Runner can't sleep.", ex);
                continue;
            }

            IsMyTurnResponse imtr;

            try {
                imtr = apolloClient.isMyTurn();
            } catch (SoapResponseInvalidException e) {
                logger.warn("SoapResponseInvalidException has been thrown by isMyTurn. Response is the following:");
                logger.warn(WsCoordinateUtils.commonRespToString(e.getResponse()));
                continue;
            }

            if (imtr.getResult().getTurnsLeft() == 0) {
                break;
            }

            if (imtr.isIsYourTurn() && imtr.getResult().getBuilderUnit() != lastRobotId) {
                long startTime = System.currentTimeMillis();

                int currentRobot = imtr.getResult().getBuilderUnit();
                BuilderUnit builderUnit = standingComponent.getUnits().get(currentRobot);

                if (imtr.getResult().getTurnsLeft() % 10 == 0) {
                    if (builderUnit.getStrategies() instanceof DefaultStrategy) {
                        ((DefaultStrategy) builderUnit.getStrategies()).setDefaultDirection(WsCoordinateUtils.UP_ORDER[(currentRobot + imtr.getResult().getTurnsLeft() / 10) % 4]);
                    }
                }
                /**
                 *
                 * If it is our turn, query the current robotID and order the robot to step. If the current strategy is ended, then we
                 * should call a new step on the robot, to change the previous strategy to the new one. For example, if the current strategy
                 * is Goto, and the robot arrived to the target, the strategy will be ended, so we could call the step once more, to do an
                 * other strategy in this round.
                 */

                builderUnit.step();

                lastRobotId = currentRobot;
            }
        }
    }

    public void stopGame() {
        this.stopRunning = true;
    }
}
