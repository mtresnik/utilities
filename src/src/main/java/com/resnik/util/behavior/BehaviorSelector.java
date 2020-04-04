package com.resnik.util.behavior;

import java.util.List;

public class BehaviorSelector extends BehaviorNodeCollection {

    public BehaviorSelector(List<String> children) {
        super(children);
    }

    @Override
    public boolean execute(Object entity) {
        for(String nodeID : this.children){
            BehaviorNode node = BehaviorNodeLoader.getNode(nodeID);
            if(node.succeeds(entity)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "BehaviorSelector{" +
                "children=" + children +
                '}';
    }
}
