package com.resnik.util.files.kml.region;

import com.resnik.util.files.kml.KMLElement;
import com.resnik.util.files.kml.KMLNode;

public class LevelOfDetail extends KMLNode {

    public LevelOfDetail() {
        super(new KMLElement("Lod"));
    }

    public void setMinLodPixels(float minLodPixels){
        this.setInner("minLodPixels", Float.toString(minLodPixels));
    }

    public void setMaxLodPixels(float maxLodPixels){
        this.setInner("maxLodPixels", Float.toString(maxLodPixels));
    }

    public void setMinFadeExtent(float minFadeExtent){
        this.setInner("minFadeExtent", Float.toString(minFadeExtent));
    }

    public void setMaxFadeExtent(float maxFadeExtent){
        this.setInner("maxFadeExtent", Float.toString(maxFadeExtent));
    }

}
