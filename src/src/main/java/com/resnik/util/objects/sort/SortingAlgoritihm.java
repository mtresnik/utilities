package com.resnik.util.objects.sort;

public abstract class SortingAlgoritihm<T extends Comparable<T>> {

    protected SortingListener<T> listener;
    protected T[] elements;

    public SortingAlgoritihm(T[] elements){
        this.elements = elements;
    }

    public abstract T[] sort();

    public void setListener(SortingListener<T> listener) {
        this.listener = listener;
    }
}
