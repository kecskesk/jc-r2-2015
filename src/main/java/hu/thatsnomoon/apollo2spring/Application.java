package hu.thatsnomoon.apollo2spring;

/**
 *
 * @author David
 */
import eu.loxon.centralcontrol.StartGameResponse;
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
            StartGameResponse response = apolloClient.startGame();
        };
    }
}
