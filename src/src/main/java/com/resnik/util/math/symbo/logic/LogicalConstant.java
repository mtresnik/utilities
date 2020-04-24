package com.resnik.util.math.symbo.logic;

import com.resnik.util.math.symbo.logic.operations.LogicalOperation;

public class LogicalConstant extends LogicalOperation<LogicalInterface> {


    protected final boolean value;


    public static final LogicalConstant TRUE = new LogicalConstant(true);
    public static final LogicalConstant FALSE = new LogicalConstant(false);

    public LogicalConstant(boolean value){
        this.value = value;
    }

    @Override
    public boolean allConstants(){return true;}

    @Override
    public LogicalConstant constantRepresentation() {
        return this;
    }

    @Override
    public LogicalVariable[] getVariables() {
        return new LogicalVariable[]{};
    }

    @Override
    public String nonConstantString() {
        return Boolean.toString(this.value);
    }

    @Override
    public LogicalConstant generate(LogicalOperation[] ops) {
        return new LogicalConstant(value);
    }

    public boolean getValue(){
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalConstant that = (LogicalConstant) o;
        return value == that.value;
    }


}
