package com.resnik.util.math.symbo.algebra.operations.polynomials;

import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.base.Addition;
import com.resnik.util.math.symbo.algebra.operations.base.Multiplication;

public abstract class Polynomial extends Addition<Multiplication> {

    public Polynomial(Multiplication[] p_n) {
        super(p_n);
    }

    public Polynomial(Operation[] o_n) {
        super(formatOperations(o_n));
    }

    public static Multiplication[] formatOperations(Operation[] o_n) {
        Multiplication[] retArray = new Multiplication[o_n.length];
        for (int i = 0; i < o_n.length; i++) {
            Operation opCurr = o_n[i];
            Multiplication toAdd = new Multiplication(Constant.ONE, opCurr);
            retArray[i] = toAdd;
        }
        return retArray;
    }
    
    public abstract <T extends Polynomial> T generate(Multiplication[] p_n);

}
