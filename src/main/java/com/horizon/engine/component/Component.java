package com.horizon.engine.component;

import lombok.Data;

public @Data abstract class Component {

    private ComponentType componentType;

    public Component(ComponentType componentType){
        this.componentType = componentType;
    }

    public abstract void update();
}
