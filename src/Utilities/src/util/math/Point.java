package util.math;


public abstract class Point {

    private ComplexNumber[] values;

    public Point(ComplexNumber ... values){
        this.values = values;
    }
    
    public ComplexNumber[] getValues() {
        return values;
    }
    
}
