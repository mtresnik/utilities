package com.resnik.util.math.linear;

import java.util.List;

public interface MatrixInterface<T> {

    int width();

    int height();

    MatrixInterface<T> dot(MatrixInterface<T> other);

    MatrixInterface<T> transpose();

    List<RowVector<T>> getRows();

    List<ColumnVector<T>> getColumns();

    T[][] getElements();

}
