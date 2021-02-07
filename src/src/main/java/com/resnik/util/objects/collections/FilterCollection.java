package com.resnik.util.objects.collections;

import com.resnik.util.objects.Filter;

import java.util.Collection;

public interface FilterCollection<T> extends Collection<T> {

    FilterCollection<T> apply(Filter<T> filter);

}
