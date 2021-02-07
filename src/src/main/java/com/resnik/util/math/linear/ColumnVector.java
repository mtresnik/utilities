package com.resnik.util.math.linear;

import com.resnik.util.math.OperatorInterface;
import com.resnik.util.math.symbo.algebra.operations.Operation;

public class ColumnVector<T> extends Matrix<T>{

    protected ColumnVector(int height, OperatorInterface<T> operatorInterface) {
        super(height, 1, operatorInterface);
    }

    @Override
    public RowVector<T> transpose() {
        RowVector<T> retMatrix = new RowVector<T>(this.height, this.operatorInterface);
        for(int i = 0; i < this.height; i++){
            retMatrix.set(0, i, this.elements[i][0]);
        }
        return retMatrix;
    }

    public T magnitude(){
        T sum = operatorInterface.getZero();
        for(int i = 0; i < height; i++){
            sum = operatorInterface.add(sum, operatorInterface.multiply(this.elements[i][0], this.elements[i][0]));
        }
        return operatorInterface.sqrt(sum);
    }

    public ColumnVector<T> unit(){
        T magnitude = magnitude();
        ColumnVector<T> ret = new ColumnVector<>(this.height, this.operatorInterface);
        for(int i = 0; i < height; i++){
            ret.set( i, 0, operatorInterface.divide(elements[i][0], magnitude));
        }
        return ret;
    }

    public T theta(ColumnVector<T> other){
        if(other.height != this.height){
            throw new IllegalArgumentException("Heights don't match.");
        }
        Matrix<T> ret = this.unit().transpose().dot(other.unit());
        return operatorInterface.acos(ret.toScalar());
    }

    public static ColumnVector<Double> generate(double ... values){
        ColumnVector<Double> ret = new ColumnVector<>(values.length, OperatorInterface.DOUBLE_OPERATOR_INTERFACE);
        for(int i = 0; i < values.length; i++){
            ret.set( i, 0, values[i]);
        }
        return ret;
    }

    public static ColumnVector<Operation> generate(Operation ... values){
        ColumnVector<Operation> ret = new ColumnVector<>(values.length, OperatorInterface.OPERATION_INTERFACE);
        for(int i = 0; i < values.length; i++){
            ret.set( i, 0, values[i]);
        }
        return ret;
    }

}
