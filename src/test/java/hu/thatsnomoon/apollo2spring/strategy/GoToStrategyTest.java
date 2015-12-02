package hu.thatsnomoon.apollo2spring.strategy;

import com.google.common.collect.Lists;
import eu.loxon.centralcontrol.WsCoordinate;
import eu.loxon.centralcontrol.WsDirection;
import hu.thatsnomoon.apollo2spring.model.BuilderUnit;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author David
 */
public class GoToStrategyTest {

    private GoToStrategy goToCenterStrategy;

    public GoToStrategyTest() {
    }

    /**
     * Test of step method, of class GoToStrategy.
     */
    @Before
    public void before() {
        // Set destination to origo
        WsCoordinate destination = new WsCoordinate();
        destination.setX(0);
        destination.setY(0);

        this.goToCenterStrategy = new GoToStrategy(null, destination);
    }

    /**
     * Test of sortMoveDirections method, of class GoToStrategy.
     */
    @Test
    public void testSortMoveDirections() {
        BuilderUnit unit = new BuilderUnit(null, 0, null);

        // Act 1.
        // In the same row, the destination is at the left from the unit
        // Arrange
        WsCoordinate coord1 = new WsCoordinate();
        coord1.setX(5);
        coord1.setY(0);
        unit.setPosition(coord1);

        List<WsDirection> expectedDirections1 = Lists.newArrayList(WsDirection.LEFT, WsDirection.UP, WsDirection.DOWN, WsDirection.RIGHT);

        // Act
        List<WsDirection> actualDirections1 = this.goToCenterStrategy.sortMoveDirections(unit);

        // Assert
        assertArrayEquals(expectedDirections1.toArray(), actualDirections1.toArray());


        // Act 2.
        // In the same coloumn, the destination is above from the unit
        // Arrange
        WsCoordinate coord2 = new WsCoordinate();
        coord2.setX(0);
        coord2.setY(-5);
        unit.setPosition(coord2);

        List<WsDirection> expectedDirections2 = Lists.newArrayList(WsDirection.UP, WsDirection.RIGHT, WsDirection.LEFT, WsDirection.DOWN);

        // Act
        List<WsDirection> actualDirections2 = this.goToCenterStrategy.sortMoveDirections(unit);

        // Assert
        assertArrayEquals(expectedDirections2.toArray(), actualDirections2.toArray());


        // Act 3.
        // The destination is right above from the unit
        // Arrange
        WsCoordinate coord3 = new WsCoordinate();
        coord3.setX(-5);
        coord3.setY(-5);
        unit.setPosition(coord3);

        List<WsDirection> expectedDirections3 = Lists.newArrayList(WsDirection.RIGHT, WsDirection.UP, WsDirection.DOWN, WsDirection.LEFT);

        // Act
        List<WsDirection> actualDirections3 = this.goToCenterStrategy.sortMoveDirections(unit);

        // Assert
        assertArrayEquals(expectedDirections3.toArray(), actualDirections3.toArray());


        // Act 3.
        // The destination is right under from the unit
        // Arrange
        WsCoordinate coord4 = new WsCoordinate();
        coord4.setX(-5);
        coord4.setY(5);
        unit.setPosition(coord4);

        List<WsDirection> expectedDirections4 = Lists.newArrayList(WsDirection.RIGHT, WsDirection.DOWN, WsDirection.UP, WsDirection.LEFT);

        // Act
        List<WsDirection> actualDirections4 = this.goToCenterStrategy.sortMoveDirections(unit);

        // Assert
        assertArrayEquals(expectedDirections4.toArray(), actualDirections4.toArray());
    }

    /**
     * Test of shouldAvoid method, of class GoToStrategy.
     */
    @Test
    public void testShouldAvoid() {
    }

    /**
     * Test of isEnded method, of class GoToStrategy.
     */
    @Test
    public void testIsEnded() {
    }

}
