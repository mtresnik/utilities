package com.resnik.util.objects.structures;

import java.util.*;

public final class CollectionUtils {

    private CollectionUtils(){}

    public static <T> T get(Collection<T> collection, int index){
        Iterator<T> iter = collection.iterator();
        T found = null;
        int count = 0;
        while(iter.hasNext()){
            T curr = iter.next();
            if(count == index){
                return curr;
            }
            count++;
        }
        return found;
    }


    public static <T> Set<T> reverse(Set<T> inputSet){
        List<T> listRep = new ArrayList<>(inputSet);
        Set<T> retSet = new LinkedHashSet<>();
        for(int i = listRep.size() - 1 ; i >= 0; i--){
            retSet.add(listRep.get(i));
        }
        return retSet;
    }

}
