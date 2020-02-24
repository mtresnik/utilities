package com.resnik.util.objects;

import java.util.*;

public final class SetUtils {

    public static <T> LinkedHashSet<T> sort(Set<T> inputSet, Comparator<T> comparator){
        List<T> list = new ArrayList<>(inputSet);
        list.sort(comparator);
        LinkedHashSet<T> ret = new LinkedHashSet<>(list);
        return ret;
    }

}
