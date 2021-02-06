package com.horizon.engine.event.data;

import lombok.Getter;

public abstract class Event {

    @Getter private final String eventName;

    public Event(String eventName){
        this.eventName = eventName;
    }
}
