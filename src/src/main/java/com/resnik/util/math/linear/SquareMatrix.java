package com.resnik.util.math.linear;

import com.resnik.util.math.OperatorInterface;

public class SquareMatrix<T> extends Matrix<T> {

    public SquareMatrix(int size, OperatorInterface<T> operatorInterface) {
        super(size, size, operatorInterface);
    }

    public SquareMatrix<T> removeRowCol(int row, int col){
        return super.removeRowCol(row, col).toSquareMatrix();
    }

    private Matrix<T> useDet(){
        Matrix<T> ret = new Matrix<>(height, width*2 - 1, operatorInterface);
        for(int ROW = 0; ROW < height; ROW++){
            for(int NEW_COL = 0; NEW_COL < 2*width - 1; NEW_COL++){
                int COL = NEW_COL % this.width;
                T elem = this.get(ROW, COL);
                int count = COL + ROW * width;
                if(count % 2 == 1){
                    elem = operatorInterface.negate(elem);
                }
                ret.set(ROW, NEW_COL, elem);
            }
        }
        return ret;
    }

    public T det(){
        Matrix<T> useDet = useDet();
        T sum = operatorInterface.getZero();
        for(int LEFT_COL = 0; LEFT_COL < width; LEFT_COL++){
            T leftProd = operatorInterface.getOne();
            T rightProd = operatorInterface.getOne();
            for(int ROW = 0; ROW < height; ROW++){
                T leftElem = useDet.get(ROW, LEFT_COL + ROW);
                T rightElem = useDet.get(ROW, useDet.width - 1 - LEFT_COL - ROW);
                leftProd = operatorInterface.multiply(leftProd, leftElem);
                rightProd = operatorInterface.multiply(rightProd, rightElem);
            }
            sum = operatorInterface.add(sum, leftProd);
            sum = operatorInterface.subtract(sum, rightProd);
        }
        return sum;
    }

    public static SquareMatrix<Double> randomMatrix(int n, double scale){
        SquareMatrix<Double> ret = new SquareMatrix<>(n, OperatorInterface.DOUBLE_OPERATOR_INTERFACE);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                ret.set(i, j, Math.random() * scale);
            }
        }
        return ret;
    }

}
