package com.resnik.util.behavior;

public abstract class BehaviorDecorator implements BehaviorNode {

    BehaviorNode child;

    public BehaviorDecorator(BehaviorNode child) {
        this.child = child;
    }

    protected abstract boolean executeDecorator(Object entity);

    @Override
    public boolean execute(Object entity) {
        return executeDecorator(entity) && child.execute(entity);
    }

}
