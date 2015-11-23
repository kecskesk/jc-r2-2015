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
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
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
                robots.put(bu.getUnitid(), new BuilderUnit(bu.getCord(), bu.getUnitid(), new DefaultSrategy()));
            }

            while (true) {
                IsMyTurnResponse imtr = apolloClient.isMyTurn();
                Thread.sleep(300);

                if (imtr.isIsYourTurn()) {
                    // If it is our turn, query the current robotID and
                    // order the robot to step
                    int currentRobot = imtr.getResult().getBuilderUnit();
                    BuilderUnit builderUnit = robots.get(currentRobot);
                    builderUnit.step();
                }
            }
        };
    }
}
