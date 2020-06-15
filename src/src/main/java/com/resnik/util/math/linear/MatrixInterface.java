package com.resnik.util.math.linear;

import java.util.List;

public interface MatrixInterface<T> {

    int width();

    int height();

    MatrixInterface dot(MatrixInterface other);

    MatrixInterface transpose();

    List<RowVector> getRows();

    List<ColumnVector> getColumns();

    T[][] getElements();

}
