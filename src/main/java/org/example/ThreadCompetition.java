package org.example;

public interface ThreadCompetition {
    boolean isRunning();
    void start(int threadIndex);
    void startAll();
    void stop(int threadIndex);
    void stopAll(int threadIndex);
    void setThread(int index, Thread thread);
}
