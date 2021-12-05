package com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorEventBus;

public class AdvisorSpinnerEvent {

    int pos;

    public AdvisorSpinnerEvent(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
