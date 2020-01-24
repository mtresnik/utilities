package com.resnik.util.objects;

public class FunctionUtils {

    /**
     * Enables the use of two-variable lambda expressions. Example:
     * Function2<Double, Integer, Double> f2 = (x, y) -> {return x / y;};
     *
     * @param <FIRST_TYPE> First variable type.
     * @param <SECOND_TYPE> Second variable type.
     * @param <RET_TYPE> Return variable type.
     */
    @FunctionalInterface
    public interface Function2<FIRST_TYPE, SECOND_TYPE, RET_TYPE> {

        RET_TYPE apply(FIRST_TYPE a, SECOND_TYPE b);
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
    public interface Function3<FIRST_TYPE, SECOND_TYPE, THIRD_TYPE, RET_TYPE> {

        RET_TYPE apply(FIRST_TYPE a, SECOND_TYPE b, THIRD_TYPE c);
    }

    /**
     * Enables the use of four-variable lambda expressions. Example:
     * Function3<Double, Integer, Float, Integer, Double> f3 = (x, y, z, w) -> {return
     * x + y*z + w;};
     *
     * @param <FIRST_TYPE> First variable type.
     * @param <SECOND_TYPE> Second variable type.
     * @param <THIRD_TYPE> Third variable type.
     * @param <FOURTH_TYPE> Third variable type.
     * @param <RET_TYPE> Return variable type.
     */
    @FunctionalInterface
    public interface Function4<FIRST_TYPE, SECOND_TYPE, THIRD_TYPE, FOURTH_TYPE, RET_TYPE> {

        RET_TYPE apply(FIRST_TYPE a, SECOND_TYPE b, THIRD_TYPE c, FOURTH_TYPE d);
    }
    /**
     * Enables the use of four-variable lambda expressions. Example:
     * Function3<Double, Integer, Float, Integer, Double, Double> f3 = (x, y, z, w, a) -> {return
     * x + y*z + w^a;};
     *
     * @param <FIRST_TYPE> First variable type.
     * @param <SECOND_TYPE> Second variable type.
     * @param <THIRD_TYPE> Third variable type.
     * @param <FOURTH_TYPE> Third variable type.
     * @param <FIFTH_TYPE> Third variable type.
     * @param <RET_TYPE> Return variable type.
     */
    @FunctionalInterface
    public interface Function5<FIRST_TYPE, SECOND_TYPE, THIRD_TYPE, FOURTH_TYPE, FIFTH_TYPE, RET_TYPE> {

        RET_TYPE apply(FIRST_TYPE a, SECOND_TYPE b, THIRD_TYPE c, FOURTH_TYPE d, FIFTH_TYPE e);
    }

}
