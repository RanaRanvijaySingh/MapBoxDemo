package webonise.mapboxdemo.areabuffer;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import webonise.mapboxdemo.areabuffer.sromku.Polygon;

public class PointPositionCheckTest {
    final List<LatLng> latLngPolygon = new ArrayList<>();

    {
        latLngPolygon.add(new LatLng(18.5222294252479, 73.77664268016815));
        latLngPolygon.add(new LatLng(18.522987318585017, 73.77766728401184));
        latLngPolygon.add(new LatLng(18.522977145542317, 73.77920687198639));
        latLngPolygon.add(new LatLng(18.52205139612452, 73.77998471260071));
        latLngPolygon.add(new LatLng(18.52091709192927, 73.77995252609253));
        latLngPolygon.add(new LatLng(18.520316874109714, 73.77870798110962));
        latLngPolygon.add(new LatLng(18.520454212271208, 73.77709329128265));
        latLngPolygon.add(new LatLng(18.5222294252479, 73.77664268016815));

    }

    @Test
    public void isInsideTestForInsidePoint() {
        PointPositionCheck pointPositionCheck = new PointPositionCheck();
        List<Point> polygonPoints = new ArrayList<>();
        for (int i = 0; i < latLngPolygon.size() - 1; i++) {
            Point point = new Point();
            point.setX(latLngPolygon.get(i).getLatitude());
            point.setY(latLngPolygon.get(i).getLongitude());
            polygonPoints.add(point);
        }
        Point[] polygon = polygonPoints.toArray(new Point[polygonPoints.size() - 1]);
        Point innerPoint = new Point();
        innerPoint.setX(18.52146644198572);
        innerPoint.setY(73.77792477607727);
        boolean actualValue = pointPositionCheck.isInside(polygon, polygon.length, innerPoint);
        Assert.assertTrue(actualValue);
    }

    @Test
    public void isInsideTestForInsidePoint2() {
        PointPositionCheck pointPositionCheck = new PointPositionCheck();
        List<Point> points = new ArrayList<>();
        Point point = new Point();
        point.setX(0);
        point.setY(10);
        points.add(point);
        Point point1 = new Point();
        point1.setX(10);
        point1.setY(10);
        points.add(point1);
        Point point2 = new Point();
        point2.setX(10);
        point2.setY(0);
        points.add(point2);
        Point point3 = new Point();
        point3.setX(0);
        point3.setY(0);
        points.add(point3);
        Point[] polygon = points.toArray(new Point[points.size()]);
        Point innerPoint = new Point();
        innerPoint.setX(5);
        innerPoint.setY(5);
        boolean actualValue = pointPositionCheck.isInside(polygon, polygon.length, innerPoint);
        Assert.assertTrue(actualValue);
    }

    @Test
    public void isInsideTestForInsidePoint3() {
        PointPositionCheck pointPositionCheck = new PointPositionCheck();
        List<Point> points = new ArrayList<>();
        Point point1 = new Point();
        point1.setY(73.77743124961853);
        point1.setX(18.52240236764744);
        points.add(point1);
        Point point2 = new Point();
        point2.setY(73.77688407897949);
        point2.setX(18.520438952480948);
        points.add(point2);
        Point point3 = new Point();
        point3.setY(73.77718448638916);
        point3.setX(18.51930972422245);
        points.add(point3);
        Point point4 = new Point();
        point4.setY(73.77923369407654);
        point4.setX(18.51895365971209);
        points.add(point4);
        Point point5 = new Point();
        point5.setY(73.78017783164978);
        point5.setX(18.520906918763433);
        points.add(point5);
        Point point6 = new Point();
        point6.setY(73.77743124961853);
        point6.setX(18.52240236764744);
        points.add(point6);

        Point[] polygon = points.toArray(new Point[points.size()]);
        Point innerPoint = new Point();
        innerPoint.setX(18.520530511202185);
        innerPoint.setY(73.77832174301147);
        boolean actualValue = pointPositionCheck.isInside(polygon, polygon.length, innerPoint);
        Assert.assertTrue(actualValue);
    }

    @Test
    public void orientationTestForClockwise() {
        Point p = new Point();
        p.setX(1);
        p.setY(1);
        Point q = new Point();
        q.setX(1);
        q.setY(10);
        Point r = new Point();
        r.setX(5);
        r.setY(5);
        PointPositionCheck pointPositionCheck = new PointPositionCheck();
        Assert.assertEquals(1, pointPositionCheck.orientation(p, q, r));
    }

    @Test
    public void orientationTestForAntiClockwise() {
        Point p = new Point();
        p.setX(5);
        p.setY(5);
        Point q = new Point();
        q.setX(1);
        q.setY(1);
        Point r = new Point();
        r.setX(10);
        r.setY(1);
        PointPositionCheck pointPositionCheck = new PointPositionCheck();
        Assert.assertEquals(2, pointPositionCheck.orientation(p, q, r));
    }

    @Test
    public void orientationTestForClockwiseWithLatLng() {
        Point p = new Point();
        p.setX(18.52236167533387);
        p.setY(73.77558588981628);
        Point q = new Point();
        q.setX(18.522117521249115);
        q.setY(73.77968430519104);
        Point r = new Point();
        r.setX(18.51954370906854);
        r.setY(73.77920150756836);
        PointPositionCheck pointPositionCheck = new PointPositionCheck();
        Assert.assertEquals(2, pointPositionCheck.orientation(p, q, r));
    }

    @Test
    public void orientationTestForAntiClockwiseWithLatLng() {
        Point q = new Point();
        q.setX(18.52236167533387);
        q.setY(73.77558588981628);
        Point p = new Point();
        p.setX(18.522117521249115);
        p.setY(73.77968430519104);
        Point r = new Point();
        r.setX(18.51954370906854);
        r.setY(73.77920150756836);
        PointPositionCheck pointPositionCheck = new PointPositionCheck();
        Assert.assertEquals(1, pointPositionCheck.orientation(p, q, r));
    }

    @Test
    public void isInsideTestForInsidePoint4() {
        Polygon polygon = Polygon.Builder()
                .addVertex(new webonise.mapboxdemo.areabuffer.sromku.Point(
                        18.52240236764744, 73.77743124961853))
                .addVertex(new webonise.mapboxdemo.areabuffer.sromku.Point(
                        18.520438952480948, 73.77688407897949))
                .addVertex(new webonise.mapboxdemo.areabuffer.sromku.Point(
                        18.51930972422245, 73.77718448638916))
                .addVertex(new webonise.mapboxdemo.areabuffer.sromku.Point(
                        18.51895365971209, 73.78017783164978))
                .addVertex(new webonise.mapboxdemo.areabuffer.sromku.Point(
                        18.52240236764744, 73.77743124961853))
                .build();
        webonise.mapboxdemo.areabuffer.sromku.Point innerPoint = new
                webonise.mapboxdemo.areabuffer.sromku.Point(18.520530511202185, 73.77832174301147);
        Assert.assertTrue(polygon.contains(innerPoint));
    }

}
