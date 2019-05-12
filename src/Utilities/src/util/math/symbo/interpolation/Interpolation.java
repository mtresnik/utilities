package util.math.symbo.interpolation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import util.math.Algebraic;
import util.math.Point2d;
import util.math.matrices;
import util.math.ComplexNumber;
import util.math.symbo.Polynomial2d;

public final class Interpolation {

    private Interpolation() {
    }

    public static Polynomial2d linearInterpolation(Point2d p1, Point2d p2) {
        return interpolation_n(p1, p2);
    }

    public static Polynomial2d linearInterpolation(double x1, double y1, double x2, double y2) {
       return Interpolation.linearInterpolation(new Point2d(x1, y1), new Point2d(x2, y2));
    }

    public static Polynomial2d interpolation_n(Point2d p1, Point2d... pn) {
        int n = pn.length;
        if(n == 0){
            return new Polynomial2d(new ComplexNumber[]{p1.y});
        }
        ComplexNumber[][] X_matrix = new ComplexNumber[n + 1][n + 1];
        ComplexNumber[][] Y_vector = new ComplexNumber[n + 1][1];
        Point2d[] args = new Point2d[n + 1];
        List<Point2d> pointList = new ArrayList();
        pointList.add(p1);
        pointList.addAll(Arrays.asList(pn));
//        System.out.println(pointList);
        args = pointList.toArray(args);
        for (int p_i = 0; p_i < args.length; p_i++) {
            for (int COL = 0; COL < X_matrix[0].length; COL++) {
                X_matrix[p_i][COL] = args[p_i].x.pow(args.length - COL - 1);
            }
            Y_vector[p_i][0] = args[p_i].y;
        }
//        matrices.printMatrix(X_matrix, "X_matrix");
//        matrices.printMatrix(Y_vector, "Y_vector");
        ComplexNumber[][] X_inverse = matrices.inverse(X_matrix);
//        matrices.printMatrix(X_inverse, "X_inverse");
        ComplexNumber[][] identity_check = matrices.dot(X_inverse, X_matrix);
        matrices.printMatrix(identity_check, "Identity:");
        ComplexNumber[][] coefficients = matrices.dot(X_inverse, Y_vector);
        ComplexNumber[] coefficientLine = matrices.transpose(coefficients)[0];
//        System.out.println("coefficient Line:" + Arrays.toString(coefficientLine));
        Polynomial2d poly1 = new Polynomial2d(coefficientLine);
        for(Point2d p : pointList){
//            Algebraic error = poly1.evaluate(p.x).subtract(p.y);
//            System.out.printf("point:%s\terror:%s\n", p, error);
        }
        return poly1;
    }

    
}
