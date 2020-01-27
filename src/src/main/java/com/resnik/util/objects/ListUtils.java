package com.resnik.util.objects;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public final class ListUtils {

    private ListUtils(){

    }

    /**
     * Adds an element at the index without modifying the input list.
     */
    public static <T> List<T> insert(List<T> inputList, int index, T elem){
        if(index > inputList.size() - 1){
            List<T> retList = new ArrayList<>(inputList);
            retList.add(elem);
            return retList;
        }
        List<T> retList = new ArrayList<>();
        Queue<T> inQueue = new ArrayDeque<>(inputList);
        int count = 0;
        while(!inQueue.isEmpty()){
            if(count == index){
                retList.add(elem);
            }
            retList.add(inQueue.poll());
            count++;
        }
        return retList;
    }

}
