package com.resnik.util.files.kml.style;

public class BalloonStyle extends StyleElement {

    public BalloonStyle() {
        super("BalloonStyle");
    }

    public void setBGColor(String bgColor){
        this.setInner("bgColor", bgColor);
    }

    public void setTextColor(String textColor){
        this.setInner("textColor", textColor);
    }

    public enum DisplayMode{
        _default, _hide;

        public String toString(){
            return super.toString().replaceAll("_", "");
        }

    }

    public void setDisplayMode(DisplayMode displayMode){
        this.setInner("displayMode", displayMode.toString());
    }


}
