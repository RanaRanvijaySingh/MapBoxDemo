package webonise.mapboxdemo;

import java.util.ArrayList;
import java.util.List;

import webonise.mapboxdemo.areabuffer.EquationHandler;
import webonise.mapboxdemo.areabuffer.LineEquation;
import webonise.mapboxdemo.areabuffer.Point;

public class Transects {
    private static double MIN_LAT_INIT = 90.0; // initialize min x at max value
    private static double MAX_LAT_INIT = -90.0; // initialize max x at min value
    private static double MIN_LON_INIT = 180.0; // initialize min lon at max value

    public static List<Point> generateTransects(List<Point> polygonPoints, int angle) {
        List<Point> transectPoints = new ArrayList<>();
        EquationHandler equationHandler = new EquationHandler();
        try {

            /**
             * STEP 1: Get all line equation in a list.
             */
            List<LineEquation> edgeLines = getAllEdgeLineEquations(polygonPoints);

            /**
             * STEP 2: Find which corner should be selected as a reference points
             *  top left corner or bottom left corner of polygon.
             */
            Point referencePoint;
            boolean isReferencePointOnTop = isReferencePointTopLeftCorner(angle);
            if (isReferencePointOnTop) {
                referencePoint = getTopLeftCornerPoint(polygonPoints);
            } else {
                referencePoint = getBottomLeftCornerPoint(polygonPoints);
            }
            /**
             * STEP 3: Get reference line equation based on given angle and reference point.
             */
            LineEquation referenceLine = equationHandler.getLineEquation(angle, referencePoint);
            double distanceBetweenTransects = 0.0001;
            int transectCount = 0;
            boolean isPolygonEndReached = false;
            do {
                /**
                 * Distance from reference point will be calculated based on
                 * Given distance X total number of transects generated.
                 */
                double distanceFromBasePoint = distanceBetweenTransects * transectCount;
                /**
                 * Find the next point from where next transect is suppose to pass.
                 */
                Point transectCrossingPoint = getTransectCrossingPoint(referenceLine, referencePoint,
                        distanceFromBasePoint, isReferencePointOnTop);
                /**
                 * Get the equation of transect line passing through new point.
                 */
                LineEquation transectLine = equationHandler.getParallelLineEquation
                        (referenceLine, transectCrossingPoint);
                /**
                 * Find all the point of intersections of transect line and all edge lines. Save
                 * only those points which are on the polygon.
                 */
                List<Point> intersectionPoints = getAllIntersectionPoints(transectLine,
                        edgeLines, polygonPoints);
                //Check selected base point
                //If its top then
                //  Get the next parallel line from given line at a given distance by DECREASING LATITUDE.
                //else
                //  Get the next parallel line from given line at a given distance by INCREASING LONGITUDE.
                //Repeat the above process.  Until there is no point of intersection.
            } while (!isPolygonEndReached);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Function to get all the line equation of edges of the polygon.
     *
     * @param polygonPoints List<Points>
     * @return List<LineEquation>
     */
    private static List<LineEquation> getAllEdgeLineEquations(List<Point> polygonPoints) {
        EquationHandler equationHandler = new EquationHandler();
        List<LineEquation> edgeLines = new ArrayList<>();
        for (int i = 0; i < polygonPoints.size(); i++) {
            Point firstPoint = polygonPoints.get(i);
            Point secondPoint = polygonPoints.get((i + 1) % polygonPoints.size());
            /**
             * Get equation of line from two points
             */
            try {
                edgeLines.add(equationHandler.getLineEquation(firstPoint, secondPoint));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return edgeLines;
    }

    /**
     * Function to check if the top left corner should be selected or not.
     * If the angle falls between 0 to 90 or 180 to 270 then return true else false
     *
     * @param angle int
     * @return boolean
     */
    private static boolean isReferencePointTopLeftCorner(int angle) {
        return angle % 180 >= 0 && angle % 180 < 90;
    }

    /**
     * Function to get the top left corner point of the polygon.
     *
     * @param polygonPoints List<Point>
     * @return Point
     */
    public static Point getTopLeftCornerPoint(List<Point> polygonPoints) {
        /**
         * Instead of 0,0 as the reference point we changed it to max x and max y.
         */
        double maxLat = MAX_LAT_INIT;
        double minLon = MIN_LON_INIT;
        for (Point point : polygonPoints) {
            if (point.getX() > maxLat) {
                maxLat = point.getX();
            }
            if (point.getY() < minLon) {
                minLon = point.getY();
            }
        }
        return new Point(maxLat, minLon);
    }

    /**
     * Function to get the bottom left corner point of the polygon.
     *
     * @param polygonPoints List<Point>
     * @return Point
     */
    public static Point getBottomLeftCornerPoint(List<Point> polygonPoints) {
        double minLat = MIN_LAT_INIT;
        double minLon = MIN_LON_INIT;
        for (Point point : polygonPoints) {
            if (point.getX() < minLat) {
                minLat = point.getX();
            }
            if (point.getY() < minLon) {
                minLon = point.getY();
            }
        }
        return new Point(minLat, minLon);
    }

    /**
     * Function to get the transect crossing point at a given distance from given point.
     *
     * @param referenceLineEquation reference line from which you have to find the distance.
     * @param referencePoint        Point reference point from which you have to find next point.
     * @param distance              double distance at which you have to get the point.
     * @param isReferencePointOnTop flag to find out which of the points top one or bottom one we
     *                              have to select.
     * @return Point
     */
    public static Point getTransectCrossingPoint(LineEquation referenceLineEquation,
                                                 Point referencePoint, double distance,
                                                 boolean isReferencePointOnTop) {
        EquationHandler equationHandler = new EquationHandler();
        try {
            /**
             * Find the equation of a line perpendicular to given line.
             */
            LineEquation perpendicularLine = equationHandler.getPerpendicularLineEquation
                    (referenceLineEquation, referencePoint);
            /**
             * Find the points on the perpendicular line at a given distance.
             */
            Point[] pointsAtDistance = equationHandler.getPointOnLineAtDistance(perpendicularLine,
                    referencePoint, distance);
            return getTransectPoint(pointsAtDistance, isReferencePointOnTop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Function to find out which of the point we have to select from given two points.
     *
     * @param pointsOnSameLine      Point[]
     * @param isReferencePointOnTop boolean flag which tell us if the reference point at the top
     *                              or at the bottom.
     *                              if TRUE :  You will get point that is below the given line.
     *                              if FALSE : You will get point that is on right side of given
     *                              line.
     * @return Point
     */
    public static Point getTransectPoint(Point[] pointsOnSameLine, boolean
            isReferencePointOnTop) throws ArrayIndexOutOfBoundsException {
        /**
         * If the reference point is at the top then LATITTUDE should be decreasing for the next
         * point.
         */
        if (isReferencePointOnTop) {
            /**
             * Check which of the points have smaller latitude values.
             */
            return pointsOnSameLine[0].getX() < pointsOnSameLine[1].getX()
                    ? pointsOnSameLine[0] : pointsOnSameLine[1];
        }
        /**
         * If the reference point is at the bottom then LONGITUDE should be increasing for the next
         * point.
         */
        else {
            /**
             * Check which of the points have greater longitude values.
             */
            return pointsOnSameLine[0].getY() > pointsOnSameLine[1].getY()
                    ? pointsOnSameLine[0] : pointsOnSameLine[1];
        }
    }

    /**
     * Function to get all the intersection points between the polygon and the transect lines.
     *
     * @param transectLine  LineEquation
     * @param edgeLines     List<LineEquation>
     * @param polygonPoints List<Point>
     * @return List<Point>
     */
    public static List<Point> getAllIntersectionPoints(LineEquation transectLine,
                                                        List<LineEquation> edgeLines,
                                                        List<Point> polygonPoints) {
        List<Point> intersectionPoints = null;
        if (edgeLines != null) {
            intersectionPoints = new ArrayList<>();
            EquationHandler equationHandler = new EquationHandler();
            for (int i = 0; i < edgeLines.size(); i++) {
                try {
                    Point interceptPoint = equationHandler
                            .getIntersectionPoint(transectLine, edgeLines.get(i));
                    if (equationHandler.isPointOnPolygon(polygonPoints, interceptPoint)) {
                        intersectionPoints.add(interceptPoint);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return intersectionPoints;
    }
}
