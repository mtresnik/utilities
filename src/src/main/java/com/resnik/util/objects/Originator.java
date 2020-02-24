package com.resnik.util.objects;

public class Originator<T extends State> {

    private T state;

    public Memento<T> createMemento(){
        return new Memento<>(state);
    }

    public void setState(T state){
        this.state = state;
    }

    public void restore(Memento<T> memento){
        this.setState(memento.getState());
    }

}
