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

    private final ThreadCompetition threadCompetition;

    public AppFrame(ThreadCompetition threadCompetition) {
        super("Lab1");
        this.threadCompetition = threadCompetition;
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

        return slider;
    }

    void initializeCenterPane() {
        JPanel centerPane = new JPanel(new GridBagLayout());


        threadPriorityField1 = initPriorityField();
        threadPriorityField2 = initPriorityField();
        initStartButton(centerPane);


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

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

    private void initStartButton(JPanel panel) {
        startThreadsBtn = new JButton("Start");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        startThreadsBtn.setPreferredSize(new Dimension(210, 50));
        panel.add(startThreadsBtn, gbc);

        startThreadsBtn.addActionListener(e -> {
            slider.setEnabled(false);
            if (threadCompetition.isRunning()) {
                return;
            }

            threadCompetition.setThread(0, getThread1());
            threadCompetition.setThread(1, getThread2());
            threadCompetition.startAll();

        });
    }

    private Thread getThread1() {
        SliderMoveThreadFactory factory = new SliderMoveThreadFactory();
        return factory.getUpperThread(slider, 10, (int) threadPriorityField1.getValue());
    }
    private Thread getThread2() {
        SliderMoveThreadFactory factory = new SliderMoveThreadFactory();
        return factory.getLowerThread(slider, 90, (int) threadPriorityField2.getValue());
    }

    private JSpinner initPriorityField() {
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 10, 1);
        Dimension preferredSize = new Dimension(100, 40);
        JSpinner jSpinner = new JSpinner(model);
        jSpinner.setPreferredSize(preferredSize);
        return jSpinner;
    }

}
