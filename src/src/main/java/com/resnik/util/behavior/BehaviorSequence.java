package com.resnik.util.behavior;

import java.util.List;

public class BehaviorSequence extends BehaviorNodeCollection {

    public BehaviorSequence(List<String> children) {
        super(children);
    }

    @Override
    public boolean execute(Object entity) {
        for(String nodeID : this.children){
            BehaviorNode node = BehaviorNodeLoader.getNode(nodeID);
            if(node.fails(entity)){
                return false;
            }
        }
        return true;
    }
}
