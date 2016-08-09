package webonise.mapboxdemo.AreaBuffer;

/**
 * Class represents equation of a line in the form of "y = mx + c"
 */
public class LineEquation {
    /**
     * "m" represents slope of a line
     * From math formula : m = (y2-y1) / (x2-x1)
     */
    private double m;
    /**
     * "c" represents 'y' intercept of a line
     */
    private double c;
    private String equation;

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public String getEquation() {
        return "y = " + m + "x + " + c;
    }
}
