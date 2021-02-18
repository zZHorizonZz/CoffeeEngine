package com.horizon.engine.graphics.light;

import com.horizon.engine.GameEngine;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.light.DirectionalLightComponent;
import com.horizon.engine.graphics.object.GameObject;
import org.joml.Vector3f;

public class DirectionalLight extends GameObject {

    public DirectionalLight(GameEngine gameEngine) {
        super(gameEngine, "Directional Light");

        addComponent(new DirectionalLightComponent(new Vector3f(1, 1, 1), new Vector3f(0, 1.5f, 1), 1.0f));
    }

    @Override
    public void update() {

    }

    public DirectionalLightComponent getDirectionalLight(){
        return (DirectionalLightComponent) getComponent(ComponentType.LIGHT);
    }
}
