package com.library.patterns;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject class for Observer pattern.
 * Maintains a list of observers and notifies them of changes.
 */
public abstract class Subject {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    protected void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
