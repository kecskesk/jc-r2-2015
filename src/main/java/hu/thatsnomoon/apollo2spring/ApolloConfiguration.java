package hu.thatsnomoon.apollo2spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
public class ApolloConfiguration {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("eu.loxon.centralcontrol");
        return marshaller;
    }

    @Bean
    public ApolloClient apolloClient(Jaxb2Marshaller marshaller) {
        ApolloClient client = new ApolloClient();
        client.setDefaultUri(System.getProperty("serverUrl"));
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);

        WebServiceTemplate template = client.getWebServiceTemplate();
        template.setMessageSender(new WebServiceMessageSenderWithAuth());

        return client;
    }
}
