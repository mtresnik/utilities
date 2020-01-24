package com.resnik.util.math.fractals;


import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TriangleFractal {

    
    public static void generateTriangleGif(String outDir, int kMax){
        BufferedImage[] gifBuffered = new BufferedImage[kMax + 1];
        for (int k = 0; k < kMax + 1; k++) {
            gifBuffered[k] = ImageUtils.bytesToBufferedImage(testTriangles(k));
        }
        GifDecoder gd = new GifDecoder();
        try {
            ImageUtils.saveGifBuffered(gifBuffered, gd, outDir, 500, true);
        } catch (IOException ex) {
            Logger.getLogger(TriangleFractal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public static byte[][][] testTriangles(int kMax) {
        final int SIZE = 1000;
        byte[][][] retImage = new byte[SIZE][SIZE][];
        for (int ROW = 0; ROW < SIZE; ROW++) {
            for (int COL = 0; COL < SIZE; COL++) {
                retImage[ROW][COL] = ImageUtils.GREY_B(0.5);
            }
        }
        double height = 0.05;
        Point p1 = new Point(0 + height, 0 + height);
        Point p2 = new Point(0.5, Math.sqrt(3)/2 + height);
        Point p3 = new Point(1 - height, 0 + height);
        TriangleElement ROOT = new TriangleElement(p1, p2, p3);
        System.out.println("ROOT:" + ROOT);
        System.out.println(ROOT.isInside(0.5, 0.5));
        for (double ROW = 0; ROW < SIZE; ROW++) {
            for (double COL = 0; COL < SIZE; COL++) {
                double x = COL / SIZE;
                double y = ROW / SIZE;
                if (ROOT.isInside(x, y)) {
                    retImage[SIZE - (int) ROW - 1][(int) COL] = ImageUtils.WHITE_B;
                }
            }
        }
        TriangleElement dark = ROOT.darkTriangle();
        for (double ROW = 0; ROW < SIZE; ROW++) {
            for (double COL = 0; COL < SIZE; COL++) {
                if (dark.isInside(COL/SIZE, ROW/SIZE)) {
                    retImage[SIZE - (int) ROW - 1][(int) COL] = CircleApproximation.gradientB(4*dark.getArea() / ROOT.getArea());
                }
            }
        }
        System.out.println(dark);
        
        TriangleElement[] subTriangles = ROOT.lightTriangles();
        for (int k = 0; k < kMax; k++) {
            List<TriangleElement> subList = new ArrayList();
            for(TriangleElement currTriangle : subTriangles){
                dark = currTriangle.darkTriangle();
                for (double ROW = 0; ROW < SIZE; ROW++) {
                    for (double COL = 0; COL < SIZE; COL++) {
                        if (dark.isInside(COL / SIZE, ROW / SIZE)) {
                            retImage[SIZE - (int) ROW - 1][(int) COL] = CircleApproximation.gradientB(4*dark.getArea() / ROOT.getArea());
                        }
                    }
                }
                TriangleElement[] subSub = currTriangle.lightTriangles();
                for(TriangleElement currSub : subSub){
                    subList.add(currSub);
                }
            }
            subTriangles = subList.toArray(new TriangleElement[subList.size()]);
        }

        return retImage;

    }

    public static class Point {

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
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

    }

    public static class Edge {

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

            if (Objects.equals(p1, other.p1) && Objects.equals(p2, other.p2)) {
                return true;
            }
            if (Objects.equals(p1, other.p2) && Objects.equals(p2, other.p1)) {
                return true;
            }

            return false;
        }

        @Override
        public String toString() {
            return Arrays.toString(new Point[]{p1, p2});
        }

        public Point midPoint() {
            double midX = (p1.x + p2.x) / 2.0;
            double midY = (p1.y + p2.y) / 2.0;
            Point retPoint = new Point(midX, midY);
            return retPoint;
        }

    }

    public static class TriangleElement implements Comparable<TriangleElement> {

        public final Edge e1, e2, e3;
        public final double x1, y1, x2, y2, x3, y3;
        public final Point p1, p2, p3;

        public TriangleElement(Point p1, Point p2, Point p3) {
            e1 = new Edge(p1, p2);
            e2 = new Edge(p2, p3);
            e3 = new Edge(p3, p1);
            x1 = p1.x;
            y1 = p1.y;
            x2 = p2.x;
            y2 = p2.y;
            x3 = p3.x;
            y3 = p3.y;
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }

        public TriangleElement(Edge e1, Edge e2, Edge e3) {
            this.e1 = e1;
            this.e2 = e2;
            this.e3 = e3;
            Set<Double> xSet = new LinkedHashSet();
            xSet.add(e1.p1.x);
            xSet.add(e1.p2.x);
            xSet.add(e2.p1.x);
            xSet.add(e2.p2.x);
            xSet.add(e3.p1.x);
            xSet.add(e3.p2.x);
            List<Double> xList = new ArrayList(xSet);
            x1 = xList.get(0);
            x2 = xList.get(1);
            x3 = xList.get(2);

            Set<Double> ySet = new LinkedHashSet();
            ySet.add(e1.p1.y);
            ySet.add(e1.p2.y);
            ySet.add(e2.p1.y);
            ySet.add(e2.p2.y);
            ySet.add(e3.p1.y);
            ySet.add(e3.p2.y);
            List<Double> yList = new ArrayList(ySet);
            y1 = yList.get(0);
            y2 = yList.get(1);
            y3 = yList.get(2);

            this.p1 = new Point(x1, y1);
            this.p2 = new Point(x2, y2);
            this.p3 = new Point(x3, y3);
        }

        public double getArea() {
            return getArea(x1, y1, x2, y2, x3, y3);
        }

        public static double THRESH = 0.00001;
        
        public boolean isInside(double x, double y) {
//            System.out.println("isInside:" + x + " " + y);
            double A1 = Math.abs(x1 * (y2 - y) + x2 * (y - y1) + x * (y1 - y2));
            double A2 = Math.abs(x1 * (y - y3) + x * (y3 - y1) + x3 * (y1 - y));
            double A3 = Math.abs(x * (y2 - y3) + x2 * (y3 - y) + x3 * (y - y2));
            return Math.abs(this.getArea() -  (A1 + A2 + A3)) < THRESH;
        }


        public static double getArea(double x1, double y1, double x2, double y2, double x3, double y3) {
            return Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
        }

        @Override
        public int compareTo(TriangleElement t) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public TriangleElement darkTriangle() {
            Point m1 = e1.midPoint(), m2 = e2.midPoint(), m3 = e3.midPoint();
            return new TriangleElement(m1, m2, m3);
        }

        public TriangleElement[] lightTriangles() {
            Point m1 = e1.midPoint(), m2 = e2.midPoint(), m3 = e3.midPoint();
            TriangleElement t1 = new TriangleElement(m1, p2, m2);
            TriangleElement t2 = new TriangleElement(m2, p3, m3);
            TriangleElement t3 = new TriangleElement(m3, p1, m1);
            return new TriangleElement[]{t1, t2, t3};
        }

        public String toString() {
            return Arrays.toString(new Point[]{p1, p2, p3});
        }

    }

}
