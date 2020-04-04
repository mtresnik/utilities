package com.resnik.util.behavior;

import com.resnik.util.behavior.tasks.BehaviorTask;
import com.resnik.util.files.xml.XMLElement;
import com.resnik.util.files.xml.XMLNode;
import com.resnik.util.files.xml.XMLTree;
import com.resnik.util.files.xml.XMLTreeParser;
import com.resnik.util.logger.Log;
import com.resnik.util.objects.reflection.ReflectionUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BehaviorNodeLoader {

    public static final String TAG = BehaviorNodeLoader.class.getSimpleName();

    public static final String DO_FIRST = "doFirst", DO_LAST = "doLast";
    public static final String PACKAGE_TAG = "package", NAME_TAG = "name", ID_TAG = "id";

    private static final Map<String, BehaviorNode> allNodes = new LinkedHashMap<>();

    public static BehaviorNode getNode(String id){
        return allNodes.get(id);
    }

    static void put(String id, BehaviorNode node){
        allNodes.put(id, node);
    }

    static String similarName(String inputName){
        for(Map.Entry<String, BehaviorNode> nodeEntry : allNodes.entrySet()){
            if(nodeEntry.getKey().endsWith(inputName)){
                return nodeEntry.getKey();
            }
        }
        return null;
    }

    public static List<String> allNames(){
        return new ArrayList<>(allNodes.keySet());
    }

    public static List<BehaviorNode> findAllSimilar(String inputName){
        List<BehaviorNode> ret = new ArrayList<>();
        for(Map.Entry<String, BehaviorNode> nodeEntry : allNodes.entrySet()){
            if(nodeEntry.getKey().endsWith(inputName)){
                ret.add(nodeEntry.getValue());
            }
        }
        return ret;
    }

    private static void evaluateRoot(XMLNode root){
        List<XMLNode> packages = root.findChildrenByName(PACKAGE_TAG);
        BehaviorTaskLoader.loadAllTasks(packages);
        BehaviorSelectorLoader.loadAllSelectors(packages);
        BehaviorSequenceLoader.loadAllSequences(packages);
    }

    public static void loadAllNodes(String fileName){
        try {
            XMLTree tree = XMLTreeParser.fromFileLocation(fileName);
            List<XMLNode> doFirstList = tree.getRoot().findChildrenByName(DO_FIRST);
            for(XMLNode doFirst : doFirstList){
                evaluateRoot(doFirst);
            }
            evaluateRoot(tree.getRoot());
            List<XMLNode> doLastList = tree.getRoot().findChildrenByName(DO_LAST);
            for(XMLNode doLast : doLastList){
                evaluateRoot(doLast);
            }
            Log.v(TAG, allNodes);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }


    public static class BehaviorTaskLoader {

        public static final String TAG = BehaviorTaskLoader.class.getSimpleName();

        private static final String TASK_TAG = "task";

        public static void loadAllTasks(List<XMLNode> packages){
            for(XMLNode p : packages){
                XMLElement packageValue = (XMLElement) p.getValue();
                String packageName = Objects.toString(packageValue.get(NAME_TAG));
                List<XMLNode> taskNodes = p.findAllByName(TASK_TAG);
                for(XMLNode taskNode : taskNodes){
                    XMLElement nodeValue = (XMLElement) taskNode.getValue();
                    Object nameObject = nodeValue.get(NAME_TAG);
                    Object idObject = nodeValue.get(ID_TAG);
                    if(nameObject == null){
                        continue;
                    }
                    try{
                        String taskName = nameObject.toString();
                        BehaviorTaskLoader.loadTask(packageName, taskName);
                    }catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                }
            }
        }

        public static void loadTask(String packageName, String name){
            String className = packageName + "." + name;
            Class clazz = ReflectionUtils.findClass(className);
            if(clazz == null){
               return;
            }
            Constructor[] constructors = clazz.getDeclaredConstructors();
            Constructor found = null;
            for(Constructor constructor : constructors){
                Class[] types = constructor.getParameterTypes();
                if(types.length == 0){
                    found = constructor;
                    break;
                }
            }
            if(found == null){
                return;
            }
            try {
                Object object = found.newInstance();
                if(object instanceof BehaviorTask){
                    put(className, (BehaviorTask) object);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

    public static class BehaviorSelectorLoader {

        public static final String SELECTOR_TAG = "selector", ORDER_TAG = "order";

        public static void loadAllSelectors(List<XMLNode> packages){
            for(XMLNode p : packages){
                XMLElement packageValue = (XMLElement) p.getValue();
                String packageName = Objects.toString(packageValue.get(NAME_TAG));
                List<XMLNode> selectorNodes = p.findAllByName(SELECTOR_TAG);
                for(XMLNode selectorNode : selectorNodes){
                    XMLElement nodeValue = (XMLElement) selectorNode.getValue();
                    Object nameObject = nodeValue.get(NAME_TAG);
                    Object orderObject = nodeValue.get(ORDER_TAG);
                    if(nameObject == null || orderObject == null){
                        continue;
                    }
                    String[] references = orderObject.toString().replaceAll(" ", "").split("\\,");
                    List<String> order = new ArrayList<>();
                    boolean doContinue = false;
                    for(String ref : references){
                        String classRef = packageName + "." + ref;
                        if(!allNodes.containsKey(classRef)){
                            classRef = similarName(ref);
                            if(classRef == null){
                                doContinue = true;
                                break;
                            }
                        }
                        order.add(classRef);
                    }
                    if(doContinue){
                        continue;
                    }
                    try{
                        String selectorName = nameObject.toString();
                        BehaviorSelectorLoader.loadSelector(packageName, selectorName, order);
                    }catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                }
            }
        }

        public static void loadSelector(String packageName, String name, List<String> order){
            String className = packageName + "." + name;
            Class clazz = ReflectionUtils.findClass(className);
            if(clazz == null){
                clazz = BehaviorSelector.class;
            }
            Constructor[] constructors = clazz.getDeclaredConstructors();
            Constructor found = null;
            for(Constructor constructor : constructors){
                Class[] types = constructor.getParameterTypes();
                if(types.length == 1){
                    found = constructor;
                    break;
                }
            }
            if(found == null){
                return;
            }
            try {
                Object object = found.newInstance(order);
                if(object instanceof BehaviorSelector){
                    put(className, (BehaviorSelector) object);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }


    }

    public static class BehaviorSequenceLoader {


        public static void loadAllSequences(List<XMLNode> packages){
        }

        public static void loadSequence(String packageName, String name, int id, List<Integer> order){
            String className = packageName + "." + name;
            Class clazz = ReflectionUtils.findClass(className);
            if(clazz == null){
                return;
            }

        }


    }

    public static class BehaviorDecoratorLoader{
        
    }


}
