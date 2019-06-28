package util.math.symbo;

import util.math.ComplexNumber;
import java.util.ArrayList;
import java.util.List;

public class Addition<T extends Operation> extends Operation {

    public Addition(T o_1, T o_2, T... o_n) {
        super(formatSuper2(o_1, o_2, o_n));
    }

    public Addition(T[] o_n) {
        super(o_n);
    }

//    @Override
//    public ComplexNumber evaluate(ComplexNumber x) {
//        ComplexNumber sum = ComplexNumber.ZERO;
//        for (Operation o : this.values) {
//            sum = sum.add(o.evaluate(x));
//        }
//        return sum;
//    }

    @Override
    public Addition getDerivative(Variable dVar) {
        Operation d_1 = this.values[0].getDerivative(dVar);
        Operation d_2 = this.values[1].getDerivative(dVar);
//        System.out.println("val0:" + this.values[0]);
//        System.out.println("d_1:" + d_1);
        Operation[] remainder = (this.values.length > 2 ? new Operation[this.values.length - 2] : new Operation[]{});
        for (int i = 2; i < this.values.length; i++) {
            remainder[i - 2] = this.values[i].getDerivative(dVar);
        }
        return new Addition(d_1, d_2, remainder);
    }

    @Override
    public String nonConstantString() {
        String retString = "(";
        List<Operation> nonConstantList = new ArrayList();
        if (allConstants()) {
            return this.constantRepresentation().toString();
        }
        List<Constant> constantList = new ArrayList();
        for (Operation o : this.values) {
            if (o.toString().equals(Constant.ZERO.toString())) {
                continue;
            }
            if (o.allConstants() || o instanceof Constant) {
                constantList.add(o.constantRepresentation());
            } else {
                nonConstantList.add(o);
            }
        }
        if (nonConstantList.isEmpty() && constantList.isEmpty()) {
            return Constant.ZERO.toString();
        }
        for (int i = 0; i < nonConstantList.size(); i++) {
            retString += nonConstantList.get(i).toString();
            if (i < nonConstantList.size() - 1) {
                retString += " + ";
            }
        }
        if (constantList.isEmpty() == false) {
            ComplexNumber sum = ComplexNumber.ZERO;
            for (int i = 0; i < constantList.size(); i++) {
                sum = sum.add(constantList.get(i).value);
            }
            retString += " + " + new Constant(sum);
        }
        retString += ")";
        return retString;
    }

    public Addition generate(Operation[] ops) {
        return new Addition(ops);
    }

    @Override
    public Constant constantRepresentation() {
        if(allConstants() == false){
            return Constant.NaN;
        }
        Constant retConstant = Constant.ZERO;
        for (int i = 0; i < this.values.length; i++) {
            retConstant = new Constant(retConstant.value.add(this.values[i].constantRepresentation().value));
        }
        return retConstant;
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        Operation d_1 = this.values[0].getIntegral(dVar);
        Operation d_2 = this.values[1].getIntegral(dVar);
        Operation[] remainder = (this.values.length > 2 ? new Operation[this.values.length - 2] : new Operation[]{});
        for (int i = 2; i < this.values.length; i++) {
            remainder[i - 2] = this.values[i].getIntegral(dVar);
        }
        return new Addition(d_1, d_2, remainder);
    }

}
