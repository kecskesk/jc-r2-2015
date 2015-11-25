package hu.thatsnomoon.apollo2spring.component;

import eu.loxon.centralcontrol.ActionCostResponse;
import eu.loxon.centralcontrol.ExplodeCellRequest;
import eu.loxon.centralcontrol.ExplodeCellResponse;
import eu.loxon.centralcontrol.GetSpaceShuttleExitPosResponse;
import eu.loxon.centralcontrol.GetSpaceShuttlePosResponse;
import eu.loxon.centralcontrol.IsMyTurnResponse;
import eu.loxon.centralcontrol.MoveBuilderUnitRequest;
import eu.loxon.centralcontrol.MoveBuilderUnitResponse;
import eu.loxon.centralcontrol.ObjectType;
import eu.loxon.centralcontrol.RadarResponse;
import eu.loxon.centralcontrol.ResultType;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.StartGameResponse;
import eu.loxon.centralcontrol.StructureTunnelRequest;
import eu.loxon.centralcontrol.StructureTunnelResponse;
import eu.loxon.centralcontrol.WatchResponse;
import eu.loxon.centralcontrol.WsBuilderunit;
import eu.loxon.centralcontrol.WsCoordinate;
import hu.thatsnomoon.apollo2spring.configuration.ApolloConfiguration;
import hu.thatsnomoon.apollo2spring.model.BuilderUnit;
import hu.thatsnomoon.apollo2spring.service.ApolloClientService;
import hu.thatsnomoon.apollo2spring.strategy.DefaultStrategy;
import hu.thatsnomoon.apollo2spring.utils.WsCoordinateUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author David
 */
@Component
public class ApolloStandingComponent {

    private static final String ACTION_COST_NAME = "actionCosts";
    private static final String COMMON_RESP_NAME = "commonResponse";
    private static final String IS_MY_TURN_NAME = "isMyTurn";
    private static final String MAP_NAME = "map";
    private static final String SHUTTLE_EXIT_NAME = "shuttleExitResponse";
    private static final String UNITS_NAME = "units";

    private final List<List<Scouting>> map;
    private final Map<String, Object> standing;
    private final Map<Integer, BuilderUnit> units;

    public ApolloStandingComponent() {
        this.map = Collections.synchronizedList(new ArrayList<List<Scouting>>());
        this.standing = Collections.synchronizedMap(new HashMap<>());
        this.units = Collections.synchronizedMap(new HashMap<Integer, BuilderUnit>());
        this.standing.put(COMMON_RESP_NAME, null);
        this.standing.put(MAP_NAME, Collections.unmodifiableList(map));
        this.standing.put(UNITS_NAME, Collections.unmodifiableMap(units));
    }

    private void refreshMap(Scouting scouting) {

        this.map.get(scouting.getCord().getX()).set(scouting.getCord().getY(), scouting);
    }

    private Scouting newScouting(WsCoordinate coord, ObjectType type, String team) {
        Scouting s = new Scouting();
        s.setCord(coord);
        s.setObject(type);
        s.setTeam(team);
        return s;
    }

    public void initMap(StartGameResponse response) {
        this.standing.put(COMMON_RESP_NAME, response.getResult());

        if (response.getResult().getType() == ResultType.DONE) {

            for (int x = 0; x < response.getSize().getX(); x++) {
                map.add(Collections.synchronizedList(new ArrayList<Scouting>()));
                for (int y = 0; y < response.getSize().getY(); y++) {
                    map.get(x).set(y, new Scouting());
                }
            }
        }
    }

    public void initUnits(StartGameResponse response, ApolloClientService apolloClient) {
        for (WsBuilderunit bu : response.getUnits()) {
            this.units.put(bu.getUnitid(), new BuilderUnit(bu.getCord(), bu.getUnitid(), new DefaultStrategy(apolloClient, WsCoordinateUtils.UP_ORDER[bu.getUnitid() % 4])));
        }
    }

    public void refreshStanding(ActionCostResponse response) {
        this.standing.put(COMMON_RESP_NAME, response.getResult());

        if (response.getResult().getType() == ResultType.DONE) {

            // Must clone, cause I want to set the commonresp field to null
            ActionCostResponse responseClone = new ActionCostResponse();
            responseClone.setAvailableActionPoints(response.getAvailableActionPoints());
            responseClone.setAvailableExplosives(response.getAvailableExplosives());
            responseClone.setDrill(response.getDrill());
            responseClone.setExplode(response.getExplode());
            responseClone.setMove(response.getMove());
            responseClone.setRadar(response.getRadar());
            responseClone.setWatch(response.getWatch());

            this.standing.put(ACTION_COST_NAME, responseClone);
        }
    }

    public void refreshStanding(ExplodeCellRequest request, ExplodeCellResponse response) {
        this.standing.put(COMMON_RESP_NAME, response.getResult());

        if (response.getResult().getType() == ResultType.DONE) {
            WsCoordinate robotCoord = this.units.get(request.getUnit()).getPosition();
            WsCoordinate explodedCoord = WsCoordinateUtils.directionToCoordinate(robotCoord, request.getDirection());
            Scouting exploded = newScouting(explodedCoord, ObjectType.ROCK, null);
            this.refreshMap(exploded);
        }
    }

    public void refreshStanding(GetSpaceShuttleExitPosResponse response) {
        this.standing.put(COMMON_RESP_NAME, response.getResult());

        if (response.getResult().getType() == ResultType.DONE) {
            this.standing.put(SHUTTLE_EXIT_NAME, response.getCord());
        }
    }

    public void refreshStanding(GetSpaceShuttlePosResponse response) {
        this.standing.put(COMMON_RESP_NAME, response.getResult());

        if (response.getResult().getType() == ResultType.DONE) {
            Scouting shuttle = new Scouting();

            shuttle.setCord(response.getCord());
            shuttle.setObject(ObjectType.SHUTTLE);
            shuttle.setTeam(ApolloConfiguration.user);
            this.refreshMap(shuttle);
        }
    }

    public void refreshStanding(IsMyTurnResponse response) {
        this.standing.put(COMMON_RESP_NAME, response.getResult());

        if (response.getResult().getType() == ResultType.DONE) {
            this.standing.put(IS_MY_TURN_NAME, response.isIsYourTurn());
        }
    }

    public void refreshStanding(MoveBuilderUnitRequest request, MoveBuilderUnitResponse response) {
        this.standing.put(COMMON_RESP_NAME, response.getResult());

        if (response.getResult().getType() == ResultType.DONE) {
            WsCoordinate originalCoord = this.units.get(request.getUnit()).getPosition();
            WsCoordinate newCoord = WsCoordinateUtils.directionToCoordinate(originalCoord, request.getDirection());
            Scouting robotCell = newScouting(newCoord, ObjectType.BUILDER_UNIT, ApolloConfiguration.user);
            this.refreshMap(robotCell);
            this.units.get(request.getUnit()).setPosition(newCoord);
        }
    }

    public void refreshStanding(RadarResponse response) {
        this.standing.put(COMMON_RESP_NAME, response.getResult());

        if (response.getResult().getType() == ResultType.DONE) {
            for (Scouting scout : response.getScout()) {
                this.refreshMap(scout);
            }
        }
    }

    public void refreshStanding(StructureTunnelRequest request, StructureTunnelResponse response) {
        this.standing.put(COMMON_RESP_NAME, response.getResult());

        if (response.getResult().getType() == ResultType.DONE) {
            WsCoordinate robotCoord = this.units.get(request.getUnit()).getPosition();
            WsCoordinate structuredCoord = WsCoordinateUtils.directionToCoordinate(robotCoord, request.getDirection());
            Scouting structured = newScouting(structuredCoord, ObjectType.TUNNEL, ApolloConfiguration.user);
            this.refreshMap(structured);
        }
    }

    public void refreshStanding(WatchResponse response) {
        this.standing.put(COMMON_RESP_NAME, response.getResult());

        for (Scouting scout : response.getScout()) {
            this.refreshMap(scout);
        }
    }

    public Map<String, Object> getStanding() {
        return Collections.unmodifiableMap(this.standing);
    }

    public Map<Integer, BuilderUnit> getUnits() {
        return Collections.unmodifiableMap(this.units);
    }

}
