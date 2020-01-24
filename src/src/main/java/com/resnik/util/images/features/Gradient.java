package com.resnik.util.images.features;

public class Gradient {

    public final double G_x, G_y;
    public final double theta, mag;

    public Gradient(double G_x, double G_y, double theta, double mag) {
        this.G_x = G_x;
        this.G_y = G_y;
        this.theta = theta;
        this.mag = mag;
    }

    public double positiveTheta() {
        if (theta < 0) {
            return theta + Math.PI;
        }
        return theta;
    }

    public double positiveDegree() {
        return this.positiveTheta() * 180 / Math.PI;
    }

    @Override
    public String toString() {
        return "Gradient{" + "G_x=" + G_x + ", G_y=" + G_y + ", theta=" + theta + ", mag=" + mag + '}';
    }

}