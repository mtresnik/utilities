package com.resnik.util.behavior;

import java.util.List;

public abstract class BehaviorNodeCollection implements BehaviorNode {

    List<String> children;

    public BehaviorNodeCollection(List<String> children) {
        this.children = children;
    }

}
