package hu.thatsnomoon.apollo2spring.model;

import eu.loxon.centralcontrol.WsCoordinate;
import java.util.HashSet;
import java.util.Set;

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
public class CoordinateTest {

    public CoordinateTest() {
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
     * Test of getWsCoordinate method, of class Coordinate.
     */
    @Ignore
    @Test
    public void testGetWsCoordinate() {
        System.out.println("getWsCoordinate");
        Coordinate instance = new Coordinate();
        WsCoordinate expResult = null;
        WsCoordinate result = instance.getWsCoordinate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class Coordinate.
     */
    @Ignore
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Coordinate instance = new Coordinate();
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class Coordinate.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        WsCoordinate wc1 = new WsCoordinate();
        wc1.setX(8);
        wc1.setY(64);

        WsCoordinate wc2 = new WsCoordinate();
        wc2.setX(8);
        wc2.setY(64);

        WsCoordinate wc3 = new WsCoordinate();
        wc3.setX(64);
        wc3.setY(8);

        Set<Coordinate> awesomeSet = new HashSet<>();

        awesomeSet.add(new Coordinate(wc1));
        boolean contains = awesomeSet.contains(new Coordinate(wc2));
        boolean notContains = awesomeSet.contains(new Coordinate(wc3));
        assertEquals(true, contains);
        assertEquals(false, notContains);

    }

}
