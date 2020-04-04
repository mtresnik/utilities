package com.resnik.util.behavior.tasks;

import com.resnik.util.behavior.BehaviorNode;
import com.resnik.util.behavior.BehaviorNodeLoader;
import com.resnik.util.logger.Log;
import org.junit.Test;

import java.util.List;

public class TestTasks {

    public static final String TAG = "TestTasks";

    @Test
    public void testTasks(){
        BehaviorNodeLoader.loadAllNodes("src/res/xml/tasks.xml");
        List<BehaviorNode> nodeList = BehaviorNodeLoader.findAllSimilar("Selector3");
        Log.e(TAG, BehaviorNodeLoader.allNames());
        Log.e(TAG, nodeList);
    }

}
