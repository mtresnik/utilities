package util.math.symbo;

import util.math.ComplexNumber;
import java.util.ArrayList;
import java.util.List;

public class Multiplication extends Operation {

    public Multiplication(Operation o1, Operation o2, Operation... o_n) {
        super(formatSuper2(o1, o2, o_n));
    }

    public Multiplication(Operation[] ops) {
        super(formatInput(ops));
    }

    public static Operation[] formatInput(Operation[] ops) {
        if (ops.length == 0) {
            return new Operation[]{Constant.ONE, Constant.ONE};
        } else if (ops.length == 1) {
            return new Operation[]{Constant.ONE, ops[0]};
        } else {
            return ops;
        }
    }

    @Override
    public String nonConstantString() {
        String retString = "";
        List<Operation> remaining = new ArrayList();
        Constant coefficient = Constant.ONE;
        for (Operation o : this.values) {
            if (o instanceof Constant || o.allConstants()) {
                Constant o_rep = o.constantRepresentation();
                if (o_rep.value.equals(ComplexNumber.ZERO)) {
                    return Constant.ZERO.toString();
                }
                ComplexNumber newVal = coefficient.value.multiply(o_rep.value);
                coefficient = new Constant(newVal);
            } else {
                remaining.add(o);
            }
        }
        if (coefficient.value.equals(ComplexNumber.ZERO)) {
            return Constant.ZERO.toString();
        }
        if (coefficient.value.equals(ComplexNumber.ONE) == false) {
            List<Operation> tempList = new ArrayList();
            tempList.add(coefficient);
            tempList.addAll(remaining);
            remaining = tempList;
        }
        String doAdd = "";
        for (int i = 0; i < remaining.size(); i++) {
            String currElemStr = remaining.get(i).toString();
            if (currElemStr.equals(Constant.ONE.toString()) || currElemStr.isEmpty()) {
                continue;
            } else {
                retString += doAdd;
            }
            retString += remaining.get(i).toString();
            if (i < remaining.size() - 1) {
                doAdd = " * ";
            }

        }
        return retString;
    }

//    @Override
//    public ComplexNumber evaluate(ComplexNumber x) {
//        ComplexNumber prod = ComplexNumber.ONE;
//        for (Operation o : this.values) {
//            prod = prod.multiply(o.evaluate(x));
//        }
//        return prod;
//    }
    @Override
    public Addition getDerivative(Variable dVar) {
//        System.out.println("this:" + this);
//        System.out.println("dVar:" + dVar);
        Multiplication[] products;
        List<Multiplication> productList = new ArrayList();
        for (int elem = 0; elem < this.values.length; elem++) {
            Operation[] values_copy = new Operation[this.values.length];
            for (int sub_elem = 0; sub_elem < this.values.length; sub_elem++) {
                if (sub_elem == elem) {
                    values_copy[sub_elem] = this.values[sub_elem].getDerivative(dVar);
                } else {
                    values_copy[sub_elem] = this.values[sub_elem];
                }
            }
            Multiplication curr_product = new Multiplication(values_copy);
            if (curr_product.toString().equals(Constant.ZERO.toString()) == false) {
                productList.add(curr_product);
            }
        }
        products = productList.toArray(new Multiplication[productList.size()]);
//        System.out.println("prods:" + Arrays.toString(products));
        Addition retPoly = new Addition(products);
//        System.out.println("retPoly:" + retPoly);
//        System.out.println("elements:" + Arrays.toString(retPoly.getValues()));
//        System.out.println(retPoly.allConstants());
        return retPoly;
    }

    public Multiplication generate(Operation[] ops) {
        return new Multiplication(ops);
    }

    @Override
    public Constant constantRepresentation() {

        if (allConstants() == false) {
            return Constant.NaN;
        }
        Constant retConstant = Constant.ONE;
        for (int i = 0; i < this.values.length; i++) {
            retConstant = new Constant(retConstant.value.multiply(this.values[i].constantRepresentation().value));
        }
        return retConstant;

    }

    @Override
    public Operation getIntegral(Variable dVar) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
