package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class AppFrame extends JFrame {
    JLabel titleLabel;
    JSlider slider;
    JTextField threadTextField1;
    JTextField threadTextField2;
    JButton startThread1Btn;
    JButton startThread2Btn;

    public AppFrame() {
        super("Lab1");
        setSize(640, 480);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        titleLabel = new JLabel("Welcome!");
        slider = initializeSlider();
        threadTextField1 = new JTextField(30);
        threadTextField2 = new JTextField(30);
        startThread1Btn = new JButton("Start 1");
        startThread2Btn = new JButton("Start 2");

        setLayout(new FlowLayout(FlowLayout.LEFT,150, 10));
        add(titleLabel);
        add(slider);
        add(threadTextField1);
        add(threadTextField2);
        add(startThread1Btn);
        add(startThread2Btn);

        setVisible(true);
    }

    private JSlider initializeSlider() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100 ,50);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = 0; i <= 100; i += 10) {
            labelTable.put(i, new JLabel(i + ""));
        }
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        return slider;
    }

}
