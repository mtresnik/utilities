package com.resnik.util.files.kml.style;

import com.resnik.util.files.kml.objects.Icon;

public class IconStyle extends ColorStyle{

    public IconStyle(){
        super("IconStyle");
    }

    public void setScale(float scale){
        this.setInner("scale", Float.toString(scale));
    }

    public void setHeading(float heading){
        this.setInner("heading", Float.toString(heading));
    }

    public void setHotSpot(HotSpot hotSpot){
        this.replaceChild(hotSpot);
    }

    public void setIcon(Icon icon){
        this.replaceChild(icon);
    }

}
