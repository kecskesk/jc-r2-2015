package hu.thatsnomoon.apollo2spring.controller;

import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;
import hu.thatsnomoon.apollo2spring.component.ApolloStandingComponent;
import hu.thatsnomoon.apollo2spring.exception.HttpInvalidParamsException;
import hu.thatsnomoon.apollo2spring.model.CellStrategyParams;
import hu.thatsnomoon.apollo2spring.model.DefaultStrategyParams;
import hu.thatsnomoon.apollo2spring.model.GoToRequestParams;
import hu.thatsnomoon.apollo2spring.service.GameRunnerService;
import hu.thatsnomoon.apollo2spring.service.StrategyChangerService;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
     * API Method for getting the actual standing map. It will contain the latest CommonResp, the known Map and the BuilderUnit
     *
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
     * @param params
     */
    @RequestMapping(value = "/changeToDefaultStrategyOfRobot", method = RequestMethod.POST)
    public void changeToDefaultStrategyOfBuilder(@RequestBody DefaultStrategyParams params) {
        try {
            strategyChangerService.changeToDefaultStrategyOfRobot(params.getId(), WsDirection.valueOf(params.getDirection()));
        } catch (IllegalArgumentException e) {
            Logger.getLogger(this.getClass()).error("Wrong direction name. Got: '" + params.getDirection() + "'.", e);
            throw new HttpInvalidParamsException();
        }
    }

    /**
     * REST APi for adding default strategy to a robot's strategy queue. For example you change the robots strategy to GoTo strategy, and
     * then you call this method. Then if the robot arrives to the goto destination, it will start the default strategy.
     *
     * @param params
     */
    @RequestMapping(value = "/addDefaultStrategyToRobot", method = RequestMethod.POST)
    public void addDefaultStrategyOfBuilder(@RequestBody DefaultStrategyParams params) {
        try {
            strategyChangerService.addDefaultStrategyToRobot(params.getId(), WsDirection.valueOf(params.getDirection()));
        } catch (IllegalArgumentException e) {
            Logger.getLogger(this.getClass()).error("Wrong direction name. Got: '" + params.getDirection() + "'.", e);
            throw new HttpInvalidParamsException();
        }
    }

    /**
     * REST Api for changing the robots strategy to goto strategy. After calling this, you should add an other strategy to the robot.
     *
     * @param params Should have x, y, id int fields
     */
    @RequestMapping(value = "/changeToGoToStrategyOfBuilder", method = RequestMethod.POST)
    public void changeToGoToStrategyOfBuilder(@RequestBody GoToRequestParams params) {
        WsCoordinate destination = new WsCoordinate();
        destination.setX(params.getX());
        destination.setY(params.getY());
        strategyChangerService.changeToGoToStrategyOfBuilder(params.getId(), destination);
    }

    /**
     * REST Api for adding default strategy to a robot's strategy queue. For example you change the robots strategy to GoTo strategy, and
     * then you call this method. Then if the robot arrives to the goto destination, it will start the default strategy.
     *
     * @param params Should have x, y, id int fields
     */
    @RequestMapping(value = "/addGoToStrategyToRobot", method = RequestMethod.POST)
    public void addGoToStrategyToRobot(@RequestBody GoToRequestParams params) {
        WsCoordinate destination = new WsCoordinate();
        destination.setX(params.getX());
        destination.setY(params.getY());
        strategyChangerService.addGoToStrategyToBuilder(params.getId(), destination);
    }

    /**
     * REST Api for changing the robots strategy to goto strategy. After calling this, you should add an other strategy to the robot.
     *
     * @param params Should have x, y, id int fields
     */
    @RequestMapping(value = "/changeToCellStrategyOfBuilder", method = RequestMethod.POST)
    public void changeToCellStrategyOfBuilder(@RequestBody CellStrategyParams params) {

        try {
            strategyChangerService.changeToCellStrategyOfRobot(params.getId(), WsDirection.valueOf(params.getDirection()));
        } catch (IllegalArgumentException e) {
            Logger.getLogger(this.getClass()).error("Wrong direction name. Got: '" + params.getDirection() + "'.", e);
            throw new HttpInvalidParamsException();
        }
    }

    /**
     * REST Api for changing the robots strategy to goto strategy. After calling this, you should add an other strategy to the robot.
     *
     * @param params Should have x, y, id int fields
     */
    @RequestMapping(value = "/addCellStrategyToBuilder", method = RequestMethod.POST)
    public void addCellStrategyToBuilder(@RequestBody CellStrategyParams params) {

        try {
            strategyChangerService.addCellStrategyToBuilder(params.getId(), WsDirection.valueOf(params.getDirection()));
        } catch (IllegalArgumentException e) {
            Logger.getLogger(this.getClass()).error("Wrong direction name. Got: '" + params.getDirection() + "'.", e);
            throw new HttpInvalidParamsException();
        }
    }



}
