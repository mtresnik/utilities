package com.resnik.util.objects;

import com.resnik.util.objects.collections.FilterCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@FunctionalInterface
public interface Filter<T> {

    boolean accept(T obj);

    default Collection<T> apply(Collection<T> collection){
        if(collection instanceof FilterCollection)
            return ((FilterCollection<T>) collection).apply(this);
        List<T> retList = new ArrayList<>();
        collection.forEach(t -> { if(accept(t)){ retList.add(t); }});
        return retList;
    }

    default Filter<T> and(Filter<T> filter){
        return (t) -> accept(t) && filter.accept(t);
    }

    default Filter<T> or(Filter<T> filter){
        return (t) -> accept(t) || filter.accept(t);
    }

}
