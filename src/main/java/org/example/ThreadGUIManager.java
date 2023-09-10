package org.example;

public class ThreadGUIManager {
    private Runnable onLowerFinished = null;
    private Runnable onUppedFinished = null;
    private Runnable onCompetitionChange = null;
    void setOnLowerFinished(Runnable toExecute) {
        this.onLowerFinished = toExecute;
    }

    void onLowerFinished() {
        if (onLowerFinished != null)
            onLowerFinished.run();
    }
    void setOnUpperFinished(Runnable toExecute) {
        this.onUppedFinished = toExecute;
    }
    void onUpperFinished() {
        if (onUppedFinished != null)
            onUppedFinished.run();
    }
    void setOnCompetitionChange(Runnable toExecute) {
        this.onCompetitionChange = toExecute;
    }

    void onSliderMove() {
        if (onCompetitionChange != null)
            onCompetitionChange.run();
    }

}
