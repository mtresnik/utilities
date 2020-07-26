package com.resnik.util.math.symbo.logic;

import com.resnik.util.math.symbo.logic.operations.LogicalOperation;

import java.util.Objects;

public class LogicalVariable extends LogicalOperation {


    public final String name;

    public static final LogicalVariable P = new LogicalVariable("P");
    public static final LogicalVariable Q = new LogicalVariable("Q");

    public static final LogicalVariable A = new LogicalVariable("A");
    public static final LogicalVariable B = new LogicalVariable("B");
    public static final LogicalVariable C = new LogicalVariable("C");
    public static final LogicalVariable D = new LogicalVariable("D");
    public static final LogicalVariable X = new LogicalVariable("X");
    public static final LogicalVariable Y = new LogicalVariable("Y");
    public static final LogicalVariable Z = new LogicalVariable("Z");
    public static final LogicalVariable W = new LogicalVariable("W");

    public LogicalVariable(String name) {
        this.name = name;
    }

    @Override
    public LogicalConstant constantRepresentation() {
        return null;
    }

    @Override
    public String nonConstantString() {
        return this.name;
    }

    @Override
    public LogicalVariable generate(LogicalOperation[] ops) {
        return new LogicalVariable(this.name);
    }

    public boolean allConstants(){
        return false;
    }

    @Override
    public LogicalVariable[] getVariables() {
        return new LogicalVariable[]{this};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalVariable that = (LogicalVariable) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
