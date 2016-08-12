package webonise.mapboxdemo.areabuffer;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EquationHandlerTest {
    final List<LatLng> pointList = new ArrayList<>();
    {
        pointList.add(new LatLng(18.5222294252479, 73.77664268016815));
        pointList.add(new LatLng(18.522987318585017, 73.77766728401184));
        pointList.add(new LatLng(18.522977145542317, 73.77920687198639));
        pointList.add(new LatLng(18.52205139612452, 73.77998471260071));
        pointList.add(new LatLng(18.52091709192927, 73.77995252609253));
        pointList.add(new LatLng(18.520316874109714, 73.77870798110962));
        pointList.add(new LatLng(18.520454212271208, 73.77709329128265));
        pointList.add(new LatLng(18.5222294252479, 73.77664268016815));

    }
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
    public void testGetPerpendicularLineEquationForValidData2() {
        EquationHandler equationHandler = new EquationHandler();
        try {
            LineEquation lineEquation = new LineEquation();
            lineEquation.setC(5);
            lineEquation.setM(0);
            Point centerPoint = new Point();
            centerPoint.setX(2.5);
            centerPoint.setY(5);
            LineEquation actualEquation = equationHandler.getPerpendicularLineEquation
                    (lineEquation, centerPoint);
            Assert.assertEquals("0.0y = -1.0x + 2.5", actualEquation.getEquation());
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
            Point[] polygon = pointList.toArray(new Point[pointList.size() - 1]);
            Point actualPoint = equationHandler
                    .getPointOnLineAtDistance(lineEquation, point, 10, polygon);
            Assert.assertEquals(10.0, actualPoint.getX(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
