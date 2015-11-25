package hu.thatsnomoon.apollo2spring.configuration;

import hu.thatsnomoon.apollo2spring.service.ApolloClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
public class ApolloConfiguration {

    public static String serverUrl;
    public static String user;
    public static String password;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("eu.loxon.centralcontrol");
        return marshaller;
    }

    @Bean
    public ApolloClientService apolloClient(Jaxb2Marshaller marshaller) {
        ApolloClientService client = new ApolloClientService();
        client.setDefaultUri(serverUrl);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);

        WebServiceTemplate template = client.getWebServiceTemplate();
        template.setMessageSender(new WebServiceMessageSenderWithAuth(user, password));

        return client;
    }
}
