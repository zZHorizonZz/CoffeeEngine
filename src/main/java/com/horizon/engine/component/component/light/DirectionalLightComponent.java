package com.horizon.engine.component.component.light;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

public class DirectionalLightComponent extends Light {

    @Getter @Setter private Vector3f direction;

    public DirectionalLightComponent(Vector3f color, Vector3f direction, float intensity) {
        super(LightType.DIRECTIONAL_LIGHT);
        this.intensity = intensity;

        setColor(color);
        setDirection(direction);
    }

    public DirectionalLightComponent(DirectionalLightComponent light) {
        this(new Vector3f(light.getColor()), new Vector3f(light.getDirection()), light.getIntensity());
    }
}
