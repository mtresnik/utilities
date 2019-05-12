package util.math;

public interface Algebraic<
        ADDPARAM extends Algebraic, ADDRET extends Algebraic,
        SUBPARAM extends Algebraic, SUBRET extends Algebraic,
        MULTPARAM extends Algebraic, MULTRET extends Algebraic,
        DIVPARAM extends Algebraic, DIVRET extends Algebraic,
        POWPARAM extends Algebraic, POWRET extends Algebraic> {

    public ADDRET add(ADDPARAM a1);

    public SUBRET subtract(SUBPARAM a1);

    public MULTRET multiply(MULTPARAM a1);

    public DIVRET divide(DIVPARAM a1);

    public POWRET pow(POWPARAM a1);

}
