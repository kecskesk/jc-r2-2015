package hu.thatsnomoon.apollo2spring.service;

import eu.loxon.centralcontrol.IsMyTurnResponse;
import eu.loxon.centralcontrol.StartGameResponse;
import hu.thatsnomoon.apollo2spring.component.ApolloStandingComponent;
import hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException;
import hu.thatsnomoon.apollo2spring.model.BuilderUnit;
import hu.thatsnomoon.apollo2spring.strategy.DefaultStrategy;
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
                Thread.sleep(300);
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
                int currentRobot = imtr.getResult().getBuilderUnit();

                if (imtr.getResult().getTurnsLeft() % 10 == 0) {
                    if (standingComponent.getUnits().get(currentRobot).getStrategy() instanceof DefaultStrategy) {
                        ((DefaultStrategy) standingComponent.getUnits().get(currentRobot).getStrategy()).setDefaultDirection(WsCoordinateUtils.UP_ORDER[(currentRobot + imtr.getResult().getTurnsLeft() / 10) % 4]);
                    }
                }
                // If it is our turn, query the current robotID and
                // order the robot to step
                BuilderUnit builderUnit = standingComponent.getUnits().get(currentRobot);
                builderUnit.step();
                lastRobotId = currentRobot;
            }
        }
    }

    public void stopGame () {
        this.stopRunning = true;
    }
}
