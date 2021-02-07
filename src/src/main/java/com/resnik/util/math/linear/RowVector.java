package com.resnik.util.math.linear;

import com.resnik.util.math.OperatorInterface;

import java.util.Collections;
import java.util.List;

public class RowVector<T> extends Matrix<T> {

    public RowVector(T[] elements, OperatorInterface<T> operatorInterface){
        super(1, elements.length, operatorInterface);
        this.elements[0] = elements;
    }

    public RowVector(int width, OperatorInterface<T> operatorInterface) {
        super(1, width, operatorInterface);
    }

    @Override
    public List<RowVector<T>> getRows() {
        return Collections.singletonList(this);
    }

    @Override
    public ColumnVector<T> transpose() {
        ColumnVector<T> retMatrix = new ColumnVector<T>(this.width, this.operatorInterface);
        for(int i = 0; i < this.width; i++){
            retMatrix.set(i, 0, this.elements[0][i]);
        }
        return retMatrix;
    }


}
