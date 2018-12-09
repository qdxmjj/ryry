package com.ruyiruyi.ruyiruyi.ui.multiType;

import com.ruyiruyi.ruyiruyi.ui.model.Event;

/**
 * Created by Lenovo on 2018/12/6.
 */
public class TwoEvent {
    public Event leftEvent;
    public Event rightEvent;

    public TwoEvent(Event leftEvent, Event rightEvent) {
        this.leftEvent = leftEvent;
        this.rightEvent = rightEvent;
    }

    public Event getLeftEvent() {
        return leftEvent;
    }

    public void setLeftEvent(Event leftEvent) {
        this.leftEvent = leftEvent;
    }

    public Event getRightEvent() {
        return rightEvent;
    }

    public void setRightEvent(Event rightEvent) {
        this.rightEvent = rightEvent;
    }
}