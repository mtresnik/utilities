package com.resnik.util.objects.patterns;

import com.resnik.util.objects.State;

public class Memento<T extends State> {

    private final T state;

    public Memento(T state) {
        this.state = state;
    }

    public T getState() {
        return state;
    }
}
