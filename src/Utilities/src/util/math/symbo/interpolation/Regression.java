package util.math.symbo.interpolation;

import java.util.Arrays;
import java.util.function.Function;
import javafx.scene.paint.Color;
import util.math.Bounds;
import util.math.ComplexNumber;
import util.math.Point2d;
import util.math.graphing.Graph2d.CartesianPlot;
import util.math.graphing.Graph2d.GraphDataset;
import util.math.graphing.Graph2d.GraphElement;
import util.math.graphing.Graph2d.GraphFunction;
import util.math.matrices;
import util.math.symbo.Constant;
import util.math.symbo.Multiplication;
import util.math.symbo.Operation;
import util.math.symbo.Polynomial2d;
import util.math.symbo.Power;
import util.math.symbo.Sigma;
import util.math.symbo.Variable;
import util.math.symbo.Vector;

public class Regression {

    public final Polynomial2d polynomial;
    public final Point2d[] dataset;
    public final ComplexNumber x_bar, y_bar;

    public Regression(int n, Point2d[] dataset) {
        this.dataset = dataset;
        this.polynomial = polynomialLinearRegression(n, dataset);
        this.x_bar = x_bar();
        this.y_bar = y_bar();
    }

    private ComplexNumber x_bar() {
        ComplexNumber retVal = ComplexNumber.ZERO;
        ComplexNumber xSum = ComplexNumber.ZERO;
        for (int i = 0; i < dataset.length; i++) {
            xSum = xSum.add(dataset[i].x);
        }
        retVal = xSum.divide(ComplexNumber.a(dataset.length));
        return retVal;
    }

    private ComplexNumber y_bar() {
        ComplexNumber retVal = ComplexNumber.ZERO;
        ComplexNumber ySum = ComplexNumber.ZERO;
        for (int i = 0; i < dataset.length; i++) {
            ySum = ySum.add(dataset[i].y);
        }
        retVal = ySum.divide(ComplexNumber.a(dataset.length));
        return retVal;
    }

    public ComplexNumber rSquared() {
        ComplexNumber retVal = ComplexNumber.ONE.subtract(SSE().divide(SSTO()));
        return retVal;
    }

    public ComplexNumber SSR() {
        return SSTO().subtract(SSE());
    }

    public ComplexNumber SSE() {
        ComplexNumber retVal = ComplexNumber.ZERO;
        Function<ComplexNumber, ComplexNumber> f = (x) -> {
            Operation o = this.polynomial.evaluate(Variable.X, new Constant(x));
            Constant c = o.constantRepresentation();
            return c.getValue();
        };
        for (int i = 0; i < dataset.length; i++) {
            ComplexNumber y_hat = f.apply(dataset[i].x);
            ComplexNumber elem = (dataset[i].y.subtract(y_hat)).pow(2);
            retVal = retVal.add(elem);
        }
        return retVal;
    }

    public ComplexNumber SSTO() {
        ComplexNumber retVal = ComplexNumber.ZERO;
        for (int i = 0; i < dataset.length; i++) {
            ComplexNumber elem = (dataset[i].y.subtract(y_bar)).pow(2);
            retVal = retVal.add(elem);
        }
        return retVal;
    }

    public void plot() {
        Function<Double, Double> f = (x) -> {
            Operation o = this.polynomial.evaluate(Variable.X, new Constant(x));
            Constant c = o.constantRepresentation();
            return c.getValue().real;
        };
        GraphElement f1 = new GraphFunction(f, Color.RED);
        GraphElement d1 = new GraphDataset(this.dataset, Color.WHITE);
        System.out.println("f1:" + f1);
        System.out.println("d1:" + d1);
        Bounds[] bounds_xy = Point2d.findBounds(dataset);
        System.out.println("bounds:" + Arrays.toString(bounds_xy));
        Bounds[] bounds_spaced = Point2d.findBoundsSpaced(2, 5, dataset);
        System.out.println("bounds_spaced:" + Arrays.toString(bounds_spaced));
        CartesianPlot p = new CartesianPlot(bounds_spaced[0], bounds_spaced[1], f1, d1);
        p.show();
    }

    public static Polynomial2d polynomialLinearRegression(int n, Point2d... points) {
        Vector[] x_y = Point2d.toVectors(points);
        return polynomialLinearRegression(n, x_y[0], x_y[1]);
    }

    public static Polynomial2d polynomialLinearRegression(int n, Vector x, Vector y) {
        Variable temp_x = x.getIndex_variable();
        Variable temp_y = y.getIndex_variable();
        x.setIndex_variable(Variable.I);
        y.setIndex_variable(Variable.I);
        int m = n + 1;
        Operation[][] X_mat = X_mat(m, x);
        Operation[][] Y_mat = Y_mat(m, x, y);

        Operation[][] X_inv = matrices.inverse(X_mat);
        Operation[][] beta_mat = matrices.dot(X_inv, Y_mat);
        matrices.printMatrix(beta_mat, "Beta");

        x.setIndex_variable(temp_x);
        y.setIndex_variable(temp_y);
        Constant[] coefficients = new Constant[m];
        for (int INDEX = 0; INDEX < m; INDEX++) {
            coefficients[INDEX] = beta_mat[m - INDEX - 1][0].constantRepresentation();
        }
        Polynomial2d retPoly = new Polynomial2d(coefficients);
        return retPoly;
    }

    public static Operation[][] X_mat(int m, Vector x) {
        Operation[][] retMat = new Operation[m][m];
        retMat[0][0] = new Constant(x.size());
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < m; COL++) {
                if (ROW == 0 && COL == 0) {
                    continue;
                }
                Power p = new Power(x, new Constant(ROW + COL));
                Sigma s = new Sigma(p, x.getIndex_variable());
                retMat[ROW][COL] = s.evaluateBounds(x.getBounds());
            }
        }
        return retMat;
    }

    public static Operation[][] Y_mat(int m, Vector x, Vector y) {
        Operation[][] retMat = new Operation[m][1];
        retMat[0][0] = new Sigma(y, y.getIndex_variable()).evaluateBounds(y.getBounds());
        for (int ROW = 1; ROW < m; ROW++) {
            Power p = new Power(x, new Constant(ROW));
            Multiplication mult = new Multiplication(p, y);
            Sigma s = new Sigma(mult, x.getIndex_variable());
            retMat[ROW][0] = s.evaluateBounds(x.getBounds());
        }
        return retMat;
    }

}
