package com.resnik.util.files.kml.style;

import com.resnik.util.files.kml.KMLElement;

public class Style extends StyleSelector {

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
        System.out.println(style);
    }

}
