package com.resnik.util.math.shapes.fractals;


import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;

import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class CircleApproximation {

    public static String intToHex(int i) {
        String hex = Integer.toHexString(i);
        return (hex.length() < 2) ? "0" + hex : hex;
    }

    public static String gradient(double value) {
        value = Math.min(Math.max(0, value), 1) * 510;
        int redValue, greenValue;
        if (value < 255) {
            redValue = 255;
            greenValue = (int) (Math.sqrt(value) * 16);
            greenValue = Math.round(greenValue);
        } else {
            greenValue = 255;
            value = value - 255;
            redValue = (int) (256 - (value * value / 255));
            redValue = Math.round(redValue);
        }
        String ret = intToHex(redValue) + intToHex(greenValue);
        return ret.length() > 4 ? ret + "000" : ret + "0000";
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    public static byte[] gradientB(double value) {
        return toByteArray(gradient(value));
    }
    
    
    public static class Point{
        public double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Point other = (Point) obj;
            if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
                return false;
            }
            if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
                return false;
            }
            return true;
        }
        
        
        @Override
        public String toString(){
            return "(" + x + ", " + y + ")";
        }
        
    }
    
    public static class Edge {
        public static Edge ROOT = new Edge(0,1,1,0);
        public Point p1, p2;

        public Edge(double x1, double y1, double x2, double y2) {
            this.p1 = new Point(x1, y1);
            this.p2 = new Point(x2, y2);
        }

        public Edge(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Edge other = (Edge) obj;
            
            if(Objects.equals(p1, other.p1) && Objects.equals(p2, other.p2)){
                return true;
            }
            if(Objects.equals(p1, other.p2) && Objects.equals(p2, other.p1)){
                return true;
            }
            
            return false;
        }

        @Override
        public String toString(){
            return Arrays.toString(new Point[]{p1, p2});
        }
        
    }
    
    public static class TriangleElement implements Comparable<TriangleElement> {

        public final double x1, y1, x2, y2, x3, y3;
        public static final TriangleElement ROOT = new TriangleElement(0, 1, 0, 0, 1, 0);

        public TriangleElement(double x1, double y1, double x2, double y2, double x3, double y3) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.x3 = x3;
            this.y3 = y3;
        }

        public double getArea() {
            return getArea(x1, y1, x2, y2, x3, y3);
        }

        public boolean isInside(double x, double y) {
            double A1 = getArea(x, y, x2, y2, x3, y3);
            double A2 = getArea(x1, y1, x, y, x3, y3);
            double A3 = getArea(x1, y1, x2, y2, x, y);
            return (this.getArea() == A1 + A2 + A3);
        }
        
        public boolean isInside(double x, double y, int SIZE){
            double A1 = getArea(x, y, x2*SIZE, y2*SIZE, x3*SIZE, y3*SIZE);
            double A2 = getArea(x1*SIZE, y1*SIZE, x, y, x3*SIZE, y3*SIZE);
            double A3 = getArea(x1*SIZE, y1*SIZE, x2*SIZE, y2*SIZE, x, y);
            return (this.getArea(x1*SIZE, y1*SIZE, x2*SIZE, y2*SIZE, x3*SIZE, y3*SIZE) == A1 + A2 + A3);
        }

        public static double getArea(double x1, double y1, double x2, double y2, double x3, double y3) {
            return Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0);
        }

        @Override
        public int compareTo(TriangleElement t) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        public static List<TriangleElement> generateTriangles(int k){
            if(k <= 0){
                return new ArrayList(){{this.add(ROOT);}};
            }
            List<TriangleElement> retList = new ArrayList();
            
            
            return retList;
        }
        
        public static byte[][][] generateCircleTriangle(){
            final int SIZE = 1000;
            byte[][][] retImage = new byte[SIZE][SIZE][];
            for (int ROW = 0; ROW < SIZE; ROW++) {
                for (int COL = 0; COL < SIZE; COL++) {
                    retImage[ROW][COL] = ImageUtils.WHITE_B;
                }
            }
            for (int ROW = 0; ROW < SIZE; ROW++) {
                for (int COL = 0; COL < SIZE; COL++) {
                    if (ROW * ROW + COL * COL <= SIZE * SIZE) {
                        retImage[SIZE - ROW - 1][COL] = ImageUtils.BLACK_B;
                    }
                }
            }
            TriangleElement currTriangle = ROOT;
            for (double ROW = 0; ROW < SIZE; ROW++) {
                for (double COL = 0; COL < SIZE; COL++) {
                    if(currTriangle.isInside(ROW, COL, SIZE) ){
                        retImage[SIZE - (int)ROW - 1][(int)COL] = gradientB(0.0);
                    }
                }
            }
            return retImage;
        }

    }

    public static class SquareElement implements Comparable<SquareElement> {

        public final int n;
        public final int k;
        public final double rep;
        public static final Comparator<SquareElement> comparator_x = (t1, t2) -> {
            return Double.compare(t1.x(), t2.x());
        };
        public static final Comparator<SquareElement> comparator_y = (t1, t2) -> {
            return Double.compare(t1.y(), t2.y());
        };
        public static final Comparator<SquareElement> comparator_area = (t1, t2) -> {
            return Double.compare(t2.area(), t1.area());
        };
        public static final SquareElement ZERO = new SquareElement(-1, 0), ONE = new SquareElement(0, 0);
        public SquareElement left, right, bottom, top;

        static {
            ZERO.left = ZERO;
            ZERO.right = ONE;
            ZERO.bottom = ONE;
            ZERO.top = ZERO;
            ONE.left = ZERO;
            ONE.right = ONE;
            ONE.bottom = ONE;
            ONE.top = ONE;
        }

        public SquareElement(int n, int k) {
            this.n = n;
            this.k = k;
            this.rep = rep(n, k);
        }

        public static double rep(int n, int k) {
            if (n < 0) {
                return 0.0;
            }
            double nMax = Math.pow(2, k - 1) - 1;
            if (n > nMax) {
                return 1.0;
            }
            double num = 2 * n + 1;
            double den = Math.pow(2, k);
            return num / den;
        }

        @Override
        public int compareTo(SquareElement t) {
            return comparator_x.compare(this, t);
        }

        @Override
        public String toString() {
            return Double.toString(rep);
        }

        public double x() {
            return Math.cos(rep * Math.PI / 2);
        }

        public double y() {
            return Math.sqrt(1 - Math.pow(x(), 2.0));
        }

        public double[] boundsUnordered() {
            double[] retArr = new double[4];
            retArr[0] = x();
            retArr[1] = y();
            retArr[2] = left.x();
            retArr[3] = bottom.y();
            return retArr;
        }

        public double[] boundsOrdered() {
            double[] bounds = boundsUnordered();
            double[] retBounds = new double[4];
            retBounds[0] = Math.min(bounds[0], bounds[2]);
            retBounds[2] = Math.max(bounds[0], bounds[2]);
            retBounds[1] = Math.min(bounds[1], bounds[3]);
            retBounds[3] = Math.max(bounds[1], bounds[3]);
            return retBounds;
        }

        public double area() {
            double[] bounds = boundsUnordered();
            double w = Math.max(bounds[2], bounds[0]) - Math.min(bounds[2], bounds[0]);
            double h = Math.max(bounds[3], bounds[1]) - Math.min(bounds[3], bounds[1]);
            return w * h;
        }

        public static byte[][][] generateCircleSquare(int kMax) {
            final int SIZE = 800;
            byte[][][] retImage = new byte[SIZE][SIZE][];
            for (int ROW = 0; ROW < SIZE; ROW++) {
                for (int COL = 0; COL < SIZE; COL++) {
                    retImage[ROW][COL] = ImageUtils.WHITE_B;
                }
            }
            for (int ROW = 0; ROW < SIZE; ROW++) {
                for (int COL = 0; COL < SIZE; COL++) {
                    if (ROW * ROW + COL * COL <= SIZE * SIZE) {
                        retImage[SIZE - ROW - 1][COL] = ImageUtils.BLACK_B;
                    }
                }
            }
            List<SquareElement> squares = maxHalves(kMax);
            squares.sort(SquareElement.comparator_area);

            double sum = 0.0;
            for (SquareElement currSquare : squares) {
                sum += currSquare.area();
                final double[] bounds = currSquare.boundsOrdered();
                double currArea = currSquare.area();
                double grad = 1.0 - currArea * 2;
                double num = (int) Math.log10(currArea) * -1;
                if (num > 0) {
                    grad = num / (num + 8);
                }
                grad *= 2;
                grad = 1 - grad;
                byte[] color = gradientB(grad);
                for (int ROW = (int) (bounds[1] * SIZE); ROW < bounds[3] * SIZE; ROW++) {
                    for (int COL = (int) (bounds[0] * SIZE); COL < bounds[2] * SIZE; COL++) {
                        retImage[SIZE - ROW - 1][COL] = color;
                    }
                }
            }

            byte[][][] left = ImageUtils.reflectCols(retImage);
            byte[][][] bottom = ImageUtils.reflectRows(retImage);
            byte[][][] bottomLeft = ImageUtils.reflectRowsCols(retImage);

            byte[][][][][] inputImageArray = new byte[][][][][]{
                {left, retImage},
                {bottomLeft, bottom}
            };
            retImage = ImageUtils.combineImages(inputImageArray);

            return retImage;
        }

        public static List<SquareElement> maxHalves(int kMax) {
            List<SquareElement> accumulation = new ArrayList();
            List<SquareElement> halves = new ArrayList();
            for (int k = 1; k <= kMax; k++) {
                halves = halves(k);
                accumulation.addAll(halves);
            }
            List<SquareElement> retList = new ArrayList();
            accumulation.sort(SquareElement.comparator_x);
            halves.sort(SquareElement.comparator_x);
            for (SquareElement lastSquare : halves) {
                SquareElement maxSquare = lastSquare;
                for (SquareElement ontoSquare : accumulation) {
                    if (ontoSquare.area() > maxSquare.area() && ontoSquare.x() == maxSquare.x()) {
                        maxSquare = ontoSquare;
                    }
                }
                retList.add(maxSquare);
            }
            return retList;
        }

        public static List<SquareElement> halves(int kMax) {
            List<SquareElement> retList = new ArrayList();
            for (int k = 1; k <= kMax; k++) {
                double nMax = Math.pow(2, k - 1) - 1;
                for (int n = 0; n <= nMax; n++) {
                    SquareElement curr = new SquareElement(n, k);
                    retList.add(curr);
                }
            }
            retList.add(SquareElement.ZERO);
            retList.add(SquareElement.ONE);
            retList.sort(SquareElement.comparator_x);
            for (int i = 1; i < retList.size() - 1; i++) {
                SquareElement curr = retList.get(i);
                curr.left = retList.get(i - 1);
                curr.right = retList.get(i + 1);
            }
            retList.sort(SquareElement.comparator_y);
            for (int i = 1; i < retList.size() - 1; i++) {
                SquareElement curr = retList.get(i);
                curr.bottom = retList.get(i - 1);
                curr.top = retList.get(i + 1);
            }
            retList.sort(SquareElement.comparator_x);
            return retList;
        }

        public static void testCircleSquare(String outputDir) throws IOException {
            int NUM = 10;
            BufferedImage[] arr = new BufferedImage[NUM];
            for (int k = 1; k <= NUM; k++) {
                byte[][][] write = generateCircleSquare(k);
                BufferedImage curr = ImageUtils.bytesToBufferedImage(write);
                arr[k - 1] = curr;
            }
            GifDecoder gd = new GifDecoder();
            ImageUtils.saveGifBuffered(arr, new GifDecoder(), outputDir, 250, true);
        }
    }

}
