package hu.thatsnomoon.apollo2spring.controller;

import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;
import hu.thatsnomoon.apollo2spring.component.ApolloStandingComponent;
import hu.thatsnomoon.apollo2spring.exception.HttpInvalidParamsException;
import hu.thatsnomoon.apollo2spring.service.GameRunnerService;
import hu.thatsnomoon.apollo2spring.service.StrategyChangerService;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author David
 */
@RestController
public class StandingRestController {

    @Autowired
    GameRunnerService gameRunnerService;

    @Autowired
    ApolloStandingComponent standingComponent;

    @Autowired
    StrategyChangerService strategyChangerService;

    /**
     * API Method for getting the actual standing map.
     * It will contain the latest CommonResp, the known Map and the BuilderUnit
     * @return
     */
    @RequestMapping(value = "/getStanding", method = RequestMethod.GET)
    public Map<String, Object> getStanding() {
        return standingComponent.getStanding();
    }

    /**
     * Starts the game runner
     */
    @RequestMapping(value = "/startGame", method = RequestMethod.POST)
    public void startGame() {
        gameRunnerService.runGame();
    }

    /**
     * Stops the game runner
     */
    @RequestMapping(value = "/stopGame", method = RequestMethod.POST)
    public void stopGame() {
        gameRunnerService.stopGame();
    }

    /**
     * Rest API for changing the selected
     *
     * @param id
     * @param direction
     */
    @RequestMapping(value = "/changeToDefaultStrategyOfRobot", method = RequestMethod.POST)
    public void changeToDefaultStrategyOfBuilder(@RequestParam int id, @RequestParam String direction) {
        try {
            strategyChangerService.changeToDefaultStrategyOfRobot(id, WsDirection.valueOf(direction));
        } catch (IllegalArgumentException e) {
            Logger.getLogger(this.getClass()).error("Wrong direction name. Got: '" + direction + "'.", e);
            throw new HttpInvalidParamsException();
        }
    }

    /**
     * REST APi for adding default strategy to a robot's strategy queue. For example you change the robots strategy to GoTo strategy, and
     * then you call this method. Then if the robot arrives to the goto destination, it will start the default strategy.
     *
     * @param id
     * @param direction
     */
    @RequestMapping(value = "/addDefaultStrategyToRobot", method = RequestMethod.POST)
    public void addDefaultStrategyToRobot(@RequestParam int id, @RequestParam String direction) {
        try {
            strategyChangerService.addDefaultStrategyToRobot(id, WsDirection.valueOf(direction));
        } catch (IllegalArgumentException e) {
            Logger.getLogger(this.getClass()).error("Wrong direction name. Got: '" + direction + "'.", e);
            throw new HttpInvalidParamsException();
        }
    }

    /**
     * REST Api for changing the robots strategy to goto strategy. After calling this, you should add an other strategy to the robot.
     *
     * @param id
     * @param x
     * @param y
     */
    @RequestMapping(value = "/changeToGoToStrategyOfBuilder", method = RequestMethod.POST)
    public void changeToGoToStrategyOfBuilder(@RequestParam int id, @RequestParam int x, @RequestParam int y) {
        WsCoordinate destination = new WsCoordinate();
        destination.setX(x);
        destination.setY(y);
        strategyChangerService.changeToGoToStrategyOfBuilder(id, destination);
    }

    /**
     * REST Api for adding default strategy to a robot's strategy queue. For example you change the robots strategy to GoTo strategy, and
     * then you call this method. Then if the robot arrives to the goto destination, it will start the default strategy.
     *
     * @param id
     * @param x
     * @param y
     */
    @RequestMapping(value = "/addGoToStrategyToRobot", method = RequestMethod.POST)
    public void addGoToStrategyToRobot(@RequestParam int id, @RequestParam int x, @RequestParam int y) {
        WsCoordinate destination = new WsCoordinate();
        destination.setX(x);
        destination.setY(y);
        strategyChangerService.addGoToStrategyToBuilder(id, destination);
    }

}
