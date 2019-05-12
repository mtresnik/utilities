package util.math;


public class Point3d extends Point{

    public ComplexNumber x, y, z;

    public Point3d(ComplexNumber x, ComplexNumber y, ComplexNumber z) {
        super(x,y,z);
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z+ ")";
    }
    
    
    
}
