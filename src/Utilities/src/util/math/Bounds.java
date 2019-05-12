package util.math;


public class Bounds {

    public static final Bounds DEFAULT_10 = new Bounds(-10, 10);
    
    public ComplexNumber min, max;

    public Bounds(ComplexNumber min, ComplexNumber max) {
        this.min = min;
        this.max = max;
    }
    
    public Bounds(double min, double max){
        this.min = ComplexNumber.a(min);
        this.max = ComplexNumber.a(max);
    }

    @Override
    public String toString() {
        return "Bounds{" + "min=" + min + ", max=" + max + '}';
    }
    
    
    
    
}
