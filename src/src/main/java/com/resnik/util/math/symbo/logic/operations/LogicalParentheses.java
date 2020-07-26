package com.resnik.util.math.symbo.logic.operations;

import com.resnik.util.math.symbo.logic.LogicalConstant;

public class LogicalParentheses extends LogicalOperation {

    private LogicalOperation elem;

    public LogicalParentheses(LogicalOperation operation){
        super(operation);
        this.elem = operation;
    }

    @Override
    public String nonConstantString() {
        return "(" + elem.toString() + ")";
    }

    public LogicalParentheses generate(LogicalOperation[] ops){
        if(ops.length == 0){
            return new LogicalParentheses(LogicalConstant.TRUE);
        }
        return new LogicalParentheses(ops[0]);
    }

    @Override
    public LogicalConstant constantRepresentation() {
        if (elem.allConstants() == false) {
            return null;
        }
        return elem.constantRepresentation();
    }

    public LogicalOperation unWrap(){
        LogicalOperation curr = this;
        while(curr instanceof LogicalParentheses){
            curr = ((LogicalParentheses) curr).elem;
        }
        return curr;
    }

}
