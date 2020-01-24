package com.resnik.util.math.symbo.operations.polynomials;


import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Variable;
import com.resnik.util.math.symbo.operations.base.Multiplication;

public class Polynomial3d extends Polynomial{

    public Polynomial3d(Multiplication[] p_n) {
        super(p_n);
    }

    public Polynomial3d(Variable var1, Constant[] coeff1, Variable var2, Constant[] coeff2 ){
        this(generateProducts(var1, coeff1, var2, coeff2));
    }
    
    public static Multiplication[] generateProducts(Variable var1, Constant[] coeff1, Variable var2, Constant[] coeff2){
        Multiplication[] p_1 = Polynomial2d.generateProducts(var1, coeff1);
        Multiplication[] p_2 = Polynomial2d.generateProducts(var2, coeff2);
        Multiplication[] p_union = new Multiplication[p_1.length + p_2.length];
        for (int index = 0; index < p_union.length; index++) {
            if(index < p_1.length){
                p_union[index] = p_1[index];
            }else{
                int newIndex = index - p_1.length;
                p_union[index] = p_2[newIndex];
            }
        }
        return p_union;
    }
    
    
    @Override
    public Polynomial3d generate(Multiplication[] p_n) {
        return new Polynomial3d(p_n);
    }
    

}
