package org.example;

import javax.swing.*;
import java.util.function.Predicate;

public class BlockingVariableThreadFactory {
    public static Integer semaphore = 1;
    private final static int DELAY = 100;

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
            while (canMove.test(currentValue = slider.getValue())) {
                slider.setValue(currentValue + delta);
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            releaseResource();
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
