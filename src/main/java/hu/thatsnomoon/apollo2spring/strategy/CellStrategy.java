package hu.thatsnomoon.apollo2spring.strategy;

import hu.thatsnomoon.apollo2spring.service.ApolloClientService;

/**
 * This strategy contains the logic for building a durable cell of tunnels.
 *
 * @author Soma
 */
public class CellStrategy {

    private final ApolloClientService apolloClient;

    public CellStrategy(ApolloClientService apolloClient) {
        this.apolloClient = apolloClient;
    }

}
