package com.resnik.util.math;

import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.base.Power;
import com.resnik.util.math.symbo.algebra.operations.base.SymbolicNegation;
import com.resnik.util.math.symbo.algebra.operations.functions.*;

public interface OperatorInterface<T> {

    T add(T first, T other);

    T subtract(T first, T other);

    T multiply(T first, T other);

    T divide(T first, T other);

    T getZero();

    T getOne();

    T negate(T other);

    T getNaN();

    T getInfinity();

    T sin(T theta);

    T asin(T y);

    T cos(T theta);

    T acos(T x);

    T sqrt(T x);

    OperatorInterface<Integer> INTEGER_OPERATOR_INTERFACE = new OperatorInterface<Integer>() {
        @Override
        public Integer add(Integer first, Integer other) {
            return first + other;
        }

        @Override
        public Integer subtract(Integer first, Integer other) {
            return first - other;
        }

        @Override
        public Integer multiply(Integer first, Integer other) {
            return first * other;
        }

        @Override
        public Integer divide(Integer first, Integer other) {
            return first / other;
        }

        @Override
        public Integer getZero() {
            return 0;
        }

        @Override
        public Integer getOne() {
            return 1;
        }

        @Override
        public Integer negate(Integer other) {
            return - other;
        }

        @Override
        public Integer getNaN() {
            return null;
        }

        @Override
        public Integer getInfinity() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Integer sin(Integer theta) {
            return 0;
        }

        @Override
        public Integer asin(Integer y) {
            return (int) Math.asin(y);
        }

        @Override
        public Integer cos(Integer theta) {
            return 0;
        }

        @Override
        public Integer acos(Integer x) {
            return (int) Math.acos(x);
        }

        @Override
        public Integer sqrt(Integer x) {
            return (int) Math.sqrt(x);
        }
    };


    OperatorInterface<Double> DOUBLE_OPERATOR_INTERFACE = new OperatorInterface<Double>() {

        @Override
        public Double add(Double first, Double other) {
            return first + other;
        }

        @Override
        public Double subtract(Double first, Double other) {
            return first - other;
        }

        @Override
        public Double multiply(Double first, Double other) {
            return first * other;
        }

        @Override
        public Double divide(Double first, Double other) {
            return first / other;
        }

        @Override
        public Double getZero() {
            return 0.0;
        }

        @Override
        public Double getOne() {
            return 1.0;
        }

        @Override
        public Double negate(Double other) {
            return  - other;
        }

        @Override
        public Double getNaN() {
            return Double.NaN;
        }

        @Override
        public Double getInfinity() {
            return Double.POSITIVE_INFINITY;
        }

        @Override
        public Double sin(Double theta) {
            return Math.sin(theta);
        }

        @Override
        public Double asin(Double y) {
            return Math.asin(y);
        }

        @Override
        public Double cos(Double theta) {
            return Math.cos(theta);
        }

        @Override
        public Double acos(Double x) {
            return Math.acos(x);
        }

        @Override
        public Double sqrt(Double x) {
            return Math.sqrt(x);
        }


    };

    OperatorInterface<Operation> OPERATION_INTERFACE = new OperatorInterface<Operation>() {
        @Override
        public Operation add(Operation first, Operation other) {
            return first.add(other);
        }

        @Override
        public Operation subtract(Operation first, Operation other) {
            return first.subtract(other);
        }

        @Override
        public Operation multiply(Operation first, Operation other) {
            return first.multiply(other);
        }

        @Override
        public Operation divide(Operation first, Operation other) {
            return first.divide(other);
        }

        @Override
        public Operation getZero() {
            return Constant.ZERO;
        }

        @Override
        public Operation getOne() {
            return Constant.ONE;
        }

        @Override
        public Operation getNaN() {
            return Constant.NaN;
        }

        @Override
        public Operation getInfinity() {
            return Constant.INFINITY;
        }

        @Override
        public Operation sin(Operation theta) {
            return new Sine(theta);
        }

        @Override
        public Operation asin(Operation y) {
            return new ArcSine(y);
        }

        @Override
        public Operation cos(Operation theta) {
            return new Cosine(theta);
        }

        @Override
        public Operation acos(Operation x) {
            return new ArcCosine(x);
        }

        @Override
        public Operation sqrt(Operation x) {
            return new Power(x, Constant.ONE_HALF);
        }

        @Override
        public Operation negate(Operation other) {
            return new SymbolicNegation(other);
        }
    };

}
