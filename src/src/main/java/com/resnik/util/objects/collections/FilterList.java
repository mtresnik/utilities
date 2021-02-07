package com.resnik.util.objects.collections;

import com.resnik.util.objects.Filter;

import java.util.ArrayList;

public class FilterList<T> extends ArrayList<T> implements FilterCollection<T> {

    @Override
    public FilterList<T> apply(Filter<T> filter) {
        FilterList<T> ret = new FilterList<>();
        this.forEach((t) -> {if(filter.accept(t)) ret.add(t);});
        return ret;
    }

}
