package com.resnik.util.math.linear;

import java.util.Collections;
import java.util.List;

public class RowVector extends Matrix {

    public RowVector(int width) {
        super(1, width, null);
    }

    @Override
    public List<RowVector> getRows() {
        return Collections.singletonList(this);
    }

    @Override
    public List<ColumnVector> getColumns() {
        return null;
    }

}
