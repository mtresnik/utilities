package com.resnik.util.math.plot;


public abstract class Dimensions {
    
    protected float[] dimRep;
    
    protected Dimensions(float ... dimRep){
        this.dimRep = dimRep;
    }
    
    public float[] getDim(){
        return dimRep;
    }
    
    public void setDim(float ... dimRep){
        this.dimRep = dimRep;
    }
    
    public abstract void updateDim();
    
}
