/**
 * CentralControlServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package eu.loxon.www.centralcontrol;


/*
 *  CentralControlServiceService java interface
 */
public interface CentralControlServiceService {
    /**
     * Auto generated method signature
     *
     * @param explodeCellRequest
     */
    public eu.loxon.www.centralcontrol.ExplodeCellResponse explodeCell(
        eu.loxon.www.centralcontrol.ExplodeCellRequest explodeCellRequest)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     *
     * @param isMyTurnRequest
     */
    public eu.loxon.www.centralcontrol.IsMyTurnResponse isMyTurn(
        eu.loxon.www.centralcontrol.IsMyTurnRequest isMyTurnRequest)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     *
     * @param moveBuilderUnitRequest
     */
    public eu.loxon.www.centralcontrol.MoveBuilderUnitResponse moveBuilderUnit(
        eu.loxon.www.centralcontrol.MoveBuilderUnitRequest moveBuilderUnitRequest)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     *
     * @param getSpaceShuttleExitPosRequest
     */
    public eu.loxon.www.centralcontrol.GetSpaceShuttleExitPosResponse getSpaceShuttleExitPos(
        eu.loxon.www.centralcontrol.GetSpaceShuttleExitPosRequest getSpaceShuttleExitPosRequest)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     *
     * @param startGameRequest
     */
    public eu.loxon.www.centralcontrol.StartGameResponse startGame(
        eu.loxon.www.centralcontrol.StartGameRequest startGameRequest)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     *
     * @param structureTunnelRequest
     */
    public eu.loxon.www.centralcontrol.StructureTunnelResponse structureTunnel(
        eu.loxon.www.centralcontrol.StructureTunnelRequest structureTunnelRequest)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     *
     * @param watchRequest
     */
    public eu.loxon.www.centralcontrol.WatchResponse watch(
        eu.loxon.www.centralcontrol.WatchRequest watchRequest)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     *
     * @param getSpaceShuttlePosRequest
     */
    public eu.loxon.www.centralcontrol.GetSpaceShuttlePosResponse getSpaceShuttlePos(
        eu.loxon.www.centralcontrol.GetSpaceShuttlePosRequest getSpaceShuttlePosRequest)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     *
     * @param radarRequest
     */
    public eu.loxon.www.centralcontrol.RadarResponse radar(
        eu.loxon.www.centralcontrol.RadarRequest radarRequest)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     *
     * @param actionCostRequest
     */
    public eu.loxon.www.centralcontrol.ActionCostResponse getActionCost(
        eu.loxon.www.centralcontrol.ActionCostRequest actionCostRequest)
        throws java.rmi.RemoteException;

    //
}
