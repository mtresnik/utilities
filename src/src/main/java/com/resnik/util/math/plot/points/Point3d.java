package com.resnik.util.math.plot.points;


import com.resnik.util.math.symbo.ComplexNumber;

import java.util.Objects;

public class Point3d extends Point{

    public ComplexNumber x, y, z;

    public Point3d(ComplexNumber x, ComplexNumber y, ComplexNumber z) {
        super(x,y,z);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3d(double x, double y, double z){
        super(ComplexNumber.a(x, y, z));
        this.x = ComplexNumber.a(x);
        this.y = ComplexNumber.a(y);
        this.z = ComplexNumber.a(z);
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z+ ")";
    }

    public static Point3d midpoint(Point3d a, Point3d b){
        return new Point3d(a.x.add(b.x).divide(ComplexNumber.TWO), a.y.add(b.y).divide(ComplexNumber.TWO), a.z.add(b.z).divide(ComplexNumber.TWO));
    }

    public static ComplexNumber dist(Point3d a, Point3d b){
        return a.x.subtract(b.x).pow(2).add(a.y.subtract(b.y).pow(2)).add(a.z.subtract(b.z).pow(2)).pow(0.5);
    }

    public static Point3d diff(Point3d a, Point3d b){
        return new Point3d(b.x.subtract(a.x), b.y.subtract(a.y), b.z.subtract(a.z));
    }

    public static Point3d cross(Point3d a, Point3d b){
        ComplexNumber x = a.y.multiply(b.z).subtract(a.z.multiply(b.y));
        ComplexNumber y = a.z.multiply(b.x).subtract(a.x.multiply(b.z));
        ComplexNumber z = a.x.multiply(b.y).subtract(a.y.multiply(b.y));
        return new Point3d(x,y,z);
    }

    public ComplexNumber magnitude(){
        return x.pow(2).add(y.pow(2)).add(z.pow(2)).pow(0.5);
    }

    public Point3d normalize(){
        ComplexNumber mag = magnitude();
        return new Point3d(x.divide(mag), y.divide(mag), z.divide(mag));
    }

    public static ComplexNumber dot(Point3d a, Point3d b){
        return a.x.multiply(b.x).add(a.y.multiply(b.y)).add(a.z.multiply(b.z));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point3d point3d = (Point3d) o;
        return Objects.equals(x, point3d.x) &&
                Objects.equals(y, point3d.y) &&
                Objects.equals(z, point3d.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
