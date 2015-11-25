package hu.thatsnomoon.apollo2spring.controller;

import hu.thatsnomoon.apollo2spring.component.ApolloStandingComponent;
import hu.thatsnomoon.apollo2spring.service.GameRunnerService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/getStanding", method = RequestMethod.GET)
    public Map<String, Object> getStanding() {
        return standingComponent.getStanding();
    }

    @RequestMapping(value = "/startGame", method = RequestMethod.POST)
    public void startGame() {
        gameRunnerService.runGame();
    }

    @RequestMapping(value = "/stopGame", method = RequestMethod.POST)
    public void stopGame() {
        gameRunnerService.stopGame();
    }
}
