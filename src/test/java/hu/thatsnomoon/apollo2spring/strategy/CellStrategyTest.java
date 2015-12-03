package hu.thatsnomoon.apollo2spring.strategy;

import eu.loxon.centralcontrol.WsCoordinate;
import hu.thatsnomoon.apollo2spring.model.BuilderUnit;
import hu.thatsnomoon.apollo2spring.model.Coordinate;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author NB57
 */
public class CellStrategyTest {

    public CellStrategyTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGenerating() {
        HashMap<Coordinate, Coordinate> routeTable = new HashMap<>();
        ///////////////////////////////////////////////////
        // 6,10 - 7,10
        WsCoordinate wc1 = new WsCoordinate();
        wc1.setX(6);
        wc1.setY(10);
        Coordinate c1 = new Coordinate(wc1);

        WsCoordinate wc2 = new WsCoordinate();
        wc2.setX(7);
        wc2.setY(10);
        Coordinate c2 = new Coordinate(wc2);
        ///////////////////////////////////////////////////
        // 8,13 - 8,12
        WsCoordinate wc3 = new WsCoordinate();
        wc3.setX(8);
        wc3.setY(13);
        Coordinate c3 = new Coordinate(wc3);

        WsCoordinate wc4 = new WsCoordinate();
        wc4.setX(8);
        wc4.setY(12);
        Coordinate c4 = new Coordinate(wc4);
        ///////////////////////////////////////////////////
        // 7,12 - 6,12
        WsCoordinate wc5 = new WsCoordinate();
        wc5.setX(7);
        wc5.setY(12);
        Coordinate c5 = new Coordinate(wc5);

        WsCoordinate wc6 = new WsCoordinate();
        wc6.setX(6);
        wc6.setY(12);
        Coordinate c6 = new Coordinate(wc6);
        ///////////////////////////////////////////////////
        // 9,13 - 9,14
        WsCoordinate wc7 = new WsCoordinate();
        wc7.setX(9);
        wc7.setY(13);
        Coordinate c7 = new Coordinate(wc7);

        WsCoordinate wc8 = new WsCoordinate();
        wc8.setX(9);
        wc8.setY(14);
        Coordinate c8 = new Coordinate(wc8);
        ///////////////////////////////////////////////////
        // 9,14 - 8,14
        WsCoordinate wc9 = new WsCoordinate();
        wc9.setX(9);
        wc9.setY(14);
        Coordinate c9 = new Coordinate(wc9);

        WsCoordinate wc10 = new WsCoordinate();
        wc10.setX(8);
        wc10.setY(14);
        Coordinate c10 = new Coordinate(wc10);
        ///////////////////////////////////////////////////

        // Loading route table
        WsCoordinate robotPos = new WsCoordinate();
        robotPos.setX(5);
        robotPos.setY(10);

        routeTable = CellStrategy.createRouteTable(new Coordinate(robotPos), 30, 30);

        assertEquals(c2, routeTable.get(c1));
        assertEquals(c4, routeTable.get(c3));
        assertEquals(c6, routeTable.get(c5));
        assertEquals(c8, routeTable.get(c7));
        assertEquals(c10, routeTable.get(c9));
    }

    @Test
    public void testLocaleToWorld() {
        WsCoordinate local = new WsCoordinate();
        local.setX(4);
        local.setY(7);

        WsCoordinate transition = new WsCoordinate();
        transition.setX(3);
        transition.setY(12);

        WsCoordinate world = CellStrategy.localeToWorld(local, transition);
        assertEquals(local.getX() + transition.getX(), world.getX());
        assertEquals(local.getY() + transition.getY(), world.getY());

    }

    @Test
    public void testRotation() {
        WsCoordinate pos = new WsCoordinate();
        pos.setX(1);
        pos.setY(3);

        assertEquals(-pos.getY(), CellStrategy.rotate(pos, 90).getX());
        assertEquals(pos.getX(), CellStrategy.rotate(pos, 90).getY());
    }
}
