package org.example;

import javax.swing.*;
import java.util.function.Predicate;

public class BlockingVariableThreadFactory {
    private final static int DELAY = 100;
    public static int semaphore = 1;
    synchronized void acquireResource() {
        while (semaphore == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        semaphore--;
    }
    synchronized void releaseResource() {
        semaphore++;
        notify();
    }

    private Thread getThread(JSlider slider, Predicate<Integer> canMove, int delta, int priority) {
        Thread thread = new Thread(()->{
            acquireResource();
            int currentValue;
            try {
                while (canMove.test(currentValue = slider.getValue())) {
                    slider.setValue(currentValue + delta);
                    try {
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        return;
                    }
                    if (Thread.interrupted()) {
                        return;
                    }
                }
            } finally {
                releaseResource();
            }
        });
        thread.setPriority(priority);
        return thread;
    }
    public Thread getUpperThread(JSlider slider, int target, int priority) {
        return getThread(slider, t->t>target, -1, priority);
    }
    public Thread getLowerThread(JSlider slider, int target, int priority) {
        return getThread(slider, t->t<target, 1, priority);
    }
}
