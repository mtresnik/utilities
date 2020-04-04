package com.resnik.util.behavior.tasks.common;

import com.resnik.util.behavior.tasks.BehaviorTask;
import com.resnik.util.logger.Log;

public class Common1 extends BehaviorTask {
    @Override
    public boolean execute(Object entity) {
        Log.e("Common1", false);
        return false;
    }
}
