package com.resnik.util.behavior;

public interface BehaviorNode {

    public boolean execute(Object entity);

    public default boolean succeeds(Object entity){
        return execute(entity);
    }

    public default boolean fails(Object entity){
        return !succeeds(entity);
    }


}
