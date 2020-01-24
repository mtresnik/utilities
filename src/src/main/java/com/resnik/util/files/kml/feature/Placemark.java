package com.resnik.util.files.kml.feature;

import com.resnik.util.files.kml.KMLElement;
import com.resnik.util.files.kml.geometry.Geometry;
import com.resnik.util.objects.structures.tree.TreeNode;

public class Placemark extends Feature {

    public Placemark() {
        super(new KMLElement("Placemark"));
    }

    public void setGeometry(Geometry geometry){
        Geometry toRemove = null;
        for(TreeNode<KMLElement> child : this.getChildren()){
            if(child instanceof Geometry){
                toRemove = (Geometry) child;
                break;
            }
        }
        if(toRemove != null){
            this.getChildren().remove(toRemove);
        }
        this.addChild(geometry);
    }

}
