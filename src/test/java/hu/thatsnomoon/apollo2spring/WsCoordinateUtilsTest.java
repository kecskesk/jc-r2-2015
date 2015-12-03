package hu.thatsnomoon.apollo2spring;

import hu.thatsnomoon.apollo2spring.utils.WsCoordinateUtils;
import eu.loxon.centralcontrol.CommonResp;
import eu.loxon.centralcontrol.ObjectType;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;
import hu.thatsnomoon.apollo2spring.model.Coordinate;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author NB57
 */
public class WsCoordinateUtilsTest {

    public WsCoordinateUtilsTest() {
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

    /**
     * Test of print method, of class WsCoordinateUtils.
     */
    @Ignore
    @org.junit.Test
    public void testPrint() {
        System.out.println("print");
        CommonResp response = null;
        WsCoordinateUtils.print(response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSameCells method, of class WsCoordinateUtils.
     */
    @org.junit.Test
    public void testIsSameCells() {
        WsCoordinate first = new WsCoordinate();
        first.setX(1);
        first.setY(1);
        WsCoordinate second = new WsCoordinate();
        second.setX(1);
        second.setY(-1);
        assertTrue(!WsCoordinateUtils.isSameCells(first, second));
        
        
        WsCoordinate third = new WsCoordinate();
        third.setX(1);
        third.setY(1);
        assertTrue(WsCoordinateUtils.isSameCells(first, third));
    }

    /**
     * Test of isNeighborCells method, of class WsCoordinateUtils.
     */
    @org.junit.Test
    public void testIsNeighborCells() {
        WsCoordinate first = new WsCoordinate();
        first.setX(1);
        first.setY(1);
        WsCoordinate second = new WsCoordinate();
        second.setX(1);
        second.setY(1);
        assertTrue(!WsCoordinateUtils.isNeighborCells(first, second));
        
        WsCoordinate from = new WsCoordinate();
        from.setX(1);
        from.setY(1);
        WsCoordinate to1 = new WsCoordinate();
        to1.setX(0);
        to1.setY(1);
        assertTrue(WsCoordinateUtils.isNeighborCells(from, to1));
        
        WsCoordinate to2 = new WsCoordinate();
        to2.setX(2);
        to2.setY(1);
        assertTrue(WsCoordinateUtils.isNeighborCells(from, to2));
        
        WsCoordinate to3 = new WsCoordinate();
        to3.setX(1);
        to3.setY(0);
        assertTrue(WsCoordinateUtils.isNeighborCells(from, to3));
        
        WsCoordinate to4 = new WsCoordinate();
        to4.setX(1);
        to4.setY(2);
        assertTrue(WsCoordinateUtils.isNeighborCells(from, to4));
    }

    /**
     * Test of moveDirection method, of class WsCoordinateUtils.
     */
    @org.junit.Test(expected = RuntimeException.class)
    public void testMoveDirection() {
        
        WsCoordinate from = new WsCoordinate();
        from.setX(1);
        from.setY(1);
        WsCoordinate to1 = new WsCoordinate();
        to1.setX(0);
        to1.setY(1);
        assertTrue(WsDirection.LEFT.equals(WsCoordinateUtils.moveDirection(from, to1)));
        
        WsCoordinate to2 = new WsCoordinate();
        to2.setX(2);
        to2.setY(1);
        assertTrue(WsDirection.RIGHT.equals(WsCoordinateUtils.moveDirection(from, to2)));
        
        WsCoordinate to3 = new WsCoordinate();
        to3.setX(1);
        to3.setY(0);
        assertTrue(WsDirection.DOWN.equals(WsCoordinateUtils.moveDirection(from, to3)));
        
        WsCoordinate to4 = new WsCoordinate();
        to4.setX(1);
        to4.setY(2);
        assertTrue(WsDirection.UP.equals(WsCoordinateUtils.moveDirection(from, to4)));
                
        WsCoordinate first = new WsCoordinate();
        first.setX(1);
        first.setY(1);
        WsCoordinate second = new WsCoordinate();
        second.setX(1);
        second.setY(1);
        WsCoordinateUtils.moveDirection(first, second);
    }

    /**
     * Test of neighbourCellCoordinate method, of class WsCoordinateUtils.
     */
    @org.junit.Test
    public void testNeighbourCellCoordinate() {
        WsCoordinate from = new WsCoordinate();
        from.setX(1);
        from.setY(1);
        
        
        WsCoordinate to1 = new WsCoordinate();
        to1.setX(0);
        to1.setY(1);        
        assertEquals(new Coordinate(WsCoordinateUtils.directionToCoordinate(from, WsDirection.LEFT)),new Coordinate(to1));
        
        WsCoordinate to2 = new WsCoordinate();
        to2.setX(2);
        to2.setY(1);
        assertEquals(new Coordinate(WsCoordinateUtils.directionToCoordinate(from, WsDirection.RIGHT)),new Coordinate(to2));
        
        WsCoordinate to3 = new WsCoordinate();
        to3.setX(1);
        to3.setY(0);
        assertEquals(new Coordinate(WsCoordinateUtils.directionToCoordinate(from, WsDirection.DOWN)),new Coordinate(to3));
        
        WsCoordinate to4 = new WsCoordinate();
        to4.setX(1);
        to4.setY(2);
        assertEquals(new Coordinate(WsCoordinateUtils.directionToCoordinate(from, WsDirection.UP)),new Coordinate(to4));
    }

    /**
     * Test of getDirectionScoutingFromWatch method, of class WsCoordinateUtils.
     */
    @org.junit.Test
    public void testGetDirectionScoutingFromWatch() {
        System.out.println("getDirectionScoutingFromWatch");
        List<Scouting> scoutings = new ArrayList<>();

        Scouting s1 = new Scouting();
        s1.setObject(ObjectType.ROCK);
        WsCoordinate p1 = new WsCoordinate();
        p1.setX(4);
        p1.setY(2);
        s1.setCord(p1);

        Scouting s2 = new Scouting();
        s2.setObject(ObjectType.GRANITE);
        WsCoordinate p2 = new WsCoordinate();
        p2.setX(5);
        p2.setY(3);
        s2.setCord(p2);

        Scouting s3 = new Scouting();
        s3.setObject(ObjectType.TUNNEL);
        WsCoordinate p3 = new WsCoordinate();
        p3.setX(6);
        p3.setY(2);
        s3.setCord(p3);

        Scouting s4 = new Scouting();
        s4.setObject(ObjectType.OBSIDIAN);
        WsCoordinate p4 = new WsCoordinate();
        p4.setX(5);
        p4.setY(1);
        s4.setCord(p4);

        scoutings.add(s1);
        scoutings.add(s2);
        scoutings.add(s3);
        scoutings.add(s4);

        Scouting result;
        
        WsCoordinate pos = new WsCoordinate();
        pos.setX(5);
        pos.setY(2);

        result = WsCoordinateUtils.getDirectionScoutingFromWatch(scoutings, pos, WsDirection.UP);
        assertEquals(ObjectType.GRANITE, result.getObject());
        
        result = WsCoordinateUtils.getDirectionScoutingFromWatch(scoutings, pos, WsDirection.DOWN);
        assertEquals(ObjectType.OBSIDIAN, result.getObject());
        
        result = WsCoordinateUtils.getDirectionScoutingFromWatch(scoutings, pos, WsDirection.LEFT);
        assertEquals(ObjectType.ROCK, result.getObject());
        
        result = WsCoordinateUtils.getDirectionScoutingFromWatch(scoutings, pos, WsDirection.RIGHT);
        assertEquals(ObjectType.TUNNEL, result.getObject());
    }

}
