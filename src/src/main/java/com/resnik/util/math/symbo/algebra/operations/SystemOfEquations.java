package com.resnik.util.math.symbo.algebra.operations;

import com.resnik.util.math.symbo.algebra.operations.interfaces.Solvable;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

@Deprecated
public class SystemOfEquations implements Solvable, Iterable<Equation> {
    @Override
    public Iterator<Equation> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super Equation> consumer) {

    }

    @Override
    public Map<Variable, Constant> solve(double threshold) {
        return null;
    }
}
