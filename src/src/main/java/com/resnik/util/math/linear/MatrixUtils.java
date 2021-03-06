package com.resnik.util.math.linear;

import com.resnik.util.logger.Log;
import com.resnik.util.math.symbo.algebra.Algebraic;
import com.resnik.util.math.symbo.algebra.ComplexNumber;
import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.Variable;
import com.resnik.util.math.symbo.algebra.operations.base.Addition;
import com.resnik.util.math.symbo.algebra.operations.base.Division;
import com.resnik.util.math.symbo.algebra.operations.base.Multiplication;
import com.resnik.util.objects.arrays.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MatrixUtils {

    public static final String TAG = MatrixUtils.class.getSimpleName();

    private MatrixUtils() {

    }

    public static String[] toString(double[][] outputMatrix) {
        String[] toWrite = new String[outputMatrix.length];
        for (int ROW = 0; ROW < outputMatrix.length; ROW++) {
            String buffer = "";
            for (int COL = 0; COL < outputMatrix[ROW].length; COL++) {
                buffer += outputMatrix[ROW][COL];
                if (COL != outputMatrix[ROW].length - 1) {
                    buffer += " ";
                }
            }
            toWrite[ROW] = buffer;
        }
        return toWrite;
    }

    public static String dimString(double[][] A) {
        return "(" + A.length + "x" + A[0].length + ")";
    }

    public static <T> String dimString(T[][] A) {
        return "(" + A.length + "x" + A[0].length + ")";
    }

    public static double[][] randMatrix(int m, int n) {
        double[][] retMatrix = new double[m][n];
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                retMatrix[ROW][COL] = Math.random();
            }
        }
        return retMatrix;
    }

    public static double[][] transpose(double[][] A) {
        int m = A.length, n = A[0].length;
        double[][] retArray = new double[n][m];
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                retArray[COL][ROW] = A[ROW][COL];
            }
        }
        return retArray;
    }

    public static ComplexNumber[][] transpose(ComplexNumber[][] A) {
        int m = A.length, n = A[0].length;
        ComplexNumber[][] retArray = new ComplexNumber[n][m];
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                retArray[COL][ROW] = A[ROW][COL];
            }
        }
        return retArray;
    }

    public static Operation[][] transpose(Operation[][] A) {
        int m = A.length, n = A[0].length;
        Operation[][] retArray = new Operation[n][m];
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                retArray[COL][ROW] = A[ROW][COL];
            }
        }
        return retArray;
    }

    public static Algebraic[][] transpose(Algebraic[][] A) {
        int m = A.length, n = A[0].length;
        Algebraic[][] retArray = new Algebraic[n][m];
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                retArray[COL][ROW] = A[ROW][COL];
            }
        }
        return retArray;
    }

    public static void printMatrix(double[][]... matrices) {
        for (double[][] matrix : matrices) {
            printMatrix(matrix, "");
        }
    }

    public static void printMatrix(double[][] A, String name) {
        if (name.length() > 0) {
            Log.v(TAG,"Matrix " + name + ":" + dimString(A));
        }
        Log.v(TAG,"[");
        for (int ROW = 0; ROW < A.length; ROW++) {
            Log.v(TAG,Arrays.toString(A[ROW]));
            if (ROW == A.length - 1) {
                Log.v(TAG,"]");
            }
        }
    }

    public static void printMatrix(ComplexNumber[][] A, String name) {
        if (name.length() > 0) {
            Log.v(TAG,"Matrix " + name + ":" + dimString(A));
        }
        Log.v(TAG,"[");
        for (int ROW = 0; ROW < A.length; ROW++) {
            Log.v(TAG,Arrays.toString(A[ROW]));
            if (ROW == A.length - 1) {
                Log.v(TAG,"]");
            }
        }
    }

    public static void printMatrix(Operation[][] A, String name) {
        if (name.length() > 0) {
            Log.v(TAG,"Matrix " + name + ":" + dimString(A));
        }
        Log.v(TAG,"[");
        for (int ROW = 0; ROW < A.length; ROW++) {
            Log.v(TAG,Arrays.toString(A[ROW]));
            if (ROW == A.length - 1) {
                Log.v(TAG,"]");
            }
        }

    }

    public static double[][] dot(double[][] A, double[][] B) {
        int m = A.length, n = A[0].length, o = B.length, p = B[0].length;
        if ((n == 1 && p == 1 || m == 1 && o == 1) && (m > 1 || n > 1) && (o > 1 || p > 1)) {
            B = transpose(B);
            o = B.length;
            p = B[0].length;
        }
        if (n != o) {
            throw new IllegalArgumentException("Matrices are of improper lengths: \n(" + m + "x" + n + ")\t(" + o + "x" + p + ")");
        }
        double[][] retMatrix = new double[m][p];
        for (int ROW_C = 0; ROW_C < m; ROW_C++) {
            for (int COL_C = 0; COL_C < p; COL_C++) {
                double result = 0.0;
                for (int i = 0; i < n; i++) {
                    double A_elem = A[ROW_C][i];
                    double B_elem = B[i][COL_C];
                    result += A_elem * B_elem;
                }
                retMatrix[ROW_C][COL_C] = result;
            }
        }
        return retMatrix;
    }

    public static ComplexNumber[][] dot(ComplexNumber[][] A, ComplexNumber[][] B) {
        int m = A.length, n = A[0].length, o = B.length, p = B[0].length;
        if ((n == 1 && p == 1 || m == 1 && o == 1) && (m > 1 || n > 1) && (o > 1 || p > 1)) {
            B = transpose(B);
            o = B.length;
            p = B[0].length;
        }
        if (n != o) {
            throw new IllegalArgumentException("Matrices are of improper lengths: \n(" + m + "x" + n + ")\t(" + o + "x" + p + ")");
        }
        ComplexNumber[][] retMatrix = new ComplexNumber[m][p];
        for (int ROW_C = 0; ROW_C < m; ROW_C++) {
            for (int COL_C = 0; COL_C < p; COL_C++) {
                ComplexNumber result = ComplexNumber.a(0);
                for (int i = 0; i < n; i++) {
                    ComplexNumber A_elem = A[ROW_C][i];
                    ComplexNumber B_elem = B[i][COL_C];
                    result = result.add(A_elem.multiply(B_elem));
                }
                retMatrix[ROW_C][COL_C] = result;
            }
        }
        return retMatrix;
    }

    public static Operation[][] dot(Operation[][] A, Operation[][] B) {
        int m = A.length, n = A[0].length, o = B.length, p = B[0].length;
        if ((n == 1 && p == 1 || m == 1 && o == 1) && (m > 1 || n > 1) && (o > 1 || p > 1)) {
            B = transpose(B);
            o = B.length;
            p = B[0].length;
        }
        if (n != o) {
            throw new IllegalArgumentException("Matrices are of improper lengths: \n(" + m + "x" + n + ")\t(" + o + "x" + p + ")");
        }
        Operation[][] retMatrix = new Operation[m][p];
        for (int ROW_C = 0; ROW_C < m; ROW_C++) {
            for (int COL_C = 0; COL_C < p; COL_C++) {
                Operation result;
                Operation[] prods = new Operation[n];
                for (int i = 0; i < n; i++) {
                    Operation A_elem = A[ROW_C][i];
                    Operation B_elem = B[i][COL_C];
                    prods[i] = new Multiplication(A_elem, B_elem);
                }
                result = new Addition(prods);
                retMatrix[ROW_C][COL_C] = result;
            }
        }
        return retMatrix;
    }

    public static double[][] hadamard(double[][] A, double[][] B) {
        int m = A.length, n = A[0].length, o = B.length, p = B[0].length;
        if (m != o || n != p) {
            throw new IllegalArgumentException("Matrices are of improper lengths: \n(" + m + "x" + n + ")\t(" + o + "x" + p + ")");
        }
        double[][] retMatrix = new double[m][n];
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                retMatrix[ROW][COL] = A[ROW][COL] * B[ROW][COL];
            }
        }
        return retMatrix;
    }

    public static double[][] add(double[][] A, double[][] B) {
        int m = A.length, n = A[0].length, o = B.length, p = B[0].length;
        if (m != o || n != p) {
            throw new IllegalArgumentException("Matrices are of improper lengths: \n(" + m + "x" + n + ")\t(" + o + "x" + p + ")");
        }
        double[][] retMatrix = new double[m][n];
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                retMatrix[ROW][COL] = A[ROW][COL] + B[ROW][COL];
            }
        }
        return retMatrix;
    }

    public static double[][] subtract(double[][] A, double[][] B) {
        int m = A.length, n = A[0].length, o = B.length, p = B[0].length;
        if (m != o || n != p) {
            throw new IllegalArgumentException("Matrices are of improper lengths: \n(" + m + "x" + n + ")\t(" + o + "x" + p + ")");
        }
        double[][] retMatrix = new double[m][n];
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                retMatrix[ROW][COL] = A[ROW][COL] - B[ROW][COL];
            }
        }
        return retMatrix;
    }

    public static double[][] scale(double[][] A, double scalar) {
        int m = A.length, n = A[0].length;
        double[][] retMatrix = new double[m][n];
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                retMatrix[ROW][COL] = A[ROW][COL] * scalar;
            }
        }
        return retMatrix;
    }

    public static ComplexNumber[][] scale(ComplexNumber[][] A, ComplexNumber scalar) {
        int m = A.length, n = A[0].length;
        ComplexNumber[][] retMatrix = new ComplexNumber[m][n];
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                retMatrix[ROW][COL] = A[ROW][COL].multiply(scalar);
            }
        }
        return retMatrix;
    }

    public static Operation[][] scale(Operation[][] A, Operation scalar) {
        int m = A.length, n = A[0].length;
        Operation[][] retMatrix = new Operation[m][n];
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                retMatrix[ROW][COL] = new Multiplication(A[ROW][COL], scalar);
            }
        }
        return retMatrix;
    }

    public static Algebraic[][] scale(Algebraic[][] A, Algebraic scalar) {
        int m = A.length, n = A[0].length;
        Algebraic[][] retMatrix = new Algebraic[m][n];
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                retMatrix[ROW][COL] = A[ROW][COL].multiply(scalar);
            }
        }
        return retMatrix;
    }

    public static double sigma(double[][] A) {
        double total = 0.0;
        for (int ROW = 0; ROW < A.length; ROW++) {
            for (int COL = 0; COL < A[0].length; COL++) {
                total += A[ROW][COL];
            }
        }
        return total;
    }

    public static double sigmoid(double x) {
        return 1.0 / (1 + Math.exp(-1.0 * x));
    }

    public static double sigmoidPrime(double x) {
        return Math.exp(-1.0 * x) / Math.pow((1 + Math.exp(-1.0 * x)), 2);
    }

    public static double[][] zeroMatrix(int m, int n) {
        double[][] retMatrix = new double[m][n];
        for (int ROW = 0; ROW < m; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                retMatrix[ROW][COL] = 0.0;
            }
        }
        return retMatrix;
    }

    public static double justifyDouble(Double d) {
        if (d > 1.0) {
            d = 1.0;
        }
        if (d < 0.0) {
            d = 0.0;
        }
        return d;

    }

    public static void ensureSquare(double[][] A) {
        int m = A.length, n = A[0].length;
        boolean isSquare = m == n;
        if (!isSquare) {
            throw new IllegalArgumentException("Matrix A is not square. dim:(" + m + "x" + n + ")");
        }
    }

    public static <T> void ensureSquare(T[][] A) {
        int m = A.length, n = A[0].length;
        boolean isSquare = m == n;
        if (!isSquare) {
            throw new IllegalArgumentException("Matrix A is not square. dim:(" + m + "x" + n + ")");
        }
    }

    public static double[][] removeRowCol(double[][] A, int r1, int c1) {
        int m = A.length, n = A[0].length;
        double[][] retMatrix = new double[m - 1][n - 1];
        List<Integer> rows = new ArrayList(Arrays.asList(ArrayUtils.generateSequenceO(0, m, 1)));
        List<Integer> cols = new ArrayList(Arrays.asList(ArrayUtils.generateSequenceO(0, n, 1)));
        rows.remove(r1);
        cols.remove(c1);
        for (int ROW = 0; ROW < retMatrix.length; ROW++) {
            for (int COL = 0; COL < retMatrix[0].length; COL++) {
                retMatrix[ROW][COL] = A[rows.get(ROW)][cols.get(COL)];
            }
        }
        return retMatrix;
    }

    public static ComplexNumber[][] removeRowCol(ComplexNumber[][] A, int r1, int c1) {
        int m = A.length, n = A[0].length;
        ComplexNumber[][] retMatrix = new ComplexNumber[m - 1][n - 1];
        List<Integer> rows = new ArrayList(Arrays.asList(ArrayUtils.generateSequenceO(0, m, 1)));
        List<Integer> cols = new ArrayList(Arrays.asList(ArrayUtils.generateSequenceO(0, n, 1)));
        rows.remove(r1);
        cols.remove(c1);
        for (int ROW = 0; ROW < retMatrix.length; ROW++) {
            for (int COL = 0; COL < retMatrix[0].length; COL++) {
                retMatrix[ROW][COL] = A[rows.get(ROW)][cols.get(COL)];
            }
        }
        return retMatrix;
    }

    public static Operation[][] removeRowCol(Operation[][] A, int r1, int c1) {
        int m = A.length, n = A[0].length;
        Operation[][] retMatrix = new Operation[m - 1][n - 1];
        List<Integer> rows = new ArrayList(Arrays.asList(ArrayUtils.generateSequenceO(0, m, 1)));
        List<Integer> cols = new ArrayList(Arrays.asList(ArrayUtils.generateSequenceO(0, n, 1)));
        rows.remove(r1);
        cols.remove(c1);
        for (int ROW = 0; ROW < retMatrix.length; ROW++) {
            for (int COL = 0; COL < retMatrix[0].length; COL++) {
                retMatrix[ROW][COL] = A[rows.get(ROW)][cols.get(COL)];
            }
        }
        return retMatrix;
    }

    public static Algebraic[][] removeRowCol(Algebraic[][] A, int r1, int c1) {
        int m = A.length, n = A[0].length;
        Algebraic[][] retMatrix = new Algebraic[m - 1][n - 1];
        List<Integer> rows = new ArrayList(Arrays.asList(ArrayUtils.generateSequenceO(0, m, 1)));
        List<Integer> cols = new ArrayList(Arrays.asList(ArrayUtils.generateSequenceO(0, n, 1)));
        rows.remove(r1);
        cols.remove(c1);
        for (int ROW = 0; ROW < retMatrix.length; ROW++) {
            for (int COL = 0; COL < retMatrix[0].length; COL++) {
                retMatrix[ROW][COL] = A[rows.get(ROW)][cols.get(COL)];
            }
        }
        return retMatrix;
    }

    public static double[][] adjugate(double[][] A) {
        return transpose(cofactor(A));
    }

    public static double[][] cofactor(double[][] A) {
        ensureSquare(A);
        int n = A.length;
        double[][] retMatrix = new double[n][n];
        double sign = 1;
        double[][] signMatrix = new double[n][n];
        for (int ROW = 0; ROW < n; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                sign = Math.pow(-1.0, ROW + COL + 2);
                double[][] tempMatrix = removeRowCol(A, ROW, COL);
                double value = sign * det(tempMatrix);
                retMatrix[ROW][COL] = value;
                signMatrix[ROW][COL] = sign;
            }
        }
        return retMatrix;
    }

    private static ComplexNumber[][] adjugate(ComplexNumber[][] A) {
        return transpose(cofactor(A));
    }

    public static ComplexNumber[][] cofactor(ComplexNumber[][] A) {
        ensureSquare(A);
        int n = A.length;
        ComplexNumber[][] retMatrix = new ComplexNumber[n][n];
        double sign = 1;
        double[][] signMatrix = new double[n][n];
        for (int ROW = 0; ROW < n; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                sign = Math.pow(-1.0, ROW + COL + 2);
                ComplexNumber[][] tempMatrix = removeRowCol(A, ROW, COL);
                ComplexNumber value = det(tempMatrix).scale(sign);
                retMatrix[ROW][COL] = value;
                signMatrix[ROW][COL] = sign;
            }
        }
        return retMatrix;
    }

    private static Operation[][] adjugate(Operation[][] A) {
        return transpose(cofactor(A));
    }

    public static Operation[][] cofactor(Operation[][] A) {
        ensureSquare(A);
        int n = A.length;
        Operation[][] retMatrix = new Operation[n][n];
        double sign = 1;
        double[][] signMatrix = new double[n][n];
        for (int ROW = 0; ROW < n; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                sign = Math.pow(-1.0, ROW + COL + 2);
                Operation[][] tempMatrix = removeRowCol(A, ROW, COL);
                Operation value = new Multiplication(det(tempMatrix), new Constant(sign));
                retMatrix[ROW][COL] = value;
                signMatrix[ROW][COL] = sign;
            }
        }
        return retMatrix;
    }

    private static Algebraic[][] adjugate(Algebraic[][] A) {
        return transpose(cofactor(A));
    }

    public static Algebraic[][] cofactor(Algebraic[][] A) {
        ensureSquare(A);
        int n = A.length;
        Algebraic[][] retMatrix = new Algebraic[n][n];
        double sign = 1;
        double[][] signMatrix = new double[n][n];
        for (int ROW = 0; ROW < n; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                sign = Math.pow(-1.0, ROW + COL + 2);
                Algebraic[][] tempMatrix = removeRowCol(A, ROW, COL);
                Algebraic value = det(tempMatrix).multiply(new Constant(sign));
                retMatrix[ROW][COL] = value;
                signMatrix[ROW][COL] = sign;
            }
        }
        return retMatrix;
    }

    public static double det(double[][] A) {
        ensureSquare(A);
        int n = A.length;
        if (n == 1) {
            return A[0][0];
        }
        double sign = 1;
        double retSum = 0.0;
        for (int COL = 0; COL < A.length; COL++) {
            sign = Math.pow(-1.0, COL + 2);
            double[][] tempMatrix = removeRowCol(A, 0, COL);
            double tempElem = A[0][COL];
            double currRes = sign * tempElem * det(tempMatrix);
            retSum += currRes;
        }
        return retSum;
    }

    public static ComplexNumber det(ComplexNumber[][] A) {
        ensureSquare(A);
        int n = A.length;
        if (n == 1) {
            return A[0][0];
        }
        double sign = 1;
        ComplexNumber retSum = ComplexNumber.a(0);
        for (int COL = 0; COL < A.length; COL++) {
            sign = Math.pow(-1.0, COL + 2);
            ComplexNumber[][] tempMatrix = removeRowCol(A, 0, COL);
            ComplexNumber tempElem = A[0][COL];
            ComplexNumber currRes = tempElem.multiply(det(tempMatrix)).scale(sign);
            retSum = retSum.add(currRes);
        }
        return retSum;
    }

    public static Operation det(Operation[][] A) {
        ensureSquare(A);
        int n = A.length;
        if (n == 1) {
            return A[0][0];
        }
        double sign = 1;
        Operation retSum;
        List<Multiplication> productList = new ArrayList();
        for (int COL = 0; COL < A.length; COL++) {
            sign = Math.pow(-1.0, COL + 2);
            Operation[][] tempMatrix = removeRowCol(A, 0, COL);
            Operation tempElem = A[0][COL];
            Multiplication currRes = new Multiplication(tempElem, det(tempMatrix), new Constant(sign));
            productList.add(currRes);
        }
        Multiplication[] prods = new Multiplication[productList.size()];
        prods = productList.toArray(prods);
        retSum = new Addition(prods);
        return retSum;
    }

    public static Algebraic det(Algebraic[][] A) {
        ensureSquare(A);
        int n = A.length;
        if (n == 1) {
            return A[0][0];
        }
        double sign = 1;
        Algebraic retSum = Constant.ZERO;
        List<Algebraic> productList = new ArrayList();
        for (int COL = 0; COL < A.length; COL++) {
            sign = Math.pow(-1.0, COL + 2);
            Algebraic[][] tempMatrix = removeRowCol(A, 0, COL);
            Algebraic tempElem = A[0][COL];
            Algebraic currRes = tempElem.multiply(det(tempMatrix).multiply(new Constant(sign)));
            productList.add(currRes);
        }
        Algebraic[] prods = new Algebraic[productList.size()];
        prods = productList.toArray(prods);
        for (int i = 0; i < prods.length; i++) {
            retSum = retSum.add(prods[i]);
        }
        return retSum;
    }

    public static double[][] inverse(double[][] A) {
        ensureSquare(A);
        double det_a = det(A);
        if (det_a == 0) {
            throw new IllegalArgumentException("A is not an invertible matrix.");
        }
        double[][] adjugate_a = adjugate(A);
        double[][] retMatrix = scale(adjugate_a, 1.0 / det_a);
        return retMatrix;
    }

    public static ComplexNumber[][] inverse(ComplexNumber[][] A) {
        ensureSquare(A);
        ComplexNumber det_a = det(A);
        if (det_a.equals(ComplexNumber.ZERO)) {
            throw new IllegalArgumentException("A is not an invertible matrix.");
        }
        Log.v(TAG,"det:" + det_a);
        ComplexNumber[][] adjugate_a = adjugate(A);
        ComplexNumber det_a_1 = ComplexNumber.ONE.divide(det_a);
        Log.v(TAG,"det_1:" + det_a_1);
        ComplexNumber[][] retMatrix = scale(adjugate_a, det_a_1);
        return retMatrix;
    }

    public static Operation[][] inverse(Operation[][] A) {
        ensureSquare(A);
        Operation det_a = det(A);
        if (det_a.allConstants() && det_a.constantRepresentation().equals(Constant.ZERO)) {
            throw new IllegalArgumentException("A is not an invertible matrix.");
        }
        Operation[][] adjugate_a = adjugate(A);
        Operation[][] retMatrix = scale(adjugate_a, new Division(Constant.ONE, det_a));
        return retMatrix;
    }

    public static Algebraic[][] inverse(Algebraic[][] A) {
        ensureSquare(A);
        Algebraic det_a = det(A);
        if(det_a.toString().equals(Constant.ZERO.toString())){
            throw new IllegalArgumentException("A is not an invertible matrix.");
        }
        Algebraic[][] adjugate_a = adjugate(A);
        Algebraic[][] retMatrix = scale(adjugate_a, det_a.pow(new Constant(-1)));
        return retMatrix;
    }

    public static Operation[][] substitute(Operation[][] A, Variable var, Operation o) {
        Operation[][] retMat = new Operation[A.length][A[0].length];
        for (int ROW = 0; ROW < A.length; ROW++) {
            for (int COL = 0; COL < A[0].length; COL++) {
                retMat[ROW][COL] = A[ROW][COL].substitute(var, o);
            }
        }
        return retMat;
    }
    
    
    
}
