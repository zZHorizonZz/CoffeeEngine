package com.horizon.engine.event.event;

import com.horizon.engine.event.data.Event;
import lombok.Getter;

public class MouseClickEvent extends Event {

    @Getter private int clickType;

    public MouseClickEvent(int clickType) {
        super("Mouse Click Event");
    }
}
