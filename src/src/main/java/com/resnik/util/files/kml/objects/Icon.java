package com.resnik.util.files.kml.objects;

import com.resnik.util.files.kml.KMLElement;
import com.resnik.util.files.kml.KMLNode;

public class Icon extends KMLNode {

    public Icon(String href){
        super(new KMLElement("Icon"));
        this.setHref(href);
    }

    public Icon(String id, String href){
        this(href);
        this.setID(id);
    }

    public void setHref(String href){
        this.setInner("href", href);
    }

    public void setGX_X(int x){
        this.setInner("gx:x", Integer.toString(x));
    }

    public void setGX_Y(int y){
        this.setInner("gx:y", Integer.toString(y));
    }

    public void setGX_W(int w){
        this.setInner("gx:w", Integer.toString(w));
    }

    public void setGX_H(int h){
        this.setInner("gx:h", Integer.toString(h));
    }

    public void setGX(int x, int y, int w, int h){
        this.setGX_X(x);
        this.setGX_Y(y);
        this.setGX_W(w);
        this.setGX_H(h);
    }

    public enum RefreshMode {
        onChange, onInterval, onExpire
    }

    public void setRefreshMode(RefreshMode refreshMode){
        this.setInner("refreshMode", refreshMode.toString());
    }

    public void setRefreshInterval(int refreshInterval){
        this.setInner("refreshInterval", Integer.toString(refreshInterval));
    }

    public enum ViewRefreshMode{
        never, onStop, onRequest, onRegion
    }

    public void setViewRefreshMode(ViewRefreshMode refreshMode){
        this.setInner("viewRefreshMode", refreshMode.toString());
    }

    public void setViewRefreshTime(float refreshTime){
        this.setInner("viewRefreshTime", Float.toString(refreshTime));
    }

    public void setViewBoundScale(float boundScale){
        this.setInner("viewBoundScale", Float.toString(boundScale));
    }

    public void setViewFormat(String format){
        this.setInner("viewFormat", format);
    }

    public void setHttpQuery(String httpQuery){
        this.setInner("httpQuery", httpQuery);
    }

}
