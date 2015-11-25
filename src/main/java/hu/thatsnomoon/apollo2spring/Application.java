package hu.thatsnomoon.apollo2spring;

/**
 *
 * @author David
 */
import hu.thatsnomoon.apollo2spring.configuration.ApolloConfiguration;
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Application {

    private static final Logger logger = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        logger.debug(Arrays.toString(args));

        ApolloConfiguration.serverUrl = args[0];
        ApolloConfiguration.user = args[1];
        ApolloConfiguration.password = args[2];
        SpringApplication.run(Application.class);
    }
}
