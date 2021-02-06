package com.horizon.engine.event.event;

import com.horizon.engine.event.data.Event;
import lombok.Getter;

public class ScreenResizeEvent extends Event {

    @Getter private final int width;
    @Getter private final int height;

    public ScreenResizeEvent(int width, int height){
        super("Screen Resize Event");
        this.width = width;
        this.height = height;
    }
}
