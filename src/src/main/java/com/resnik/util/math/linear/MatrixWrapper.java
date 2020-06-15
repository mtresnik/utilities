package com.resnik.util.math.linear;

import java.util.List;

public class MatrixWrapper implements MatrixInterface{

    private Matrix inner;

    public MatrixWrapper(Matrix inner) {
        this.inner = inner;
    }

    @Override
    public int width() {
        return inner.width();
    }

    @Override
    public int height() {
        return inner.height();
    }

    @Override
    public MatrixInterface dot(MatrixInterface other) {
        return inner.dot(other);
    }

    @Override
    public MatrixInterface transpose() {
        return inner.transpose();
    }

    @Override
    public List<RowVector> getRows() {
        return inner.getRows();
    }

    @Override
    public List<ColumnVector> getColumns() {
        return inner.getColumns();
    }

    @Override
    public Object[][] getElements() {
        return new Object[0][];
    }
}
