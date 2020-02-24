package com.resnik.util.objects;

public class Memento<T extends State> {

    private final T state;

    public Memento(T state) {
        this.state = state;
    }

    public T getState() {
        return state;
    }
}
