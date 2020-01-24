package com.resnik.util.math.symbo;

public interface Algebraic<
        ADDPARAM extends Algebraic, ADDRET extends Algebraic,
        SUBPARAM extends Algebraic, SUBRET extends Algebraic,
        MULTPARAM extends Algebraic, MULTRET extends Algebraic,
        DIVPARAM extends Algebraic, DIVRET extends Algebraic,
        POWPARAM extends Algebraic, POWRET extends Algebraic> {

    ADDRET add(ADDPARAM a1);

    SUBRET subtract(SUBPARAM a1);

    MULTRET multiply(MULTPARAM a1);

    DIVRET divide(DIVPARAM a1);

    POWRET pow(POWPARAM a1);

}
