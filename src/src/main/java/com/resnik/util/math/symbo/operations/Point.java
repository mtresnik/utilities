package com.resnik.util.math.symbo.operations;

public class Point extends Operation {

    public Point(Operation ... operations){
        super(operations);
    }

    @Override
    public Constant constantRepresentation() {
        return Constant.NaN;
    }

    @Override
    public String nonConstantString() {
        String retString = "(";
        for(int i = 0; i < this.values.length; i++){
            retString += this.values[i];
            if(i < this.values.length - 1){
                retString += ",";
            }
        }
        retString += ")";
        return retString;
    }

    @Override
    public boolean allConstants() {
        return false;
    }

    @Override
    public Operation getDerivative(Variable dVar) {
        Operation[] subtractions = new Operation[this.values.length];
        for(int i = 0; i < this.values.length; i++){
            subtractions[i] = this.values[i].getDerivative(dVar);
        }
        return new Vector(null, subtractions);
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        Operation[] subtractions = new Operation[this.values.length];
        for(int i = 0; i < this.values.length; i++){
            subtractions[i] = this.values[i].getIntegral(dVar);
        }
        return new Point(subtractions);
    }

    @Override
    public Object generate(Operation[] newValues) {
        return new Point(newValues);
    }
}
