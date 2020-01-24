package com.resnik.util.math.plot;


public class Dimensions3d extends Dimensions2d{

    protected float depth;
    
    public Dimensions3d(float width, float height, float depth) {
        super(width, height);
        this.depth = depth;
    }

    public float depth() {
        return depth;
    }
    
    public void setDepth(float depth) {
        this.depth = depth;
        this.updateDim();
    }
    
    
    @Override
    public void updateDim() {
        this.setDim(width, height, depth);
    }
    

}
