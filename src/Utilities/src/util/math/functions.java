package util.math;

public class functions {

    /**
     * Enables the use of two-variable lambda expressions. Example:
     * Function2<Double, Integer, Double> f2 = (x, y) -> {return x / y;};
     *
     * @param <FIRST_TYPE> First variable type.
     * @param <SECOND_TYPE> Second variable type.
     * @param <RET_TYPE> Return variable type.
     */
    @FunctionalInterface
    public static interface Function2<FIRST_TYPE, SECOND_TYPE, RET_TYPE> {

        public RET_TYPE apply(FIRST_TYPE a, SECOND_TYPE b);
    }

    /**
     * Enables the use of three-variable lambda expressions. Example:
     * Function3<Double, Integer, Float, Double> f3 = (x, y, z) -> {return
     * x+y*z;};
     *
     * @param <FIRST_TYPE> First variable type.
     * @param <SECOND_TYPE> Second variable type.
     * @param <THIRD_TYPE> Third variable type.
     * @param <RET_TYPE> Return variable type.
     */
    @FunctionalInterface
    public static interface Function3<FIRST_TYPE, SECOND_TYPE, THIRD_TYPE, RET_TYPE> {

        public RET_TYPE apply(FIRST_TYPE a, SECOND_TYPE b, THIRD_TYPE c);
    }

}
