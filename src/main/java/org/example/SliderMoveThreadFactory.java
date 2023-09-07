package org.example;

import javax.swing.*;
import java.util.function.Predicate;

public class SliderMoveThreadFactory implements SliderThreadFactory {
    private static final Object obj = new Object();
    private static final int DELAY = 100;
    private Thread getThread(JSlider slider, Predicate<Integer> canMove, int delta, int priority) {
        Thread thread = new Thread(()->{
            while (true) {
                synchronized (obj) {
                    int prevValue = slider.getValue();
                    if (canMove.test(prevValue)) {
                        slider.setValue(prevValue + delta);
                    }
                    try {
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
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
