package com.resnik.util.math.symbo.logic.operations;

import com.resnik.util.math.symbo.logic.LogicalConstant;
import com.resnik.util.math.symbo.logic.LogicalVariable;

public class Not extends LogicalOperation {

    private LogicalOperation elem;

    public Not(LogicalOperation elem){
        super(elem);
        this.elem = elem;
    }

    @Override
    public LogicalConstant constantRepresentation() {
        if(allConstants() == false){
            return null;
        }
        return new LogicalConstant(!elem.constantRepresentation().getValue());
    }

    @Override
    public LogicalVariable[] getVariables() {
        return new LogicalVariable[0];
    }

    @Override
    public String nonConstantString() {
        return "~" + elem.toString();
    }

    @Override
    public Not generate(LogicalOperation[] ops) {
        if(ops.length == 0){
            return new Not(LogicalConstant.TRUE);
        }
        return new Not(ops[0]);
    }


}
