package com.resnik.util.math;

import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.Operation;

public interface OperatorInterface<T> {

    T add(T first, T other);

    T subtract(T first, T other);

    T multiply(T first, T other);

    T divide(T first, T other);

    T getZero();

    T getNaN();

    T getInfinity();

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
        public Integer getNaN() {
            return null;
        }

        @Override
        public Integer getInfinity() {
            return Integer.MAX_VALUE;
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
        public Double getNaN() {
            return Double.NaN;
        }

        @Override
        public Double getInfinity() {
            return Double.POSITIVE_INFINITY;
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
        public Operation getNaN() {
            return Constant.NaN;
        }

        @Override
        public Operation getInfinity() {
            return Constant.INFINITY;
        }
    };

}
