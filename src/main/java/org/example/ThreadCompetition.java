package org.example;

public interface ThreadCompetition {
    boolean isRunning();
    boolean isRunning(int threadIndex);
    void start(int threadIndex);
    void startAll();
    void stop(int threadIndex);
    void stopAll();
    void setThread(int index, Thread thread);
    void changePriority(int lower, int value);
}
