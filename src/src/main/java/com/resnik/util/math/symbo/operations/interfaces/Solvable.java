package com.resnik.util.math.symbo.operations.interfaces;

import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Variable;

import java.util.Map;

public interface Solvable {

    public static final double DEFAULT_THRESHOLD = 0.000001d;

    default Map<Variable, Constant> solve(){
        return this.solve(DEFAULT_THRESHOLD);
    }

    Map<Variable, Constant> solve(double threshold);

}
