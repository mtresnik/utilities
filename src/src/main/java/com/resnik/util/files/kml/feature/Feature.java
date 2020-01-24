package com.resnik.util.files.kml.feature;

import com.resnik.util.files.kml.KMLElement;
import com.resnik.util.files.kml.KMLNode;
import com.resnik.util.files.kml.data.ExtendedData;
import com.resnik.util.files.kml.region.Region;
import com.resnik.util.files.kml.style.StyleSelector;
import com.resnik.util.files.kml.time.TimePrimitive;
import com.resnik.util.files.kml.views.AbstractView;
import com.resnik.util.objects.structures.tree.TreeNode;

public abstract class Feature extends KMLNode {

    public Feature(KMLElement value) {
        super(value);
    }

    public void setVisibility(boolean visibility){
        this.setInner("visibility", Integer.toString(visibility ? 1 : 0));
    }

    private void setOpen(boolean open){
        this.setInner("open", Integer.toString(open ? 1 : 0));
    }

    public void open(){
        this.setOpen(true);
    }

    public void close(){
        this.setOpen(false);
    }

    public boolean isOpen(){
        return "1".equals(this.getInner("open"));
    }

    public void setAtomAuthor(String author){
        this.setInner("atom:author", author);
    }

    public void setAtomLink(String href){
        KMLNode toReplace = new KMLNode(new KMLElement("atom:link"));
        toReplace.getValue().put("href", href);
        this.replaceChild(toReplace);
    }

    public void setAddress(String address){
        this.setInner("address", address);
    }

    public void setXALAddressDetails(String xalAddressDetails){
        this.setInner("xal:AddressDetails", xalAddressDetails);
    }

    public void setPhoneNumber(String phoneNumber){
        this.setInner("phoneNumber", phoneNumber);
    }

    public void setAbstractView(AbstractView view){
        AbstractView toRemove = null;
        for(TreeNode<KMLElement> child : this.getChildren()){
            if(child instanceof AbstractView){
                toRemove = (AbstractView) child;
                break;
            }
        }
        if(toRemove != null){
            this.getChildren().remove(toRemove);
        }
        this.addChild(view);
    }

    public void setTimePrimitive(TimePrimitive timePrimitive){
        TimePrimitive toRemove = null;
        for(TreeNode<KMLElement> child : this.getChildren()){
            if(child instanceof TimePrimitive){
                toRemove = (TimePrimitive) child;
                break;
            }
        }
        if(toRemove != null){
            this.getChildren().remove(toRemove);
        }
        this.addChild(timePrimitive);
    }

    public void setStyleUrl(String styleUrl){
        this.setInner("styleUrl", styleUrl);
    }

    public void setStyleSelector(StyleSelector selector){
        StyleSelector toRemove = null;
        for(TreeNode<KMLElement> child : this.getChildren()){
            if(child instanceof StyleSelector){
                toRemove = (StyleSelector) child;
                break;
            }
        }
        if(toRemove != null){
            this.getChildren().remove(toRemove);
        }
        this.addChild(selector);
    }

    public void setRegion(Region region){
        this.replaceChild(region);
    }


    public void setExtendedData(ExtendedData data){
        this.replaceChild(data);
    }

}
