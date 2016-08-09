package webonise.mapboxdemo.AreaBuffer;

import java.util.ArrayList;
import java.util.List;

public class AreaBuffer {
    /**
     * Function to get the buffer points from a given set of lat lng
     *
     * @param pointList List<Points> A list of INCLOSED points
     * @return List<Point> list of buffered points
     * @throws Exception
     */
    public List<Point> buffer(List<Point> pointList) throws Exception {
        EquationHandler equationHandler = new EquationHandler();
        List<LineEquation> lineEquationList = new ArrayList<>();
        /**
         * Step 1 : Get the list of line equation from given points.
         */
        for (int i = 0; i < pointList.size() - 1; i++) {
            lineEquationList.add(equationHandler
                    .getLineEquation(pointList.get(i), pointList.get(i + 1)));
        }

        return null;
    }
}
