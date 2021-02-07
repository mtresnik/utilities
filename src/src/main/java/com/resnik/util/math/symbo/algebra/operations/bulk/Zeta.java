package com.resnik.util.math.symbo.algebra.operations.bulk;

import com.resnik.util.images.ImageUtils;
import com.resnik.util.math.shapes.fractals.CircleApproximation;
import com.resnik.util.math.symbo.algebra.Bounds;
import com.resnik.util.math.symbo.algebra.ComplexNumber;
import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.Variable;
import com.resnik.util.math.symbo.algebra.operations.base.Addition;

import java.io.IOException;

public class Zeta extends Sigma{

    public Zeta(Constant s) {
        super((Constant.ONE.divide(Variable.N)).pow(s), Variable.N);
    }

    public Zeta(double s) {
        this(new Constant(s));
    }


    public Addition evaluate(Constant c){
        System.out.println("Eval:" + new Bounds(Constant.ONE.getValue().real, c.getValue().real));
        return this.evaluateBounds(new Bounds(Constant.ONE.getValue().real, c.getValue().real));
    }

    public static void main(String[] args) {
        double iMax = 10;
        double iMin = - iMax;
        double ppi = 30;
        final int size = (int) (iMax * iMax * ppi);
        byte[][][] img = new byte[size][size][];
        for(int ROW = 0; ROW < size; ROW++){
            for (int COL = 0; COL < size; COL++) {
                img[ROW][COL] = ImageUtils.WHITE_B;
                img[size/2][COL] = ImageUtils.BLACK_B;
            }
            img[ROW][size/2] = ImageUtils.BLACK_B;
        }
        double xMin = iMin;
        double xMax = iMax;
        int height = size;
        int width = size;
        double maxN = 100;
        double d_i = (iMax - iMin)/height;
        double d_x = (xMax - xMin)/width;
        for(int ROW = 0; ROW < size; ROW++){
            System.out.println("Row:" + ROW);
            double curr_i = d_i*(height - ROW - 1) + iMin;
            for (int COL = 0; COL < size; COL++) {
                double curr_x = d_x*COL + xMin;
                double g;
                if(curr_x < 0){
                    g = 0.0;
                }else{
                    ComplexNumber curr = new ComplexNumber(curr_x, curr_i);
                    ComplexNumber sum = ComplexNumber.ZERO;
                    for(double n = 1; n <= maxN; n++){
                        sum = sum.add(ComplexNumber.a(1/n).pow(curr));
                    }
                    double radius = sum.r();
                    g = 1 / radius;
                }
                img[ROW][COL] = ImageUtils.grey(g);
            }
        }
        try {
            ImageUtils.saveImageBytes(img, "src/res/test.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
