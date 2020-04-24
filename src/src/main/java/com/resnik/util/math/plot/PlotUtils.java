package com.resnik.util.math.plot;

import com.resnik.util.images.ImageUtils;
import com.resnik.util.math.fractals.CircleApproximation;
import com.resnik.util.math.symbo.algebra.Bounds;
import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.Variable;
import com.resnik.util.math.symbo.algebra.parse.SymbolicSyntaxAnalyzer;

import java.awt.*;
import java.io.IOException;

public class PlotUtils {

    public static byte[][][] projection(Operation operation){
        return projection(Bounds.DEFAULT_10, Bounds.DEFAULT_10, operation);
    }

    public static byte[][][] projection(Bounds xBounds, Bounds yBounds, Operation operation){
        if(operation.getVariables().length != 2){
            throw new IllegalArgumentException("Must have two variables.");
        }

        int height = 2000;
        int width = height;
        double d_y = (yBounds.max.real - yBounds.min.real)/width;
        double d_x = (xBounds.max.real - xBounds.min.real)/height;
        Variable x = operation.getVariables()[0];
        Variable y = operation.getVariables()[1];
        if(!x.name.toLowerCase().equals("x") && y.name.toLowerCase().equals("x")){
            Variable temp = x;
            x = y;
            y = temp;
        }else if(!y.name.toLowerCase().equals("y") && x.name.toLowerCase().equals("y")){
            Variable temp = x;
            x = y;
            y = temp;
        }
        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        byte[][][] retImage = new byte[height][width][];
        for(int ROW = 0; ROW < height; ROW++){
            double curr_y = d_y*ROW + yBounds.min.real;
            for(int COL = 0; COL < width; COL++){
                double curr_x = d_x*COL + xBounds.min.real;
                Operation curr = operation.substitute(x, new Constant(curr_x)).substitute(y, new Constant(curr_y));
                if(!curr.allConstants()){
                    throw new IllegalStateException("Illegal variable state.");
                }
                max = Math.max(max, curr.constantRepresentation().getValue().real);
                min = Math.min(min, curr.constantRepresentation().getValue().real);
            }
        }
        for(int ROW = 0; ROW < height; ROW++){
            for(int COL = 0; COL < width; COL++){
                retImage[ROW][COL] = ImageUtils.WHITE_B;
            }
        }
        double mid = (max + min)/2.0d;
        Color[] POSITIVE = new Color[]{Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED};
        for(int ROW = 0; ROW < height; ROW++){
            double curr_y = d_y*(height - ROW - 1) + yBounds.min.real;
            for(int COL = 0; COL < width; COL++){
                double curr_x = d_x*COL + xBounds.min.real;
                try{
                    Operation curr = operation.substitute(x, new Constant(curr_x)).substitute(y, new Constant(curr_y));
                    double val = curr.constantRepresentation().getValue().real;
                    double grad = (val - min)/(max - min);
                    retImage[ROW][COL] = CircleApproximation.gradientB(1.0 - grad);
                }catch (Exception e){
                    continue;
                }
            }
        }

        return retImage;
    }

    public static void main(String[] args) throws IOException {
        SymbolicSyntaxAnalyzer analyzer = new SymbolicSyntaxAnalyzer();
        byte[][][] ret = projection(analyzer.analyze("x*y"));
        ImageUtils.saveImageBytes(ret, "res/test.bmp");
    }



}
