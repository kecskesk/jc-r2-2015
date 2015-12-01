package hu.thatsnomoon.apollo2spring.service;

import com.google.common.collect.Lists;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;
import hu.thatsnomoon.apollo2spring.component.ApolloStandingComponent;
import hu.thatsnomoon.apollo2spring.strategy.DefaultStrategy;
import hu.thatsnomoon.apollo2spring.strategy.GoToStrategy;
import hu.thatsnomoon.apollo2spring.strategy.Strategy;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public class StrategyChangerService {

    @Autowired
    private ApolloStandingComponent standingComponent;

    @Autowired
    private ApolloClientService apolloClientService;

    public void changeToDefaultStrategyOfRobot( int unitId, WsDirection direction) {
        standingComponent.getUnits().get(unitId).setStrategies( Lists.newArrayList(new DefaultStrategy(apolloClientService, direction)));
    }

    public void changeToGoToStrategyOfBuilder( int unitId, WsCoordinate destination) {
        standingComponent.getUnits().get(unitId).setStrategies( Lists.newArrayList(new GoToStrategy(apolloClientService, destination)));
    }

    public void addDefaultStrategyToRobot( int unitId, WsDirection direction) {
        standingComponent.getUnits().get(unitId).getStrategies().add(new DefaultStrategy(apolloClientService, direction));
    }

    public void addGoToStrategyToBuilder( int unitId, WsCoordinate destination) {
        standingComponent.getUnits().get(unitId).getStrategies().add(new GoToStrategy(apolloClientService, destination));
    }

    public void changeStrategiesOfRobot(int unitId, List<Strategy> strategies) {
        standingComponent.getUnits().get(unitId).setStrategies(strategies);
    }
}
