package org.example;

public class SimpleThreadCompetition implements ThreadCompetition {
    private final Thread[] threads = new Thread[2];
    @Override
    public boolean isRunning() {
        for (Thread thread : threads) {
            if (thread != null && thread.isAlive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void start(int threadIndex) {
        Thread thread = threads[threadIndex];
        if (thread != null) {
            thread.start();
        }
    }

    @Override
    public void startAll() {
        for (Thread thread : threads) {
            if (thread != null) {
                thread.start();
            }
        }
    }

    @Override
    public void stop(int threadIndex) {
        Thread thread = threads[threadIndex];
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void stopAll(int threadIndex) {
        for (Thread thread : threads) {
            if (thread != null) {
                thread.interrupt();
            }
        }
    }

    @Override
    public void setThread(int index, Thread thread) {
        threads[index] = thread;
    }
}
