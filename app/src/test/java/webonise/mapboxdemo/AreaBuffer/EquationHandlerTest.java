package webonise.mapboxdemo.AreaBuffer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EquationHandlerTest {

    private Point firstPoint;
    private Point secondPoint;

    @Before
    public void setup() {
        firstPoint = new Point();
        firstPoint.setX(12);
        firstPoint.setY(13);
        secondPoint = new Point();
        secondPoint.setX(15);
        secondPoint.setY(16);
    }

    @Test
    public void testGetLineEquationTestForValidData() {
        EquationHandler equationHandler = new EquationHandler();
        try {
            LineEquation actualEquation = equationHandler.getLineEquation(firstPoint, secondPoint);
            Assert.assertEquals("y = 1.0x + 1.0", actualEquation.getEquation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetPerpendicularLineEquationForValidData() {
        EquationHandler equationHandler = new EquationHandler();
        try {
            LineEquation lineEquation = equationHandler.getLineEquation(firstPoint, secondPoint);
            Point centerPoint = new Point();
            centerPoint = centerPoint.getCenterPoint(firstPoint, secondPoint);
            LineEquation actualEquation = equationHandler.getPerpendicularLineEquation
                    (lineEquation, centerPoint);
            Assert.assertEquals("y = -1.0x + 28.0", actualEquation.getEquation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetParallelLineEquationForValidData() {
        EquationHandler equationHandler = new EquationHandler();
        try {
            LineEquation lineEquation = equationHandler.getLineEquation(firstPoint, secondPoint);
            Point point = new Point();
            point.setX(2);
            point.setY(7);
            LineEquation actualEquation = equationHandler.getParallelLineEquation(lineEquation,
                    point);
            Assert.assertEquals("y = 1.0x + 5.0", actualEquation.getEquation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetIntersectionPointForValidData() {
        EquationHandler equationHandler = new EquationHandler();
        try {
            LineEquation firstLine = new LineEquation();
            firstLine.setC(3);
            firstLine.setM(2);
            LineEquation secondLine = new LineEquation();
            secondLine.setC(7);
            secondLine.setM(-0.5);
            Point actualPoint = equationHandler
                    .getIntersectionPoint(firstLine, secondLine);
            Assert.assertEquals(1.6, actualPoint.getX(), 0);
            Assert.assertEquals(6.2, actualPoint.getY(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetPointOnLineAtDistanceForValidData() {
        EquationHandler equationHandler = new EquationHandler();
        try {
            LineEquation lineEquation = new LineEquation();
            lineEquation.setC(0);
            lineEquation.setM(0);
            Point point = new Point();
            point.setX(0);
            point.setY(4);
            Point actualPoint = equationHandler
                    .getPointOnLineAtDistance(lineEquation, point, 10);
            Assert.assertEquals(10.0, actualPoint.getX(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
