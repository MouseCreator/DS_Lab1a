package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Hashtable;

public class AppFrame extends JFrame {
    JLabel titleLabel;
    JSlider slider;
    JSpinner threadPriorityField1;
    JSpinner threadPriorityField2;
    JButton startThreadsBtn;

    public AppFrame() {
        super("Lab1");
        setSize(640, 480);
        setMinimumSize(new Dimension(640, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        titleLabel = new JLabel("Welcome!");

        initializeTopPane();
        initializeCenterPane();


        centralizeFrame();
        setVisible(true);
    }

    void initializeTopPane() {
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.red);
        topPanel.setLayout(new GridBagLayout());
        slider = initializeSlider();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int frameWidth = getWidth();
                int newSliderWidth = frameWidth - 2 * 50;

                slider.setPreferredSize(new Dimension(newSliderWidth, slider.getPreferredSize().height));
                topPanel.revalidate();
            }
        });
        topPanel.add(slider);
        add(topPanel, BorderLayout.NORTH);
    }

    private void centralizeFrame() {
        setLocationRelativeTo(null);
    }

    private JSlider initializeSlider() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100 ,50);
        slider.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = 0; i <= 100; i += 10) {
            labelTable.put(i, new JLabel(i + ""));
        }
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        slider.putClientProperty("Slider.paintThumbArrowShape", Boolean.TRUE);

        UIManager.put("Slider.thumb", Color.RED);
        UIManager.put("Slider.track", Color.GREEN);
        UIManager.put("Slider.highlight", Color.BLUE);
        UIManager.put("Slider.tickColor", Color.BLACK);


        return slider;
    }

    void initializeCenterPane() {
        JPanel centerPane = new JPanel(new GridBagLayout());
        centerPane.setBackground(Color.blue);


        threadPriorityField1 = initPriorityField();
        threadPriorityField2 = initPriorityField();
        startThreadsBtn = new JButton("Start");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPane.add(threadPriorityField1, gbc);

        gbc.gridx = 1;
        centerPane.add(threadPriorityField2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        centerPane.add(startThreadsBtn, gbc);

        add(centerPane, BorderLayout.CENTER);
    }
    private JSpinner initPriorityField() {
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 10, 1);
        Dimension preferredSize = new Dimension(100, 30);
        JSpinner jSpinner = new JSpinner(model);
        jSpinner.setPreferredSize(preferredSize);
        return jSpinner;
    }

}
