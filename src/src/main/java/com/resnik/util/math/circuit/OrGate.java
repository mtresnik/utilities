package com.resnik.util.math.circuit;

public class OrGate extends Component {

    public Connector input1;
    public Connector input2;

    public Connector output;

    public OrGate(Connector input1, Connector input2) {
        this.input1 = input1;
        this.input2 = input2;
    }
}
