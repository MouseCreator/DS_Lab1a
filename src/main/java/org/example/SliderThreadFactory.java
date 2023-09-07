package org.example;

import javax.swing.JSlider;

public interface SliderThreadFactory {
    Thread getUpperThread(JSlider slider, int target, int priority);
    Thread getLowerThread(JSlider slider, int target, int priority);
}
