package com.resnik.util.serial.geo.kml.geometry;

import com.resnik.util.serial.geo.kml.KMLElement;
import com.resnik.util.objects.arrays.ArrayUtils;

public class LinearRing extends Geometry {

    public LinearRing(double[] lon, double[] lat, double ... alt) {
        super(new KMLElement("LinearRing"));
        this.setCoordinates(lon, lat, alt);
    }

    public LinearRing(float[] lon, float[] lat, float ... alt) {
        super(new KMLElement("LinearRing"));
        this.setCoordinates(lon, lat, alt);
    }

    public void setTesselate(boolean value){
        this.setInner("tessellate", Integer.toString(value ? 1 : 0));
    }


    public void setCoordinates(double[] lon, double[] lat, double ... alt){
        this.setCoordinates(ArrayUtils.doublesToFloats(lon), ArrayUtils.doublesToFloats(lat), ArrayUtils.doublesToFloats(alt));
    }

    public void setCoordinates(float[] lon, float[] lat, float ... alt){
        if(lon.length != lat.length){
            throw new IllegalArgumentException("Lon and lat must be of the same size");
        }
        String coordString = "";
        for(int i = 0; i < lon.length; i++){
            while(lon[i] < -180){
                lon[i] += 360;
            }
            while(lon[i] > 180){
                lon[i] -= 360;
            }
            while(lat[i] < -90){
                lat[i] += 180;
            }
            while(lat[i] > 90){
                lat[i] -= 180;
            }
            coordString += lon[i] + "," + lat[i];
            float append = i < alt.length ? alt[i] : 0.0f;
            coordString += "," + append;
            if(i < lon.length - 1){
                coordString += "\n";
            }
        }
        this.setInner("coordinates", coordString);
    }

}
