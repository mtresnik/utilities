package com.resnik.util.math.circuit;

public class Connector {

    public AndGate and(Connector other){
        return new AndGate(this, other);
    }

    public OrGate or(Connector other){
        return new OrGate(this, other);
    }

}
