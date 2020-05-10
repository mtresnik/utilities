package com.resnik.util.objects.patterns;

import com.resnik.util.objects.State;

import java.util.ArrayList;
import java.util.List;

public class CareTaker<T extends State> {

    private List<Memento<T>> mementos = new ArrayList<>();

    private Originator<T> originator;

    public CareTaker(Originator<T> originator){
        this.originator = originator;
    }

    public void backup(){
        mementos.add(originator.createMemento());
    }

}
