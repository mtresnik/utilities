package com.resnik.util.math.symbo.operations;

import com.resnik.util.math.symbo.Bounds;
import com.resnik.util.math.symbo.ComplexNumber;
import com.resnik.util.math.plot.points.Point3d;
import com.resnik.util.math.plot.Plot3D;
import com.resnik.util.math.plot.elements3d.PlotDataset3D;
import com.resnik.util.math.plot.elements3d.PlotPoly3D;
import com.resnik.util.math.symbo.operations.interfaces.Solvable;
import com.resnik.util.math.symbo.operations.base.Power;
import com.resnik.util.math.symbo.operations.base.Subtraction;
import javafx.scene.paint.Color;

import java.io.PrintWriter;
import java.util.*;

public class Equation implements Solvable {

    public Operation LHS;
    public Operation RHS;
    Map<Variable, Constant> bestGuess;
    double stepSize;
    public static final Double DEFAULT_STEP_SIZE = 0.00005d;
    public int epoch = 1;
    public static final int DEFAULT_EPOCH_TIMEOUT = 300000;
    private PrintWriter output;

    public Equation(Operation LHS, Operation RHS, Map<Variable, Constant> starter, PrintWriter output){
        this(LHS, RHS, starter);
        this.output = output;
    }

    public Equation(Operation LHS, Operation RHS, Map<Variable, Constant> starter){
        this(LHS, RHS);
        for(Map.Entry<Variable, Constant> entry : starter.entrySet()){
            if(bestGuess.containsKey(entry.getKey())){
                bestGuess.replace(entry.getKey(), entry.getValue());
            }
        }
    }

    public Equation(Operation LHS, Operation RHS, PrintWriter output) {
        this.LHS = LHS;
        this.RHS = RHS;
        this.init();
        this.output = output;
    }

    public Equation(Operation LHS, Operation RHS) {
        this.LHS = LHS;
        this.RHS = RHS;
        this.init();
    }

    public void solveAndPlot(){
        if(!this.bestGuess.containsKey(Variable.X) || !this.bestGuess.containsKey(Variable.Y)){
            return;
        }
        this.stepSize = DEFAULT_STEP_SIZE;
        class TempPoint3d extends Point3d{

            public TempPoint3d(ComplexNumber x, ComplexNumber y, ComplexNumber z) {
                super(x, y, z);
            }

            @Override
            public boolean equals(Object o) {
                if (super.equals(o)) return true;
                if(Point3d.dist(this, (Point3d) o).real < 1){
                    return true;
                }
                return false;
            }
        }
        List<Point3d> attempts = new ArrayList<>();

        while(this.error().greaterThan(new Constant(Solvable.DEFAULT_THRESHOLD)) && this.epoch < DEFAULT_EPOCH_TIMEOUT){
            // Draw new best guesses based on change in error for each variable
            ComplexNumber x = this.bestGuess.get(Variable.X).value;
            ComplexNumber y = this.bestGuess.get(Variable.Y).value;
            ComplexNumber z = this.error().value;
            TempPoint3d rep = new TempPoint3d(x,z,y);
            if(!attempts.contains(rep)){
                attempts.add(rep);
            }
            this.descendGradient();
            x = this.bestGuess.get(Variable.X).value;
            y = this.bestGuess.get(Variable.Y).value;
            z = this.error().value;
            rep = new TempPoint3d(x,z,y);
            if(!attempts.contains(rep)){
                attempts.add(rep);
            }
        }
        ComplexNumber x = this.bestGuess.get(Variable.X).value;
        ComplexNumber y = this.bestGuess.get(Variable.Y).value;
        ComplexNumber z = this.error().value;
        TempPoint3d rep = new TempPoint3d(x,z,y);
        attempts.add(rep);
        System.out.println(attempts.size());
        PlotDataset3D dataset3D = new PlotDataset3D(Color.RED, attempts);
        dataset3D.setLines(true);
        PlotPoly3D poly1 = new PlotPoly3D(Color.BLUE, LHS);
        PlotPoly3D poly2 = new PlotPoly3D(Color.GREEN, RHS);

        Plot3D.CartesianPlot plot = new Plot3D.CartesianPlot(dataset3D, poly1, poly2);
        plot.show();
        System.out.println(this.bestGuess);
    }

    private void init(){
        bestGuess = new LinkedHashMap<>();
        for(Variable tempVar : this.getVariables()){
            bestGuess.put(tempVar, new Constant(0.0d));
        }
        stepSize = DEFAULT_STEP_SIZE;
    }

    public static List<Equation> solutions(Equation seed, int n){
        List<Equation> eqList = new ArrayList<Equation>(){
            @Override
            public String toString(){
                class TempStruct{
                    public final double error;
                    public final Map<Variable, Constant> guess;

                    public TempStruct(double error, Map<Variable, Constant> guess) {
                        this.error = error;
                        this.guess = guess;
                    }

                    public TempStruct(Equation eq){
                        this.error = eq.error().value.real;
                        this.guess = eq.bestGuess;
                    }

                    @Override
                    public String toString() {
                        return "{" +
                                "error=" + error +
                                ", guess=" + guess +
                                '}';
                    }
                }
                List<TempStruct> retList = new ArrayList<>();
                for(Equation eq : this){
                    retList.add(new TempStruct(eq));
                }
                return retList.toString();
            }
        };
        seed.solve();
        eqList.add(seed);
        List<Map<Variable, Constant>> starters = seed.generateStarters(n - 1);
        int index = 1;
        for(Map<Variable, Constant> starter : starters){
            Equation tempEq = seed.clone(starter);
            tempEq.solve();
            eqList.add(tempEq);
            index++;
        }
        eqList.sort((equation, t1) -> Double.compare(equation.error().getValue().real, t1.error().getValue().real));
        return eqList;
    }

    public Map<Variable, Constant> solve(double threshold, double stepSize){
        if(this.getVariables().length == 0){
            return Collections.emptyMap();
        }
        this.stepSize = stepSize;
        while(this.error().greaterThan(new Constant(threshold)) && this.epoch < DEFAULT_EPOCH_TIMEOUT){
            // Draw new best guesses based on change in error for each variable
            this.descendGradient();
            if(this.output != null){
                String printString = "";
                for(int i = 0; i < this.getVariables().length; i++){
                    printString += this.bestGuess.get(this.getVariables()[i]).value.real;
                    if(i < this.getVariables().length - 1){
                        printString += "\t";
                    }
                }
                this.output.println(printString);
            }
        }
        return bestGuess;
    }

    @Override
    public Map<Variable, Constant> solve(double threshold) {
        return this.solve(threshold, stepSize);
    }

    public List<Map<Variable, Constant>> generateStarters(int n){
        Set<Map<Variable, Constant>> retSet = new LinkedHashSet<>();
        Constant delta = new Constant(stepSize + 10);

        while (retSet.size() < n && n > 0){
            for(Variable var1 : this.getVariables()) {
                Map<Variable, Constant> currMap = new LinkedHashMap<>(this.bestGuess);
                currMap.replace(var1, this.bestGuess.get(var1).add(new Constant(Math.random()*delta.value.real)).constantRepresentation());
                retSet.add(currMap);
                currMap = new LinkedHashMap<>(this.bestGuess);
                currMap.replace(var1, this.bestGuess.get(var1).subtract(new Constant(Math.random()*delta.value.real)).constantRepresentation());
                retSet.add(currMap);
            }
        }
        List<Map<Variable, Constant>> retList = new ArrayList<>(retSet);
        return retList;
    }

    public Variable[] getVariables() {
        Variable[] lhsVariables = LHS.getVariables();
        Variable[] rhsVariables = RHS.getVariables();
        Set<Variable> retSet = new LinkedHashSet<>();
        for(Variable lhsVar : lhsVariables){
            retSet.add(lhsVar);
        }
        for(Variable rhsVar : rhsVariables){
            retSet.add(rhsVar);
        }
        return retSet.toArray(new Variable[retSet.size()]);
    }

    public static Constant evaluate(Operation side, Map<Variable, Constant> _bestGuess){
        Operation retOperation = side.evaluate(_bestGuess);
        if(retOperation.allConstants()){
            return retOperation.constantRepresentation();
        }
        throw new IllegalStateException();
    }

    public Equation evaluate(){
        return this.evaluate(this.bestGuess);
    }

    public Equation evaluate(Map<Variable, Constant> _bestGuess){
        Operation lhsEval = this.evaluate(LHS, _bestGuess);
        Operation rhsEval = this.evaluate(RHS, _bestGuess);
        return new Equation(lhsEval, rhsEval);
    }

    @Override
    public String toString() {
        return LHS.toString() + "=" + RHS.toString();
    }

    public Equation errorEquation(){
        return new Equation(this.errorExpression(), Constant.ZERO);
    }

    public Operation errorExpression(){
        Subtraction subtraction = new Subtraction(LHS, RHS);
        Power power = new Power(subtraction, Constant.TWO);
        return power;
    }

    public Constant error(){
        return this.error(this.bestGuess);
    }

    public Constant error(Map<Variable, Constant> _bestGuess){
        Operation result = this.errorExpression().evaluate(_bestGuess);
        if(result.allConstants()){
            return result.constantRepresentation();
        }
        throw new IllegalStateException();
    }

    public Map<Variable, Constant> descendGradient(){
        for(Variable var : this.getVariables()){
            Map<Variable, Constant> mockGuess = new LinkedHashMap<>(this.bestGuess);
            mockGuess.replace(var, this.bestGuess.get(var).add(new Constant(stepSize)).constantRepresentation());
            Constant addError = this.error(mockGuess).subtract(this.error()).constantRepresentation();
            if(addError.equals(Constant.NaN)){
                continue;
            }
            if(addError.greaterThan(Constant.ZERO)){
                this.bestGuess.replace(var, this.bestGuess.get(var).subtract(new Constant(stepSize)).constantRepresentation());
            }else if(addError.lessThan(Constant.ZERO)){
                this.bestGuess.replace(var, this.bestGuess.get(var).add(new Constant(stepSize)).constantRepresentation());
            }

        }
        this.epoch++;
        return this.bestGuess;
    }


    public Map<Variable, Constant> getBestGuess() {
        return bestGuess;
    }

    public Equation clone(Map<Variable, Constant> starter){
        return new Equation(this.LHS, this.RHS, starter);
    }

    public Equation clone(Map<Variable, Constant> starter, PrintWriter output){
        return new Equation(this.LHS, this.RHS, starter, output);
    }

    public List<Map<Variable, Double>> satisfies10(){
        Map<Variable, Bounds> varMap = new LinkedHashMap<>();
        for(Variable var : this.getVariables()){
            varMap.put(var, Bounds.DEFAULT_10);
        }
        return satisfies(varMap);
    }

    public List<Map<Variable, Double>> satisfies(Map<Variable, Bounds> varMap){
        return satisfies(varMap, 1000);
    }

    public List<Map<Variable, Double>> satisfies(Map<Variable, Bounds> varMap, int separations){
        return satisfies(varMap, separations, 1e-2);
    }

    public List<Map<Variable, Double>> satisfies(Map<Variable, Bounds> varMap, int separations, double threshold){
        List<Map<Variable, Double>> retList = new ArrayList<>();
        Variable[] vars = this.getVariables();
        for(Variable v : vars){
            if(!varMap.containsKey(v)){
                return retList;
            }
        }
        // Iterate through all variables
        // Plug in all bounds combinations
        for(Variable v1 : vars){
            Bounds b1 = varMap.get(v1);
            List<Double> d1 = b1.toDoubleList(separations);
            for(Double val : d1){
                Operation nLHS = this.LHS.substitute(v1, new Constant(val));
                Operation nRHS = this.RHS.substitute(v1, new Constant(val));
                if(nLHS.allConstants() && nRHS.allConstants()){
                    Map<Variable, Double> valMap = new LinkedHashMap<>();
                    valMap.put(v1, val);
                    Constant diff = nRHS.subtract(nLHS).abs().constantRepresentation();
                    if(diff.getValue().real <= threshold){
                        retList.add(valMap);
                    }
                }else{
                    Map<Variable, Bounds> nVarMap = new LinkedHashMap<>();
                    for(Map.Entry<Variable, Bounds> entry : varMap.entrySet()){
                        if(entry.getKey().equals(v1)){
                            continue;
                        }
                        nVarMap.put(entry.getKey(), entry.getValue());
                    }
                    // Without current variable, so add in
                    List<Map<Variable, Double>> subMap = new Equation(nLHS, nRHS).satisfies(nVarMap, separations, threshold);
                    for(Map<Variable, Double> currSub : subMap){
                        currSub.put(v1, val);
                        retList.add(currSub);
                    }
                }
            }
        }

        return retList;
    }

    private List<Map<Variable, Double>> standardEquation(Map<Variable, Bounds> varMap, int separations){
        Variable[] lhsVars = LHS.getVariables();
        Variable[] rhsVars = RHS.getVariables();
        if(lhsVars.length != 1 && rhsVars.length != 1){
            System.err.println("Non standard equation.");
            return null;
        }
        // Plug in all X values, expect y
        Operation evalSide = null;
        Operation inputSide = null;
        if(LHS instanceof Variable){
            evalSide = LHS;
            inputSide = RHS;
        }else if(RHS instanceof Variable){
            evalSide = RHS;
            inputSide = LHS;
        }else {
            System.err.println("Non standard equation.");
            return null;
        }

        return null;
    }

    public static Equation parse(String inputString){
        if(!inputString.contains("=")){
            return null;
        }
        String[] sides = inputString.split("=");
        Operation left = Operation.parse(sides[0]);
        Operation right = Operation.parse(sides[1]);
        return new Equation(left, right);
    }

}
