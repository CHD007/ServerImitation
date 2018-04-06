package objects;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class LineTest {

    @Test
    public void shouldCreateLineByTwoPoints() {
        Line lineByTwoPoints = Line.getLineByTwoPoints(new FieldObject(1.0, 1.0), new FieldObject(4.0, 2.0));
        assertEquals(1.0, lineByTwoPoints.getArgumentNearX(), 0.0);
        assertEquals(-3.0, lineByTwoPoints.getArgumentNearY(), 0.0);
        assertEquals(2.0, lineByTwoPoints.getFreeArgument(), 0.0);
    }

    @Test
    public void shouldCreateLineByAbscissa() {
        Line lineByAbscissa = Line.getLineByAbscissa(5.4);
        assertEquals(1.0, lineByAbscissa.getArgumentNearX(), 0.0);
        assertEquals(0.0, lineByAbscissa.getArgumentNearY(), 0.0);
        assertEquals(-5.4, lineByAbscissa.getFreeArgument(), 0.0);
    }

    @Test
    public void shouldCreateLineByOrdinate() {
        Line lineByOrdinate = Line.getLineByOrdinate(5.7);
        assertEquals(0.0, lineByOrdinate.getArgumentNearX(), 0.0);
        assertEquals(1.0, lineByOrdinate.getArgumentNearY(), 0.0);
        assertEquals(-5.7, lineByOrdinate.getFreeArgument(), 0.0);
    }

    @Test
    public void shouldFindInterceptionPoint_forTwoLinesCreatedByTwoPoints() {
        Line firstLine = Line.getLineByTwoPoints(new FieldObject(-14.0, 0.0), new FieldObject(-5.0, 1.0));
        Line secondLine = Line.getLineByTwoPoints(new FieldObject(2.0, -3.0), new FieldObject(0.0, -8.0));
        FieldObject intersectionPointWithLine = firstLine.getIntersectionPointWithLine(secondLine).get();
        assertEquals(4.0, intersectionPointWithLine.getPosX(), 0.0);
        assertEquals(2.0, intersectionPointWithLine.getPosY(), 0.0);
    }

    @Test
    public void shouldFindInterceptionPoint_forLinesCreatedByTowPointsAndByAbscissa() {
        Line firstLine = Line.getLineByTwoPoints(new FieldObject(-14.0, 0.0), new FieldObject(-5.0, 1.0));
        Line lineByOrdinate = Line.getLineByOrdinate(5.0);
        FieldObject intersectionPointWithLine = firstLine.getIntersectionPointWithLine(lineByOrdinate).get();
        assertEquals(31.0, intersectionPointWithLine.getPosX(), 0.0);
        assertEquals(5.0, intersectionPointWithLine.getPosY(), 0.0);
    }

    @Test
    public void shouldFindInterceptionPoint_forLinesCreatedByOrdinateAndAbscissa() {
        Line lineByAbscissa = Line.getLineByAbscissa(3.4);
        Line lineByOrdinate = Line.getLineByOrdinate(-7.3);
        FieldObject intersectionPointWithLine = lineByAbscissa.getIntersectionPointWithLine(lineByOrdinate).get();
        FieldObject intersectionPointWithLine1 = lineByOrdinate.getIntersectionPointWithLine(lineByAbscissa).get();
        assertEquals(intersectionPointWithLine, intersectionPointWithLine1);
        assertEquals(3.4, intersectionPointWithLine.getPosX(), 0.0);
        assertEquals(-7.3, intersectionPointWithLine.getPosY(), 0.0);
    }

    @Test
    public void shouldNotFindIntersectionPoint_forTwoLinesFromOrdinate() {
        Line firstLine = Line.getLineByOrdinate(3.0);
        Line secondLine = Line.getLineByOrdinate(1.0);
        Optional<FieldObject> intersectionPointFromFistLine = firstLine.getIntersectionPointWithLine(secondLine);
        Optional<FieldObject> intersectionPointFromSecondLine = secondLine.getIntersectionPointWithLine(firstLine);
        assertFalse(intersectionPointFromFistLine.isPresent());
        assertFalse(intersectionPointFromSecondLine.isPresent());
    }

    @Test
    public void intersectionPointsShouldBeEquals() {
        Line firstLine = Line.getLineByTwoPoints(new FieldObject(-14.0, 0.0), new FieldObject(-5.0, 1.0));
        Line secondLine = Line.getLineByTwoPoints(new FieldObject(2.0, -3.0), new FieldObject(0.0, -8.0));
        Optional<FieldObject> intersectionPointFromFirstLine = firstLine.getIntersectionPointWithLine(secondLine);
        Optional<FieldObject> intersectionPointFromSecondLine = secondLine.getIntersectionPointWithLine(firstLine);
        if (!intersectionPointFromFirstLine.isPresent() && !intersectionPointFromSecondLine.isPresent()) {
            fail();
        } else {
            assertEquals(intersectionPointFromFirstLine.get(), intersectionPointFromSecondLine.get());
        }
    }

    @Test
    public void shouldNotFindIntersectionPoint_forEqualsLines() {
        Line firstLine = Line.getLineByTwoPoints(new FieldObject(-14.0, 0.0), new FieldObject(-5.0, 1.0));
        Optional<FieldObject> noIntersectionPoint = firstLine.getIntersectionPointWithLine(firstLine);
        assertFalse(noIntersectionPoint.isPresent());
    }
}
