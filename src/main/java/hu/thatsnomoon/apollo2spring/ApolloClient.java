package hu.thatsnomoon.apollo2spring;

import eu.loxon.centralcontrol.*;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

/**
 * SOAP Client for JavaChallenge Round #2
 *
 * @author Dombi Soma
 */
public class ApolloClient extends WebServiceGatewaySupport {

    /**
     * The server URL
     */
    private final String centralControlUrl = "http://javachallenge.loxon.hu:8443/engine/CentralControl?wsdl";

    /**
     * Order builder unit to explode a cell
     *
     * @param unit the ID of the builder unit
     * @param dir the direction to explode cell
     * @return ExplodeCellResponse from server
     */
    public ExplodeCellResponse explodeCell(int unit, WsDirection dir) {
        ExplodeCellRequest request = new ExplodeCellRequest();

        request.setUnit(unit);
        request.setDirection(dir);

        ExplodeCellResponse response = (ExplodeCellResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(centralControlUrl));

        return response;
    }

    /**
     * Cost of each action
     *
     * @return ActionCostResponse from server
     */
    public ActionCostResponse getActionCost() {
        ActionCostRequest request = new ActionCostRequest();

        ActionCostResponse response = (ActionCostResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(centralControlUrl));

        return response;
    }

    /**
     * Get the position of the space shuttle
     *
     * @return GetSpaceShuttlePosResponse from server
     */
    public GetSpaceShuttlePosResponse getSpaceShuttlePos() {
        GetSpaceShuttlePosRequest request = new GetSpaceShuttlePosRequest();

        GetSpaceShuttlePosResponse response = (GetSpaceShuttlePosResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(centralControlUrl));

        return response;
    }

    /**
     * Get the exit cell position of the space shuttle
     *
     * @return GetSpaceShuttleExitPosResponse from server
     */
    public GetSpaceShuttleExitPosResponse getSpaceShuttleExitPos() {
        GetSpaceShuttleExitPosRequest request = new GetSpaceShuttleExitPosRequest();

        GetSpaceShuttleExitPosResponse response = (GetSpaceShuttleExitPosResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(centralControlUrl));

        return response;
    }

    /**
     * Is my turn
     *
     * @return IsMyTurnResponse from server
     */
    public IsMyTurnResponse isMyTurn() {
        IsMyTurnRequest request = new IsMyTurnRequest();

        IsMyTurnResponse response = (IsMyTurnResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(centralControlUrl));

        return response;
    }

    /**
     * Order builder unit to move
     *
     * @param unit the ID of the builder unit
     * @param dir the direction to move
     * @return MoveBuilderUnitResponse from server
     */
    public MoveBuilderUnitResponse moveBuilderUnit(int unit, WsDirection dir) {
        MoveBuilderUnitRequest request = new MoveBuilderUnitRequest();

        request.setUnit(unit);
        request.setDirection(dir);

        MoveBuilderUnitResponse response = (MoveBuilderUnitResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(centralControlUrl));

        return response;
    }

    /**
     * Order builder unit to scan it's surrounding cells
     *
     * @param unit the ID of the builder unit
     * @return RadarResponse from server
     */
    public RadarResponse radar(int unit) {
        RadarRequest request = new RadarRequest();

        request.setUnit(unit);

        RadarResponse response = (RadarResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(centralControlUrl));

        return response;
    }

    /**
     * Start the game
     *
     * @return StartGameResponse from server
     */
    public StartGameResponse startGame() {
        StartGameRequest request = new StartGameRequest();

        StartGameResponse response = (StartGameResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(centralControlUrl));

        return response;
    }

    /**
     * Order builder unit to structure tunnel
     *
     * @param unit the ID of the builder unit
     * @param dir the direction to structure a tunnel
     * @return StructureTunnelResponse from server
     */
    public StructureTunnelResponse structureTunnel(int unit, WsDirection dir) {
        StructureTunnelRequest request = new StructureTunnelRequest();

        request.setUnit(unit);
        request.setDirection(dir);

        StructureTunnelResponse response = (StructureTunnelResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(centralControlUrl));

        return response;
    }

    /**
     * Order builder unit to scan vertically and horizontally one cell
     *
     * @param unit the ID of the builder unit
     * @return WatchResponse from server
     */
    public WatchResponse watch(int unit) {
        WatchRequest request = new WatchRequest();

        request.setUnit(unit);

        WatchResponse response = (WatchResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request, new SoapActionCallback(centralControlUrl));

        return response;
    }
}
