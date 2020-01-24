package com.resnik.util.math.fractals;


import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SquareFractal {

    public static void generateSquareGif(String outDir){
        final int NUM = 4;
        BufferedImage[] squareImages = testSquare(NUM);
        GifDecoder gd = new GifDecoder();
        try {
            ImageUtils.saveGifBuffered(squareImages, gd, outDir, 500, true);
        } catch (IOException ex) {
            Logger.getLogger(TriangleFractal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static BufferedImage[] testSquare(int kMax) {
        final int SIZE = 1000;
        byte[][][] retImage = new byte[SIZE][SIZE][];
        for (int ROW = 0; ROW < SIZE; ROW++) {
            for (int COL = 0; COL < SIZE; COL++) {
                retImage[ROW][COL] = ImageUtils.GREY_B(0.5);
            }
        }
        final double PADDING = 0.1;
        SquareElement ROOT = new SquareElement(new Point(PADDING, 1 - PADDING), new Point(1 - PADDING, PADDING));
        for (double ROW = 0; ROW < SIZE; ROW++) {
            for (double COL = 0; COL < SIZE; COL++) {
                if (ROOT.isInside(ROW / SIZE, COL / SIZE)) {
                    retImage[SIZE - (int) ROW - 1][(int) COL] = ImageUtils.WHITE_B;
                }
            }
        }
        System.out.println(ROOT);
        SquareElement dark = ROOT.getBlackSquare();
        for (double ROW = 0; ROW < SIZE; ROW++) {
            for (double COL = 0; COL < SIZE; COL++) {
                if (dark.isInside(ROW / SIZE, COL / SIZE)) {
                    retImage[SIZE - (int) ROW - 1][(int) COL] = CircleApproximation.gradientB(9 * dark.getArea() / ROOT.getArea());
                }
            }
        }
        System.out.println(dark);
        SquareElement[] subSquares = ROOT.getWhiteSquares();
        List<BufferedImage> retList = new ArrayList();
        retList.add(ImageUtils.bytesToBufferedImage(retImage));
        for (int k = 0; k < kMax; k++) {
            List<SquareElement> subSquaresList = new ArrayList();
            for (SquareElement currSquare : subSquares) {
                dark = currSquare.getBlackSquare();
                for (double ROW = 0; ROW < SIZE; ROW++) {
                    for (double COL = 0; COL < SIZE; COL++) {
                        if (dark.isInside(ROW / SIZE, COL / SIZE)) {
                            retImage[SIZE - (int) ROW - 1][(int) COL] = CircleApproximation.gradientB(9 * dark.getArea() / ROOT.getArea());
                        }
                    }
                }
                    SquareElement[] curSubSquares = currSquare.getWhiteSquares();
                    for (SquareElement currSubSquare : curSubSquares) {
                        subSquaresList.add(currSubSquare);
                    }
            }
            retList.add(ImageUtils.bytesToBufferedImage(retImage));
            System.out.println(subSquaresList.size());
            subSquares = subSquaresList.toArray(new SquareElement[subSquaresList.size()]);
        }

        return retList.toArray(new BufferedImage[retList.size()]);
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

    public static class SquareElement {

        public double x1, y1, x2, y2, x3, y3, x4, y4;

        /*
        p1.x,p1.y (x1,y1)           p2.x,p1.y (x2, y2)
        p1.x,p2.y (x4,y4)       p2.x,p2.y (x3, y3)
         */
        public SquareElement(Point p1, Point p2) {
            x1 = p1.x;
            y1 = p1.y;
            x3 = p2.x;
            y3 = p2.y;
            x2 = x3;
            y2 = y1;
            x4 = x1;
            y4 = y3;
        }

        public SquareElement(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.x3 = x3;
            this.y3 = y3;
            this.x4 = x4;
            this.y4 = y4;
        }

        public double getArea() {
            double max_x = Math.max(x1, x2);
            double min_x = Math.min(x1, x2);
            double width = max_x - min_x;

            double max_y = Math.max(y1, y4);
            double min_y = Math.min(y1, y4);
            double height = max_y - min_y;

            return width * height;
        }

        public boolean isInside(double x, double y) {
            double max_x = Math.max(x1, x2);
            double min_x = Math.min(x1, x2);
            if (x > max_x || x < min_x) {
                return false;
            }

            double max_y = Math.max(y1, y4);
            double min_y = Math.min(y1, y4);
            if (y > max_y || y < min_y) {
                return false;
            }

            return true;
        }

        public Point topLeft() {
            return new Point(x1, y1);
        }

        public Point topRight() {
            return new Point(x3, y1);
        }

        public Point bottomLeft() {
            return new Point(x1, y3);
        }

        public Point bottomRight() {
            return new Point(x3, y3);
        }

        public SquareElement getBlackSquare() {

            // Find new topLeft
            double max_x = Math.max(x1, x2);
            double min_x = Math.min(x1, x2);
            double width = max_x - min_x;

            double max_y = Math.max(y1, y4);
            double min_y = Math.min(y1, y4);
            double height = max_y - min_y;

            double x1_new = min_x + width / 3.0;
            double y1_new = max_y - height / 3.0;

            // Find new bottomRight
            double x3_new = max_x - width / 3.0;
            double y3_new = min_y + height / 3.0;

            return new SquareElement(new Point(x1_new, y1_new), new Point(x3_new, y3_new));
        }

        public SquareElement[] getWhiteSquares() {
            double max_x = Math.max(x1, x2);
            double min_x = Math.min(x1, x2);
            double width = max_x - min_x;

            double max_y = Math.max(y1, y4);
            double min_y = Math.min(y1, y4);
            double height = max_y - min_y;

            double x1_new = min_x + width / 3.0;
            double y1_new = max_y - height / 3.0;

            // Find new bottomRight
            double x3_new = max_x - width / 3.0;
            double y3_new = min_y + height / 3.0;

            SquareElement M = this.getBlackSquare();
            // Top Left Square
            /*
            TL  TM  TR
            ML  M   MR
            BL  BM  BR
             */

            SquareElement TL = new SquareElement(topLeft(), M.topLeft());
            SquareElement TM = new SquareElement(TL.topRight(), M.topRight());
            SquareElement ML = new SquareElement(TL.bottomLeft(), M.bottomLeft());
            SquareElement BR = new SquareElement(M.bottomRight(), bottomRight());
            SquareElement MR = new SquareElement(M.topRight(), BR.topRight());
            SquareElement BM = new SquareElement(M.bottomLeft(), BR.bottomLeft());
            SquareElement BL = new SquareElement(ML.bottomLeft(), BM.bottomLeft());
            SquareElement TR = new SquareElement(TM.topRight(), MR.topRight());
            SquareElement[] retArray = new SquareElement[]{
                TL, TM, TR,
                ML, /*M*/ MR,
                BL, BM, BR
            };
            return retArray;
        }

        @Override
        public String toString() {
            return "SquareElement{" + topLeft() + " " + bottomRight() + '}';
        }

    }

}
