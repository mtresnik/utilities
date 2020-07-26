package com.resnik.util.math.symbo.logic.operations;

import com.resnik.util.math.symbo.logic.LogicalConstant;
import com.resnik.util.math.symbo.logic.LogicalVariable;

public class LogicalNegation extends LogicalOperation {

    private LogicalOperation elem;

    public LogicalNegation(LogicalOperation elem){
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
    public String nonConstantString() {
        return "!" + elem.toString();
    }

    @Override
    public LogicalNegation generate(LogicalOperation[] ops) {
        if(ops.length == 0){
            return new LogicalNegation(LogicalConstant.TRUE);
        }
        return new LogicalNegation(ops[0]);
    }


}
