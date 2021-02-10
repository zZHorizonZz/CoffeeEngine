package com.horizon.engine.graphics.light;

import com.horizon.engine.GameEngine;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.light.Light;
import com.horizon.engine.component.component.light.PointLightComponent;
import com.horizon.engine.component.component.light.SpotLightComponent;
import com.horizon.engine.graphics.object.GameObject;
import org.joml.Vector3f;

public class SpotLight extends GameObject {

    public SpotLight(GameEngine gameEngine, Vector3f color, Vector3f position, float intensity, Light.Attenuation attenuation, Vector3f coneDirection, float cutOffAngle) {
        super(gameEngine, "Point Light");

        addComponent(new SpotLightComponent(color, position, intensity, attenuation, coneDirection, cutOffAngle));
    }

    public SpotLight(GameEngine gameEngine, Vector3f color, Vector3f position, float intensity, Vector3f coneDirection, float cutOffAngle) {
        super(gameEngine, "Point Light");

        addComponent(new SpotLightComponent(color, position, intensity, coneDirection, cutOffAngle));
    }

    @Override
    public void update() {

    }

    public SpotLightComponent getSpotLight(){
        return (SpotLightComponent) getComponent(ComponentType.LIGHT);
    }
}
