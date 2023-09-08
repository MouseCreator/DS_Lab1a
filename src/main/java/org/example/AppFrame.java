package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Hashtable;
import java.util.function.Predicate;

public class AppFrame extends JFrame {
    JLabel titleLabel;
    JSlider slider;
    JSpinner threadPriorityField1;
    JSpinner threadPriorityField2;
    JButton startThreadsBtn;
    JButton stopAllThreadsBtn;
    JButton start1;
    JButton start2;
    JButton stop1;
    JButton stop2;
    Label statusLabel;
    private final ThreadGUIManager threadGUIManager = new ThreadGUIManager();
    BlockingVariableThreadFactory blockingVariableThreadFactory = new BlockingVariableThreadFactory();
    private final ThreadCompetition taskAThreadCompetition;
    private final ThreadCompetition taskBThreadCompetition;

    public AppFrame() {
        super("Lab1");
        this.taskAThreadCompetition = new SimpleThreadCompetition();
        this.taskBThreadCompetition = new SimpleThreadCompetition();
        setSize(640, 480);
        setMinimumSize(new Dimension(640, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        titleLabel = new JLabel("Welcome!");


        initGuiManager();
        initializeTopPane();
        initializeCenterPane();

        add(initBTaskPanel(), BorderLayout.SOUTH);

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


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        statusLabel = new Label("Slider is free");
        statusLabel.setAlignment(Label.CENTER);
        centerPane.add(statusLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        centerPane.add(threadPriorityField1, gbc);

        gbc.gridx = 1;
        centerPane.add(threadPriorityField2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;

        startThreadsBtn = initStartButton();
        centerPane.add(startThreadsBtn, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        stopAllThreadsBtn = initStopBtn();

        centerPane.add(stopAllThreadsBtn, gbc);

        add(centerPane, BorderLayout.CENTER);
    }

    private JButton initStartButton() {
        JButton button = new JButton("Start");
        button.setPreferredSize(new Dimension(210, 50));

        button.addActionListener(e -> {
            if (taskAThreadCompetition.isRunning()) {
                return;
            }
            setTaskBEnabled(false);
            setTaskAEnabled(false);

            taskAThreadCompetition.setThread(SimpleThreadCompetition.UPPER, getThread1());
            taskAThreadCompetition.setThread(SimpleThreadCompetition.LOWER, getThread2());
            taskAThreadCompetition.startAll();

        });
        return button;
    }

    private void setTaskBEnabled(boolean b) {
        start1.setEnabled(b);
        start2.setEnabled(b);
        stop1.setEnabled(b);
        stop2.setEnabled(b);
    }
    private void setTaskAEnabled(boolean b) {
        startThreadsBtn.setEnabled(b);
    }

    private JButton initStopBtn() {
        JButton button = new JButton("Stop");
        button.setPreferredSize(new Dimension(210, 50));

        button.addActionListener(e -> {

            taskAThreadCompetition.stopAll();
            taskBThreadCompetition.stopAll();

            setTaskBEnabled(true);
            setTaskAEnabled(true);

            statusLabel.setText("Slider is free");

        });
        return button;
    }

    private Thread getThread1() {
        SliderMoveThreadFactory factory = new SliderMoveThreadFactory();
        return factory.getUpperThread( 10, (int) threadPriorityField1.getValue());
    }
    private Thread getThread2() {
        SliderMoveThreadFactory factory = new SliderMoveThreadFactory();
        return factory.getLowerThread(90, (int) threadPriorityField2.getValue());
    }

    private JSpinner initPriorityField() {
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 10, 1);
        Dimension preferredSize = new Dimension(100, 40);
        JSpinner jSpinner = new JSpinner(model);
        jSpinner.setPreferredSize(preferredSize);
        return jSpinner;
    }

    private JPanel initBTaskPanel() {

        start1 = new JButton("Start 1");
        start1.addActionListener(e -> runUpperThread());

        start2 = new JButton("Start 2");
        start2.addActionListener(e -> runLowerThread());

        stop1 = new JButton("Stop 1");
        stop1.addActionListener(e -> stopUpperThread());

        stop2 = new JButton("Stop 2");
        stop2.addActionListener(e -> stopLowerThread());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));
        panel.add(start1);
        panel.add(start2);
        panel.add(stop1);
        panel.add(stop2);

        return panel;
    }

    private void runUpperThread() {
        Thread upperThread = blockingVariableThreadFactory.getUpperThread(10, Thread.MIN_PRIORITY);
        taskBThreadCompetition.setThread(SimpleThreadCompetition.UPPER, upperThread);
        taskBThreadCompetition.start(SimpleThreadCompetition.UPPER);
        start1.setEnabled(false);
        stop2.setEnabled(false);
        setTaskAEnabled(false);
    }

    private void runLowerThread() {
        Thread lowerThread = blockingVariableThreadFactory.getLowerThread(90, Thread.MAX_PRIORITY);
        taskBThreadCompetition.setThread(SimpleThreadCompetition.LOWER, lowerThread);
        taskBThreadCompetition.start(SimpleThreadCompetition.LOWER);
        start2.setEnabled(false);
        stop1.setEnabled(false);
        setTaskAEnabled(false);
    }

    private void stopUpperThread() {
        taskBThreadCompetition.stop(SimpleThreadCompetition.UPPER);
    }
    private void stopLowerThread() {
        taskBThreadCompetition.stop(SimpleThreadCompetition.LOWER);
    }

    private void initGuiManager() {
        threadGUIManager.setOnUpperFinished(()-> {
            start1.setEnabled(true);
            stop2.setEnabled(true);

            if (!taskBThreadCompetition.isRunning(SimpleThreadCompetition.LOWER)) {
                setTaskAEnabled(true);
                statusLabel.setText("Slider is free");
            }
        });
        threadGUIManager.setOnLowerFinished(()-> {
            start2.setEnabled(true);
            stop1.setEnabled(true);

            if (!taskBThreadCompetition.isRunning(SimpleThreadCompetition.UPPER)) {
                setTaskAEnabled(true);
                statusLabel.setText("Slider is free");
            }
        });
        threadGUIManager.setOnCompetitionChange(()-> statusLabel.setText("Slider under control of thread: " + Thread.currentThread().getName()));
    }

    private class BlockingVariableThreadFactory implements SliderThreadFactory {
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

        public Thread getUpperThread(int target, int priority) {
            Thread thread = new Thread(()->{
                acquireResource();
                int currentValue;
                threadGUIManager.onCompetitionChange();
                try {
                    while ((currentValue = slider.getValue()) > target) {
                        slider.setValue(currentValue - 1);
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
                    threadGUIManager.onUpperFinished();
                }
            });
            thread.setPriority(priority);
            thread.setName("Thread 1");
            return thread;
        }
        public Thread getLowerThread(int target, int priority) {
            Thread thread = new Thread(()->{
                acquireResource();
                int currentValue;
                threadGUIManager.onCompetitionChange();
                try {
                    while ((currentValue = slider.getValue()) < target) {
                        slider.setValue(currentValue + 1);
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
                    threadGUIManager.onLowerFinished();
                }
            });

            thread.setPriority(priority);
            thread.setName("Thread 2");
            return thread;
        }
    }

    class SliderMoveThreadFactory implements SliderThreadFactory {
        private static final Object obj = new Object();
        private static final int DELAY = 100;
        private Thread getThread(Predicate<Integer> canMove, int delta) {
            return new Thread(()->{
                while (true) {
                    synchronized (obj) {
                        threadGUIManager.onCompetitionChange();
                        int prevValue = slider.getValue();
                        if (canMove.test(prevValue)) {
                            slider.setValue(prevValue + delta);
                        }
                        try {
                            Thread.sleep(DELAY);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }
            });
        }
        public Thread getUpperThread(int target, int priority) {
            Thread thread = getThread(t->t>target, -1);
            thread.setPriority(priority);
            thread.setName("Thread 1");
            return thread;
        }
        public Thread getLowerThread(int target, int priority) {
            Thread thread = getThread(t->t<target, 1);
            thread.setPriority(priority);
            thread.setName("Thread 2");
            return thread;
        }
    }


}
