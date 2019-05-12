package util.math;

import util.math.symbo.Constant;
import util.math.symbo.Variable;
import util.math.symbo.Vector;


public class Point2d extends Point{
    
    public ComplexNumber x, y;
    
    public Point2d(ComplexNumber x, ComplexNumber y){
        super(x, y);
        this.x = x;
        this.y = y;
    }
    
    public Point2d(double x, double y){
        this(ComplexNumber.a(x), ComplexNumber.a(y));
    }
    
    public static Point2d b(double x, double y){
        return new Point2d(ComplexNumber.b(x), ComplexNumber.b(y));
    }
    

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    public static Vector[] toVectors(Point2d ... p_array){
        return toVectors(null, p_array);
    }
    
    public static Vector[] toVectors(Variable index_variable, Point2d ... p_array){
        Constant[] x_array = new Constant[p_array.length],
                y_array = new Constant[p_array.length];
        for (int i = 0; i < p_array.length; i++) {
            x_array[i] = new Constant(p_array[i].x);
            y_array[i] = new Constant(p_array[i].y);
        }
        Vector x_vec = new Vector("x", index_variable, x_array);
        Vector y_vec = new Vector("y", index_variable, y_array);
        return new Vector[]{x_vec, y_vec};
    }
    
    
    public static Point2d[] parsePoints(ComplexNumber ... c_array){
        if(c_array.length % 2 != 0){
            throw new IllegalArgumentException("c_array % 2 != 0");
        }
        Point2d[] retArray = new Point2d[c_array.length/2];
        for (int i = 0; i < retArray.length; i++) {
            int x_index = 2*i;
            int y_index = 2*i + 1;
            retArray[i] = new Point2d(c_array[x_index], c_array[y_index]);
        }
        return retArray;
    }
    
    public static Point2d[] parsePoints(double ... d_array){
        ComplexNumber[] c_array = ComplexNumber.a(d_array);
        return parsePoints(c_array);
    }
    
    public static Bounds[] findBounds(Point2d ... points){
        double xMin = Double.MAX_VALUE, xMax = Double.MIN_VALUE,
                yMin = Double.MAX_VALUE, yMax = Double.MIN_VALUE;
        for (int i = 0; i < points.length; i++) {
            xMin = Math.min(points[i].x.real, xMin);
            xMax = Math.max(points[i].x.real, xMax);
            yMin = Math.min(points[i].y.real, yMin);
            yMax = Math.max(points[i].y.real, yMax);
        }
        return new Bounds[]{new Bounds(xMin, xMax), new Bounds(yMin, yMax)};
    }
    
    public static Bounds[] findBoundsSpaced(double xSpacing, double ySpacing, Point2d ... points){
        Bounds[] bounds_xy = findBounds(points);
        Bounds xBounds = bounds_xy[0], yBounds = bounds_xy[1];
        xBounds.min = xBounds.min.subtract(ComplexNumber.a(xSpacing));
        xBounds.max = xBounds.max.add(ComplexNumber.a(xSpacing));
        
        yBounds.min = yBounds.min.subtract(ComplexNumber.a(ySpacing));
        yBounds.max = yBounds.max.add(ComplexNumber.a(ySpacing));
        return new Bounds[]{xBounds, yBounds};
    }

}
