package org.example;

import javax.swing.JSlider;

public interface SliderThreadFactory {
    Thread getUpperThread(int target, int priority);
    Thread getLowerThread(int target, int priority);
}
