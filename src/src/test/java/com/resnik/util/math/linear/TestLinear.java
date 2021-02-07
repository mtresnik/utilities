package com.resnik.util.math.linear;

import com.resnik.util.math.OperatorInterface;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.Variable;
import com.sun.javafx.geom.Matrix3f;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestLinear {

    @Test
    public void testMatrix(){
        Matrix<Double> matrix = Matrix.generate(new double[][]{
                {1,2,3},
                {3,2,1},
                {2,3,1}
        });
        System.out.println(matrix.dot(matrix));
        Matrix<Double> a = Matrix.generate(new double[][]{
                {1,0,1},
                {1,1,0},
                {0,1,1}
        });
        System.out.println(a.pow(25));

        Matrix<Operation> lo = Matrix.generateOperation(new Operation[][]{
                {Variable.THETA}
        });
        System.out.println(lo);

        Operation theta = Variable.THETA;
        Matrix<Operation> ex = Matrix.rotationMatrix(theta, OperatorInterface.OPERATION_INTERFACE);
        System.out.println(ex);

        Matrix<Operation> abcd = Matrix.generateOperation(new Operation[][]{
                {Variable.A, Variable.B},
                {Variable.C, Variable.D}
        });
        System.out.println(abcd);
        System.out.println(abcd.pow(3));
        SquareMatrix<Operation> temp = abcd.toSquareMatrix();
        System.out.println(a.scale(5.0));

        Matrix<Double> c = a.apply(Math::exp);
        System.out.println(c);

        System.out.println(matrix.removeRowCol(2,2));
    }

    @Test
    public void testColumn(){
        ColumnVector<Double> vector = ColumnVector.generate(1,2,3);
        System.out.println(vector);
        System.out.println(vector.magnitude());
        System.out.println(vector.unit());
        ColumnVector<Double> other = ColumnVector.generate(3,2,1);
        System.out.println(vector.theta(other));
        System.out.println(vector.hadamard(other));
    }

    @Test
    public void testDet(){
        Matrix<Double> matrix = Matrix.generate(new double[][]{
                {3,-5,3},
                {2,1,-1},
                {1,0,4}
        });
        double t = matrix.toSquareMatrix().det();
        assert (t == 54.0);
        Matrix<Double> matrix1 = Matrix.generate(new double[][]{
                {1,5,8,3},
                {4,1,9,5},
                {4,3,11,5},
                {7,10,15,6}
        });
        t = matrix1.toSquareMatrix().det();
        System.out.println(t);
    }

    @Test
    public void testRandDet(){
        int cases = 1;
        int size = 1000;
        long total = 0;
        for(int i = 0; i < cases; i++){
            long start = System.currentTimeMillis();
            double t = SquareMatrix.randomMatrix(size, 5).det();
            total += System.currentTimeMillis() - start;
        }
        System.out.println(total);

    }

}
