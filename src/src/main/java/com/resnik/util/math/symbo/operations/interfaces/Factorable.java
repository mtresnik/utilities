package com.resnik.util.math.symbo.operations.interfaces;

import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.base.Multiplication;
import com.resnik.util.objects.structures.CountList;

public interface Factorable {

    CountList<Operation> getFactors();

    Multiplication factoredForm();

}
