package com.resnik.util.files.kml.feature;

import com.resnik.util.files.kml.objects.Icon;
import com.resnik.util.files.kml.KMLElement;
import com.resnik.util.files.kml.KMLNode;

public class GroundOverlay extends Feature {

    public GroundOverlay() {
        super(new KMLElement("GroundOverlay"));
    }

    public void setIcon(Icon icon){
        this.replaceChild(icon);
    }

    public void setAltitude(int value){
        this.setInner("altitude", Integer.toString(value));
    }

    public enum AltitudeMode{
        clampToGround, absolute, clampToSeaFloor
    }

    private void clearAltitudeMode(){
        this.removeChildByTag("altitude");
        this.removeChildByTag("gx:altitudeMode");
    }

    public void setAltitudeMode(AltitudeMode altitudeMode){
        this.clearAltitudeMode();
        if(altitudeMode == AltitudeMode.clampToSeaFloor){
            this.setInner("gx:altitudeMode", altitudeMode.toString());
        }else{
            this.setInner("altitude", altitudeMode.toString());
        }
    }

    public void setLatLonQuad(float lon1, float lat1, float lon2, float lat2, float lon3, float lat3, float lon4, float lat4){
        KMLNode currLatLonQuad = new KMLNode(new KMLElement("gx:LatLonQuad"));
        String coordString = lon1 + "," + lat1 + " " + lon2 + "," + lat2 + " " + lon3 + "," + lat3 + " " + lon4 + "," + lat4;
        currLatLonQuad.setInner("coordinates", coordString);
        this.replaceChild(currLatLonQuad);
    }


    public static void main(String[] args) {
        GroundOverlay go = new GroundOverlay();
        go.setName("gx:LatLonQuad Example");
        go.setLatLonQuad(0,1,2,3,4,5,6,7);
        System.out.println(go);
    }


}
