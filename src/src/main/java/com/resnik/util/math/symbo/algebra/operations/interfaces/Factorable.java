package com.resnik.util.math.symbo.algebra.operations.interfaces;

import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.base.Multiplication;
import com.resnik.util.objects.structures.CountList;

public interface Factorable {

    CountList<Operation> getFactors();

    Multiplication factoredForm();

}
