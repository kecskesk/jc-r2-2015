package hu.thatsnomoon.apollo2spring;

/**
 *
 * @author David
 */
import eu.loxon.centralcontrol.GetSpaceShuttleExitPosResponse;
import eu.loxon.centralcontrol.GetSpaceShuttlePosResponse;
import eu.loxon.centralcontrol.IsMyTurnResponse;
import eu.loxon.centralcontrol.StartGameResponse;
import eu.loxon.centralcontrol.WsBuilderunit;
import hu.thatsnomoon.apollo2spring.strategy.DefaultStrategy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));

        ApolloClient.centralControlUrl = args[0];
        ApolloConfiguration.serverUrl = args[0];
        ApolloConfiguration.user = args[1];
        ApolloConfiguration.password = args[2];
        SpringApplication.run(Application.class);
    }

    @Bean
    CommandLineRunner lookup(ApolloClient apolloClient) {
        return args -> {

            StartGameResponse startGameResponse = apolloClient.startGame();

            GetSpaceShuttleExitPosResponse exitPosResp = apolloClient.getSpaceShuttleExitPos();

            GetSpaceShuttlePosResponse shuttlePosResp = apolloClient.getSpaceShuttlePos();

            // Initializing the robots
            Map<Integer, BuilderUnit> robots = new HashMap<>();
            for (WsBuilderunit bu : startGameResponse.getUnits()) {
                robots.put(bu.getUnitid(), new BuilderUnit(bu.getCord(), bu.getUnitid(), new DefaultStrategy(apolloClient, WsCoordinateUtils.UP_ORDER[bu.getUnitid() % 4])));
            }

            int lastRobotId = -1;

            while (true) {
                Thread.sleep(300);
                IsMyTurnResponse imtr = apolloClient.isMyTurn();

                if (imtr.getResult().getTurnsLeft() == 0) {
                    break;
                }

                if (imtr.isIsYourTurn() && imtr.getResult().getBuilderUnit() != lastRobotId) {
                    int currentRobot = imtr.getResult().getBuilderUnit();

                    if (imtr.getResult().getTurnsLeft() % 10 == 0) {
                        ((DefaultStrategy)robots.get(currentRobot).getStrategy()).setDefaultDirection(WsCoordinateUtils.UP_ORDER[(currentRobot + imtr.getResult().getTurnsLeft() / 10) % 4 ]);
                    }
                    // If it is our turn, query the current robotID and
                    // order the robot to step
                    BuilderUnit builderUnit = robots.get(currentRobot);
                    builderUnit.step();
                    lastRobotId = currentRobot;
                }
            }
        };
    }
}
