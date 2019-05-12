package util.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;
import util.math.symbo.Addition;
import util.math.symbo.Constant;
import util.math.symbo.Division;
import util.math.symbo.Operation;
import util.math.symbo.Multiplication;
import util.math.symbo.Variable;

public final class matrices {

    private matrices() {

    }

    public static final class mat2 {
    }

    public static final class mat3 {
    }

    public static final class mat4 {
    }

    public static int[] frequenciesFromData(double[][] data, double min, double max, int bins) {
        int[] frequency = new int[bins];
        double binSize = (max - min) / bins;
        for (int ROW = 0; ROW < data.length; ROW++) {
            for (int COL = 0; COL < data[0].length; COL++) {
                for (int BIN_INDEX = 0; BIN_INDEX < bins; BIN_INDEX++) {
                    if (data[ROW][COL] >= min) {
                        if (data[ROW][COL] < min + (BIN_INDEX + 1) * binSize) {
                            frequency[BIN_INDEX]++;
                            break;
                        }
                    }
                }
            }
        }
        return frequency;
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
            System.out.println("Matrix " + name + ":" + dimString(A));
        }
        System.out.println("[");
        for (int ROW = 0; ROW < A.length; ROW++) {
            System.out.println(Arrays.toString(A[ROW]));
            if (ROW == A.length - 1) {
                System.out.println("]");
            }
        }
    }

    public static void printMatrix(ComplexNumber[][] A, String name) {
        if (name.length() > 0) {
            System.out.println("Matrix " + name + ":" + dimString(A));
        }
        System.out.println("[");
        for (int ROW = 0; ROW < A.length; ROW++) {
            System.out.println(Arrays.toString(A[ROW]));
            if (ROW == A.length - 1) {
                System.out.println("]");
            }
        }
    }

    public static void printMatrix(Operation[][] A, String name) {
        if (name.length() > 0) {
            System.out.println("Matrix " + name + ":" + dimString(A));
        }
        System.out.println("[");
        for (int ROW = 0; ROW < A.length; ROW++) {
            System.out.println(Arrays.toString(A[ROW]));
            if (ROW == A.length - 1) {
                System.out.println("]");
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

    public static byte[] getColorSliding(double d) {
        if (d > 1.0) {
            d = 1.0;
        }
        if (d < 0.0) {
            d = 0.0;
        }
        double val = 120;
        return getColor(d * val + 360.0 - val);
    }

    public static byte[] getColor(double theta) {
        List<Pair<Double, int[]>> pairlist = new ArrayList();
        pairlist.add(new Pair(0.0, new int[]{255, 0, 0}));
        pairlist.add(new Pair(60.0, new int[]{255, 0, 255}));
        pairlist.add(new Pair(120.0, new int[]{0, 0, 255}));
        pairlist.add(new Pair(180.0, new int[]{0, 255, 255}));
        pairlist.add(new Pair(240.0, new int[]{0, 255, 0}));
        pairlist.add(new Pair(300.0, new int[]{255, 255, 0}));
        pairlist.add(new Pair(360.0, new int[]{255, 0, 0}));
        if (theta > 360.0) {
            theta -= 360.0;
        }
        if (theta < 0) {
            theta += 360.0;
        }
        for (int i = 0; i < pairlist.size() - 1; i++) {
            Pair<Double, int[]> pair1 = pairlist.get(i), pair2 = pairlist.get(i + 1);
            if (theta >= pair1.getKey() && theta <= pair2.getKey()) {
                // System.out.println(pair1.toString() + " \t" + pair2.toString());
                int[] final_color = pair2.getValue();
                int[] initial_color = pair1.getValue();
                double percentage = (theta - pair1.getKey()) / (pair2.getKey() - pair1.getKey());
                int[] new_color = new int[3];
                byte[] ret_color = new byte[3];
                for (int j = 0; j < 3; j++) {
                    new_color[j] = (int) (percentage * (final_color[j] - initial_color[j]) + initial_color[j]);
                    ret_color[j] = (byte) new_color[j];
                }
                return ret_color;
            }
        }
        return new byte[]{(byte) 255, 0, 0};

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
        List<Integer> rows = new ArrayList(Arrays.asList(generateSequenceO(0, m, 1)));
        List<Integer> cols = new ArrayList(Arrays.asList(generateSequenceO(0, n, 1)));
        rows.remove(r1);
        cols.remove(c1);
//        System.out.println("ROWS:" + rows);
//        System.out.println("COLS:" + cols);
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
        List<Integer> rows = new ArrayList(Arrays.asList(generateSequenceO(0, m, 1)));
        List<Integer> cols = new ArrayList(Arrays.asList(generateSequenceO(0, n, 1)));
        rows.remove(r1);
        cols.remove(c1);
//        System.out.println("ROWS:" + rows);
//        System.out.println("COLS:" + cols);
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
        List<Integer> rows = new ArrayList(Arrays.asList(generateSequenceO(0, m, 1)));
        List<Integer> cols = new ArrayList(Arrays.asList(generateSequenceO(0, n, 1)));
        rows.remove(r1);
        cols.remove(c1);
//        System.out.println("ROWS:" + rows);
//        System.out.println("COLS:" + cols);
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
        List<Integer> rows = new ArrayList(Arrays.asList(generateSequenceO(0, m, 1)));
        List<Integer> cols = new ArrayList(Arrays.asList(generateSequenceO(0, n, 1)));
        rows.remove(r1);
        cols.remove(c1);
//        System.out.println("ROWS:" + rows);
//        System.out.println("COLS:" + cols);
        for (int ROW = 0; ROW < retMatrix.length; ROW++) {
            for (int COL = 0; COL < retMatrix[0].length; COL++) {
                retMatrix[ROW][COL] = A[rows.get(ROW)][cols.get(COL)];
            }
        }
        return retMatrix;
    }

    public static int[] generateSequence(int start, int finish, int step) {
        int[] retArray = new int[(finish - start) / step];
        int value = start;
        for (int i = 0; i < retArray.length; i++) {
            retArray[i] = value;
            value += step;
        }
        return retArray;
    }

    public static Integer[] generateSequenceO(int start, int finish, int step) {
        Integer[] retArray = new Integer[(finish - start) / step];
        int value = start;
        for (int i = 0; i < retArray.length; i++) {
            retArray[i] = value;
            value += step;
        }
        return retArray;
    }

    public static double[] generateSequenceDouble(double start, double finish, int step) {
        double[] retArray = new double[(int) ((finish - start) / step)];
        double value = start;
        for (int i = 0; i < retArray.length; i++) {
            retArray[i] = value;
            value += step;
        }
        return retArray;
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
//        matrices.printMatrix(A, "Input Matrix:");
        for (int ROW = 0; ROW < n; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                sign = Math.pow(-1.0, ROW + COL + 2);
                double[][] tempMatrix = removeRowCol(A, ROW, COL);
                double value = sign * det(tempMatrix);
                retMatrix[ROW][COL] = value;
                signMatrix[ROW][COL] = sign;
            }
        }
//        printMatrix(signMatrix, "sign_matrix");
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
//        matrices.printMatrix(A, "Input Matrix:");
        for (int ROW = 0; ROW < n; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                sign = Math.pow(-1.0, ROW + COL + 2);
                ComplexNumber[][] tempMatrix = removeRowCol(A, ROW, COL);
                ComplexNumber value = det(tempMatrix).scale(sign);
                retMatrix[ROW][COL] = value;
                signMatrix[ROW][COL] = sign;
            }
        }
//        printMatrix(signMatrix, "sign_matrix");
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
//        matrices.printMatrix(A, "Input Matrix:");
        for (int ROW = 0; ROW < n; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                sign = Math.pow(-1.0, ROW + COL + 2);
                Operation[][] tempMatrix = removeRowCol(A, ROW, COL);
                Operation value = new Multiplication(det(tempMatrix), new Constant(sign));
                retMatrix[ROW][COL] = value;
                signMatrix[ROW][COL] = sign;
            }
        }
//        printMatrix(signMatrix, "sign_matrix");
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
//        matrices.printMatrix(A, "Input Matrix:");
        for (int ROW = 0; ROW < n; ROW++) {
            for (int COL = 0; COL < n; COL++) {
                sign = Math.pow(-1.0, ROW + COL + 2);
                Algebraic[][] tempMatrix = removeRowCol(A, ROW, COL);
                Algebraic value = det(tempMatrix).multiply(new Constant(sign));
                retMatrix[ROW][COL] = value;
                signMatrix[ROW][COL] = sign;
            }
        }
//        printMatrix(signMatrix, "sign_matrix");
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
//            System.out.println("sign:" + sign);
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
//            System.out.println("sign:" + sign);
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
//            System.out.println("sign:" + sign);
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
//            System.out.println("sign:" + sign);
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
//        System.out.println("det:" + det_a);
//        matrices.printMatrix(A, "pre-adj");
        double[][] adjugate_a = adjugate(A);
//        matrices.printMatrix(adjugate_a, "Adjugate");
        double[][] retMatrix = scale(adjugate_a, 1.0 / det_a);
//        matrices.printMatrix(retMatrix, "X_inverse");
        return retMatrix;
    }

    public static ComplexNumber[][] inverse(ComplexNumber[][] A) {
        ensureSquare(A);
        ComplexNumber det_a = det(A);
        if (det_a.equals(ComplexNumber.ZERO)) {
            throw new IllegalArgumentException("A is not an invertible matrix.");
        }
        System.out.println("det:" + det_a);
//        matrices.printMatrix(A, "pre-adj");
        ComplexNumber[][] adjugate_a = adjugate(A);
//        matrices.printMatrix(adjugate_a, "Adjugate");
        ComplexNumber det_a_1 = ComplexNumber.ONE.divide(det_a);
        System.out.println("det_1:" + det_a_1);
        ComplexNumber[][] retMatrix = scale(adjugate_a, det_a_1);
//        matrices.printMatrix(retMatrix, "X_inverse");
        return retMatrix;
    }

    public static Operation[][] inverse(Operation[][] A) {
        ensureSquare(A);
        Operation det_a = det(A);
        if (det_a.allConstants() && det_a.constantRepresentation().equals(Constant.ZERO)) {
            throw new IllegalArgumentException("A is not an invertible matrix.");
        }
//        System.out.println("det:" + det_a);
//        matrices.printMatrix(A, "pre-adj");
        Operation[][] adjugate_a = adjugate(A);
//        matrices.printMatrix(adjugate_a, "Adjugate");
        Operation[][] retMatrix = scale(adjugate_a, new Division(Constant.ONE, det_a));
//        matrices.printMatrix(retMatrix, "X_inverse");
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
