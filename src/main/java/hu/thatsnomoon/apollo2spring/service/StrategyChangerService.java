package hu.thatsnomoon.apollo2spring.service;

import com.google.common.collect.Lists;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;
import hu.thatsnomoon.apollo2spring.component.ApolloStandingComponent;
import hu.thatsnomoon.apollo2spring.model.BuilderUnit;
import hu.thatsnomoon.apollo2spring.strategy.CellStrategy;
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

    private final ApolloClientService apolloClientService = ApolloClientService.apolloClient;

    public void changeToDefaultStrategyOfRobot( int unitId, WsDirection direction) {
        standingComponent.getUnits().get(unitId).setStrategies( Lists.newArrayList(new DefaultStrategy(apolloClientService, direction)));
    }

    public void changeToGoToStrategyOfBuilder( int unitId, WsCoordinate destination) {
        standingComponent.getUnits().get(unitId).setStrategies( Lists.newArrayList(new GoToStrategy(apolloClientService, destination)));
        this.addDefaultStrategyToRobot(unitId, WsDirection.UP);
    }

    public void changeToCellStrategyOfRobot(int unitId, WsDirection direction) {
        BuilderUnit builder = standingComponent.getUnits().get(unitId);
        builder.setStrategies(Lists.newArrayList(new CellStrategy(apolloClientService, direction, builder.getPosition())));
        this.addDefaultStrategyToRobot(unitId, direction);
    }

    public void addDefaultStrategyToRobot( int unitId, WsDirection direction) {
        standingComponent.getUnits().get(unitId).getStrategies().add(new DefaultStrategy(apolloClientService, direction));
    }

    public void addGoToStrategyToBuilder( int unitId, WsCoordinate destination) {
        standingComponent.getUnits().get(unitId).getStrategies().add(new GoToStrategy(apolloClientService, destination));
        this.addDefaultStrategyToRobot(unitId, WsDirection.UP);
    }

    public void addCellStrategyToBuilder(int unitId, WsDirection direction) {
        BuilderUnit builder = standingComponent.getUnits().get(unitId);
        builder.getStrategies().add(new CellStrategy(apolloClientService, direction, builder.getPosition()));
        this.addDefaultStrategyToRobot(unitId, direction);
    }

    public void changeStrategiesOfRobot(int unitId, List<Strategy> strategies) {
        standingComponent.getUnits().get(unitId).setStrategies(strategies);
    }
}
