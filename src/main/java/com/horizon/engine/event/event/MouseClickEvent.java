package com.horizon.engine.event.event;

import com.horizon.engine.event.data.Event;

public class MouseClickEvent extends Event {

    private int clickType;

    public MouseClickEvent(int clickType) {
        super("Mouse Click Event");
    }
}
