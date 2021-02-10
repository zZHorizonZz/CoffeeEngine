package com.horizon.engine.graphics.light;

import com.horizon.engine.GameEngine;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.light.Light;
import com.horizon.engine.component.component.light.PointLightComponent;
import com.horizon.engine.graphics.object.GameObject;
import org.joml.Vector3f;

public class PointLight extends GameObject {

    public PointLight(GameEngine gameEngine, Vector3f color, Vector3f position, float intensity, Light.Attenuation attenuation) {
        super(gameEngine, "Point Light");

        addComponent(new PointLightComponent(color, position, intensity, attenuation));
    }

    public PointLight(GameEngine gameEngine, Vector3f color, Vector3f position, float intensity) {
        super(gameEngine, "Point Light");

        addComponent(new PointLightComponent(color, position, intensity));
    }

    @Override
    public void update() {

    }

    public PointLightComponent getPointLight(){
        return (PointLightComponent) getComponent(ComponentType.LIGHT);
    }
}
