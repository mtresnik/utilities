package com.resnik.util.math.linear;

import com.resnik.util.math.OperatorInterface;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.text.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Matrix<T> implements MatrixInterface<T>{

    protected final int height, width;
    protected final OperatorInterface<T> operatorInterface;
    protected T[][] elements;

    protected Matrix(int height, int width, OperatorInterface<T> operatorInterface) {
        this.height = height;
        this.width = width;
        this.operatorInterface = operatorInterface;
        T zero = operatorInterface.getZero();
        Class<?> clazz = zero.getClass();
        if(zero instanceof Operation){
            clazz = Operation.class;
        }
        this.elements = (T[][])Array.newInstance(clazz, height, width);
        for(int ROW = 0; ROW < height; ROW++){
            this.elements[ROW] = (T[]) Array.newInstance(clazz, width);
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
    public Matrix<T> dot(MatrixInterface<T> B) {
        int m = this.elements.length, n = this.elements[0].length, o = B.getElements().length, p = B.getElements()[0].length;
        if ((n == 1 && p == 1 || m == 1 && o == 1) && (m > 1 || n > 1) && (o > 1 || p > 1)) {
            B = B.transpose();
            o = B.getElements().length;
            p = B.getElements()[0].length;
        }
        if (n != o) {
            throw new IllegalArgumentException("Matrices are of improper lengths: \n(" + m + "x" + n + ")\t(" + o + "x" + p + ")");
        }
        Matrix<T> retMatrix = new Matrix<T>(m, p, operatorInterface);
        for (int ROW_C = 0; ROW_C < m; ROW_C++) {
            for (int COL_C = 0; COL_C < p; COL_C++) {
                T result = operatorInterface.getZero();
                for (int i = 0; i < n; i++) {
                    T A_elem = this.elements[ROW_C][i];
                    T B_elem = B.getElements()[i][COL_C];
                    result = operatorInterface.add(result, operatorInterface.multiply(A_elem, B_elem));
                }
                retMatrix.set(ROW_C, COL_C, result);
            }
        }
        return retMatrix;
    }

    public Matrix<T> pow(int n){
        Matrix<T> ret = this.clone();
        for(int i = 1; i < n; i++){
            ret = this.clone().dot(ret);
        }
        return ret;
    }

    public Matrix<T> add(Matrix<T> other){
        if(this.width != other.width || this.height != other.height){
            throw new IllegalArgumentException("Dimensions don't match.");
        }
        Matrix<T> ret = new Matrix<>(this.height, this.width, this.operatorInterface);
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++){
                ret.set(i, j, this.operatorInterface.add(other.elements[i][j], this.elements[i][j]));
            }
        }
        return ret;
    }

    public Matrix<T> subtract(Matrix<T> other){
        if(this.width != other.width || this.height != other.height){
            throw new IllegalArgumentException("Dimensions don't match.");
        }
        Matrix<T> ret = new Matrix<>(this.height, this.width, this.operatorInterface);
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++){
                ret.set(i, j, this.operatorInterface.subtract(other.elements[i][j], this.elements[i][j]));
            }
        }
        return ret;
    }

    public Matrix<T> hadamard(Matrix<T> other){
        if(this.height != other.height || this.width != other.width){
            throw new IllegalArgumentException("Dimensions must equal.");
        }
        Matrix<T> ret = new Matrix<>(this.height, this.width, this.operatorInterface);
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++){
                ret.set(i, j, this.operatorInterface.multiply(other.elements[i][j], this.elements[i][j]));
            }
        }
        return ret;
    }

    public Matrix<T> scale(T scalar){
        Matrix<T> ret = new Matrix<>(this.height, this.width, this.operatorInterface);
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++){
                ret.set(i, j, this.operatorInterface.multiply(scalar, this.elements[i][j]));
            }
        }
        return ret;
    }

    public Matrix<T> apply(Function<T, T> function){
        Matrix<T> ret = new Matrix<>(this.height, this.width, this.operatorInterface);
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++){
                ret.set(i, j, function.apply(this.elements[i][j]));
            }
        }
        return ret;
    }

    public RowVector<T> row(int i){
        return this.getRows().get(i);
    }

    public ColumnVector<T> col(int j){
        return this.getColumns().get(j);
    }

    @Override
    public Matrix<T> transpose() {
        Matrix<T> retMatrix = new Matrix<>(this.width, this.height, this.operatorInterface);
        for(int i = 0; i < this.height; i++){
            for (int j = 0; j < this.width; j++) {
                retMatrix.set(j, i, this.elements[i][j]);
            }
        }
        return retMatrix;
    }

    @Override
    public List<RowVector<T>> getRows() {
        List<RowVector<T>> retList = new ArrayList<>();
        for(int i = 0; i < this.elements.length; i++){
            retList.add(new RowVector<T>(Arrays.copyOf(this.elements[i], this.elements[i].length), operatorInterface));
        }
        return retList;
    }

    @Override
    public List<ColumnVector<T>> getColumns() {
        List<ColumnVector<T>> ret = new ArrayList<>();
        for(int col = 0; col < width; col++){
            ColumnVector<T> columnVector = new ColumnVector<>(height, operatorInterface);
            for(int row = 0; row < height; row++){
                columnVector.set(row, 0, elements[row][col]);
            }
            ret.add(columnVector);
        }
        return ret;
    }

    public T sum(){
        T ret = operatorInterface.getZero();
        for(T[] row : this.elements){
            for(T elem : row){
                ret = operatorInterface.add(ret, elem);
            }
        }
        return ret;
    }

    public T product(){
        T ret = operatorInterface.getOne();
        for(T[] row : this.elements){
            for(T elem : row){
                ret = operatorInterface.multiply(ret, elem);
            }
        }
        return ret;
    }

    public T get(int i, int j){
        return this.elements[i][j];
    }

    public void set(int i, int j, T elem){
        this.elements[i][j] = elem;
    }

    @Override
    public T[][] getElements() {
        return this.elements;
    }

    public static Matrix<Double> generate(double[][] elements){
        Matrix<Double> ret = new Matrix<>(elements.length, elements[0].length, OperatorInterface.DOUBLE_OPERATOR_INTERFACE);
        for(int i = 0; i < elements.length; i++){
            for(int j = 0; j < elements[0].length; j++){
                ret.set(i, j, elements[i][j]);
            }
        }
        return ret;
    }

    public static Matrix<Double> generate(int height, int width){
        return new Matrix<>(height, width, OperatorInterface.DOUBLE_OPERATOR_INTERFACE);
    }

    public static Matrix<Operation> generateOperation(Operation[][] elements){
        Matrix<Operation> ret = new Matrix<>(elements.length, elements[0].length, OperatorInterface.OPERATION_INTERFACE);
        for(int i = 0; i < elements.length; i++){
            for(int j = 0; j < elements[0].length; j++){
                ret.set(i, j, elements[i][j]);
            }
        }
        return ret;
    }

    public static Matrix<Operation> operationMatrix(int height, int width){
        return new Matrix<>(height, width, OperatorInterface.OPERATION_INTERFACE);
    }

    public Matrix<T> clone(){
        Matrix<T> ret = new Matrix<>(this.height, this.width, this.operatorInterface);
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++){
                ret.set(i, j, this.operatorInterface.add(operatorInterface.getZero(), this.elements[i][j]));
            }
        }
        return ret;
    }

    public static <T> Matrix<T> rotationMatrix(T theta, OperatorInterface<T> operatorInterface){
        Matrix<T> ret = new Matrix<>(2, 2, operatorInterface);
        ret.set(0,0, operatorInterface.cos(theta));
        ret.set(1, 0, operatorInterface.sin(theta));
        ret.set(0, 1, operatorInterface.negate(operatorInterface.sin(theta)));
        ret.set(1, 1, operatorInterface.cos(theta));
        return ret;
    }

    public boolean isSquare(){
        return height == width;
    }

    public SquareMatrix<T> toSquareMatrix(){
        if(!isSquare()){
            throw new IllegalStateException("Matrix is not square:" + height + "!=" + width);
        }
        SquareMatrix<T> ret = new SquareMatrix<>(this.width, this.operatorInterface);
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++){
                ret.set(i, j, this.operatorInterface.add(operatorInterface.getZero(), this.elements[i][j]));
            }
        }
        return ret;
    }

    public Matrix<T> removeRowCol(int row, int col){
        Matrix<T> ret = new Matrix<>(this.height - 1, this.width - 1, this.operatorInterface);
        for(int i = 0; i < this.height; i++){
            if(i == row){
                continue;
            }
            int toRow = i > row ? i - 1 : i;
            for(int j = 0; j < this.width; j++){
                if(j == col){
                    continue;
                }
                int toCol = j > row ? j - 1: j;
                ret.set(toRow, toCol, this.operatorInterface.add(this.operatorInterface.getZero(), this.elements[i][j]));
            }
        }
        return ret;
    }

    public boolean isColumn(){
        return width == 1;
    }

    public ColumnVector<T> toColumnVector(){
        if(!isColumn()){
            throw new IllegalStateException("Matrix is not a column vector.");
        }
        return getColumns().get(0);
    }

    public boolean isRow(){
        return height == 1;
    }

    public RowVector<T> toRowVector(){
        if(!isRow()){
            throw new IllegalStateException("Matrix is not a row vector.");
        }
        return getRows().get(0);
    }

    public boolean isScalar(){
        return width == 1 && height == 1;
    }

    public T toScalar(){
        if(!isScalar()){
            throw new IllegalStateException("Matrix is not a scalar.");
        }
        return this.get(0,0);
    }

    @Override
    public String toString() {
        String retString = "";
        for (int i = 0; i < elements.length; i++) {
            retString += Arrays.toString(elements[i]);
            if(i < elements.length - 1){
                retString += "\n";
            }
        }
        return retString;
    }
}
