package hu.thatsnomoon.apollo2spring.service;

import hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException;
import eu.loxon.centralcontrol.*;
import hu.thatsnomoon.apollo2spring.component.ApolloStandingComponent;
import hu.thatsnomoon.apollo2spring.configuration.ApolloConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

/**
 * SOAP Client for JavaChallenge Round #2
 *
 * @author Dombi Soma
 */
@Service
public class ApolloClientService extends WebServiceGatewaySupport {

    private static final String serverUrl = "http://192.168.1.2:8443/engine/CentralControl?wsdl";

    public static ApolloClientService apolloClient;

    @Autowired
    private ApolloStandingComponent standingComponent;

    public ApolloClientService() {
        apolloClient = this;
    }



    /**
     * Cached responses
     */
    private GetSpaceShuttleExitPosResponse cachedShuttleExitPos = null;
    private GetSpaceShuttlePosResponse cachedShuttlePos = null;



    /**
     * Order builder unit to explode a cell
     *
     * @param unit the ID of the builder unit
     * @param dir the direction to explode cell
     * @return ExplodeCellResponse from server
     * @throws hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException
     */
    public ExplodeCellResponse explodeCell(int unit, WsDirection dir) throws SoapResponseInvalidException {
        ExplodeCellRequest request = new ExplodeCellRequest();

        request.setUnit(unit);
        request.setDirection(dir);

        ExplodeCellResponse response = (ExplodeCellResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(serverUrl));

        if (response.getResult().getType() == ResultType.INVALID) {
            throw new SoapResponseInvalidException(response.getResult());
        }

        this.standingComponent.refreshStanding(request, response);

        return response;
    }

    /**
     * Cost of each action
     *
     * @return ActionCostResponse from server
     * @throws hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException
     */
    public ActionCostResponse getActionCost() throws SoapResponseInvalidException {
        ActionCostRequest request = new ActionCostRequest();

        ActionCostResponse response = (ActionCostResponse) getWebServiceTemplate()
                .marshalSendAndReceive(serverUrl, request, new SoapActionCallback(serverUrl));

        if (response.getResult().getType() == ResultType.INVALID) {
            throw new SoapResponseInvalidException(response.getResult());
        }

        this.standingComponent.refreshStanding(response);

        return response;
    }

    /**
     * Get the position of the space shuttle
     *
     * @return GetSpaceShuttlePosResponse from server
     * @throws hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException
     */
    public GetSpaceShuttlePosResponse getSpaceShuttlePos() throws SoapResponseInvalidException {
        if (this.cachedShuttlePos != null) {
            return cachedShuttlePos;
        }
        GetSpaceShuttlePosRequest request = new GetSpaceShuttlePosRequest();

        GetSpaceShuttlePosResponse response = (GetSpaceShuttlePosResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(serverUrl));

        if (response.getResult().getType() == ResultType.INVALID) {
            throw new SoapResponseInvalidException(response.getResult());
        }

        this.standingComponent.refreshStanding(response);

        this.cachedShuttlePos = response;
        return response;
    }

    /**
     * Get the exit cell position of the space shuttle
     *
     * @return GetSpaceShuttleExitPosResponse from server
     * @throws hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException
     */
    public GetSpaceShuttleExitPosResponse getSpaceShuttleExitPos() throws SoapResponseInvalidException {
        if (this.cachedShuttleExitPos != null) {
            return cachedShuttleExitPos;
        }
        GetSpaceShuttleExitPosRequest request = new GetSpaceShuttleExitPosRequest();

        GetSpaceShuttleExitPosResponse response = (GetSpaceShuttleExitPosResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(serverUrl));

        if (response.getResult().getType() == ResultType.INVALID) {
            throw new SoapResponseInvalidException(response.getResult());
        }

        this.standingComponent.refreshStanding(response);

        this.cachedShuttleExitPos = response;
        return response;
    }

    /**
     * Is my turn
     *
     * @return IsMyTurnResponse from server
     * @throws hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException
     */
    public IsMyTurnResponse isMyTurn() throws SoapResponseInvalidException {
        IsMyTurnRequest request = new IsMyTurnRequest();

        IsMyTurnResponse response = (IsMyTurnResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(serverUrl));

        if (response.getResult().getType() == ResultType.INVALID) {
            throw new SoapResponseInvalidException(response.getResult());
        }

        this.standingComponent.refreshStanding(response);

        return response;
    }

    /**
     * Order builder unit to move
     *
     * @param unit the ID of the builder unit
     * @param dir the direction to move
     * @return MoveBuilderUnitResponse from server
     * @throws hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException
     */
    public MoveBuilderUnitResponse moveBuilderUnit(int unit, WsDirection dir) throws SoapResponseInvalidException {
        MoveBuilderUnitRequest request = new MoveBuilderUnitRequest();

        request.setUnit(unit);
        request.setDirection(dir);

        MoveBuilderUnitResponse response = (MoveBuilderUnitResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(serverUrl));

        if (response.getResult().getType() == ResultType.INVALID) {
            throw new SoapResponseInvalidException(response.getResult());
        }

        this.standingComponent.refreshStanding(request, response);

        return response;
    }

    /**
     * Order builder unit to scan it's surrounding cells
     *
     * @param unit the ID of the builder unit
     * @return RadarResponse from server
     * @throws hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException
     */
    public RadarResponse radar(int unit) throws SoapResponseInvalidException {
        RadarRequest request = new RadarRequest();

        request.setUnit(unit);

        RadarResponse response = (RadarResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(serverUrl));

        if (response.getResult().getType() == ResultType.INVALID) {
            throw new SoapResponseInvalidException(response.getResult());
        }

        this.standingComponent.refreshStanding(response);

        return response;
    }

    /**
     * Start the game
     *
     * @return StartGameResponse from server
     * @throws hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException
     */
    public StartGameResponse startGame() throws SoapResponseInvalidException {
        StartGameRequest request = new StartGameRequest();

        StartGameResponse response = (StartGameResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(serverUrl));

        if (response.getResult().getType() == ResultType.INVALID) {
            throw new SoapResponseInvalidException(response.getResult());
        }

        this.standingComponent.initMap(response);
        this.standingComponent.initUnits(response, this);

        return response;
    }

    /**
     * Order builder unit to structure tunnel
     *
     * @param unit the ID of the builder unit
     * @param dir the direction to structure a tunnel
     * @return StructureTunnelResponse from server
     * @throws hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException
     */
    public StructureTunnelResponse structureTunnel(int unit, WsDirection dir) throws SoapResponseInvalidException {
        StructureTunnelRequest request = new StructureTunnelRequest();

        request.setUnit(unit);
        request.setDirection(dir);

        StructureTunnelResponse response = (StructureTunnelResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(serverUrl));

        if (response.getResult().getType() == ResultType.INVALID) {
            throw new SoapResponseInvalidException(response.getResult());
        }

        this.standingComponent.refreshStanding(request, response);

        return response;
    }

    /**
     * Order builder unit to scan vertically and horizontally one cell
     *
     * @param unit the ID of the builder unit
     * @return WatchResponse from server
     * @throws hu.thatsnomoon.apollo2spring.exception.SoapResponseInvalidException
     */
    public WatchResponse watch(int unit) throws SoapResponseInvalidException {
        WatchRequest request = new WatchRequest();

        request.setUnit(unit);

        WatchResponse response = (WatchResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(serverUrl));

        if (response.getResult().getType() == ResultType.INVALID) {
            throw new SoapResponseInvalidException(response.getResult());
        }

        this.standingComponent.refreshStanding(response);

        return response;
    }
}
