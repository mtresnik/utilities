package com.resnik.util.behavior.tasks;


public class Task1 extends BehaviorTask {

    public Task1() {
        super();
    }

    @Override
    public boolean execute(Object entity) {
        if(entity instanceof TaskEntity){
            return true;
        }
        return false;
    }

}
