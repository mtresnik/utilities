package com.resnik.util.math.linear;

import com.resnik.util.math.OperatorInterface;
import com.resnik.util.math.symbo.algebra.operations.Operation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Matrix<T> implements MatrixInterface<T>{

    protected final int height, width;
    protected final OperatorInterface<T> operatorInterface;
    protected T[][] elements;

    protected Matrix(int height, int width, OperatorInterface<T> operatorInterface) {
        this.height = height;
        this.width = width;
        this.operatorInterface = operatorInterface;
        T zero = operatorInterface.getZero();
        this.elements = (T[][])Array.newInstance(zero.getClass(), height, width);
        for(int ROW = 0; ROW < height; ROW++){
            this.elements[ROW] = (T[]) Array.newInstance(zero.getClass(), width);
            for(int COL = 0; COL < width; COL++){
                this.elements[ROW][COL] = operatorInterface.getZero();
            }
        }
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public MatrixInterface dot(MatrixInterface other) {
        return null;
    }

    @Override
    public MatrixInterface transpose() {
        return null;
    }

    @Override
    public List<RowVector> getRows() {
        return null;
    }

    @Override
    public List<ColumnVector> getColumns() {
        return null;
    }


    @Override
    public T[][] getElements() {
        return this.elements;
    }

    public static Matrix<Double> generate(int height, int width){
        return new Matrix<>(height, width, OperatorInterface.DOUBLE_OPERATOR_INTERFACE);
    }

    public static Matrix<Operation> operationMatrix(int height, int width){
        return new Matrix<>(height, width, OperatorInterface.OPERATION_INTERFACE);
    }
}
