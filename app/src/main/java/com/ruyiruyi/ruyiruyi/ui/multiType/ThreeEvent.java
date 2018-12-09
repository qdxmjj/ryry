package com.ruyiruyi.ruyiruyi.ui.multiType;

import com.ruyiruyi.ruyiruyi.ui.model.Event;

public class ThreeEvent {
    public Event leftEvent;
    public Event rightOneEvent;
    public Event  rightTwoEvent;

    public ThreeEvent(Event leftEvent, Event rightOneEvent, Event rightTwoEvent) {
        this.leftEvent = leftEvent;
        this.rightOneEvent = rightOneEvent;
        this.rightTwoEvent = rightTwoEvent;
    }

    public Event getLeftEvent() {
        return leftEvent;
    }

    public void setLeftEvent(Event leftEvent) {
        this.leftEvent = leftEvent;
    }

    public Event getRightOneEvent() {
        return rightOneEvent;
    }

    public void setRightOneEvent(Event rightOneEvent) {
        this.rightOneEvent = rightOneEvent;
    }

    public Event getRightTwoEvent() {
        return rightTwoEvent;
    }

    public void setRightTwoEvent(Event rightTwoEvent) {
        this.rightTwoEvent = rightTwoEvent;
    }
}