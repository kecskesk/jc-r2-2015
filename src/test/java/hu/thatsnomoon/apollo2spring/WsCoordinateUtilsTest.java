package hu.thatsnomoon.apollo2spring;

import eu.loxon.centralcontrol.CommonResp;
import eu.loxon.centralcontrol.ObjectType;
import eu.loxon.centralcontrol.Scouting;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;
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
    @Ignore
    @org.junit.Test
    public void testIsSameCells() {
        System.out.println("isSameCells");
        WsCoordinate first = null;
        WsCoordinate second = null;
        boolean expResult = false;
        boolean result = WsCoordinateUtils.isSameCells(first, second);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isNeighborCells method, of class WsCoordinateUtils.
     */
    @Ignore
    @org.junit.Test
    public void testIsNeighborCells() {
        System.out.println("isNeighborCells");
        WsCoordinate from = null;
        WsCoordinate to = null;
        boolean expResult = false;
        boolean result = WsCoordinateUtils.isNeighborCells(from, to);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of moveDirection method, of class WsCoordinateUtils.
     */
    @Ignore
    @org.junit.Test
    public void testMoveDirection() {
        System.out.println("moveDirection");
        WsCoordinate from = null;
        WsCoordinate to = null;
        WsDirection expResult = null;
        WsDirection result = WsCoordinateUtils.moveDirection(from, to);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of neighbourCellCoordinate method, of class WsCoordinateUtils.
     */
    @Ignore
    @org.junit.Test
    public void testNeighbourCellCoordinate() {
        System.out.println("neighbourCellCoordinate");
        WsCoordinate pos = null;
        WsDirection dir = null;
        WsCoordinate expResult = null;
        WsCoordinate result = WsCoordinateUtils.neighbourCellCoordinate(pos, dir);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        s3.setObject(ObjectType.ROCK);
        WsCoordinate p3 = new WsCoordinate();
        p3.setX(6);
        p3.setY(2);
        s3.setCord(p3);

        Scouting s4 = new Scouting();
        s4.setObject(ObjectType.ROCK);
        WsCoordinate p4 = new WsCoordinate();
        p4.setX(5);
        p4.setY(1);
        s4.setCord(p4);

        scoutings.add(s1);
        scoutings.add(s2);
        scoutings.add(s3);
        scoutings.add(s4);

        WsCoordinate pos = new WsCoordinate();
        pos.setX(5);
        pos.setY(2);
        WsDirection dir = WsDirection.UP;

        Scouting result = WsCoordinateUtils.getDirectionScoutingFromWatch(scoutings, pos, dir);
        assertEquals(ObjectType.GRANITE, result.getObject());
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

}
