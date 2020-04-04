package com.resnik.util.files.geo.kml.style;

import com.resnik.util.files.geo.kml.KMLElement;
import com.resnik.util.logger.Log;

public class Style extends StyleSelector {

    public static final String TAG = Style.class.getSimpleName();

    public Style(String id) {
        super(new KMLElement("Style"));
        this.getValue().put("id", id);
    }

    public boolean addStyleElement(StyleElement element){
        return this.addChild(element);
    }

    public String getStyleUrl(){
        return "#" + this.getValue().get("id");
    }

    public static void main(String[] args) {
        Style style = new Style("test");
        IconStyle iconStyle = new IconStyle();
        iconStyle.setColor("ffffff");
        HotSpot hotSpot = new HotSpot();
        hotSpot.setX(0.0f);
        hotSpot.setY(0.0f);
        iconStyle.setHotSpot(hotSpot);
        style.addStyleElement(iconStyle);
        Log.v(TAG,style);
    }

}
