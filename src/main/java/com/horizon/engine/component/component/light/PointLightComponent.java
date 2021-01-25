package com.horizon.engine.component.component.light;

import org.joml.Vector3f;

public class PointLightComponent extends Light {

    public PointLightComponent(Vector3f color, Vector3f position, float intensity) {
        super(LightType.POINT_LIGHT);

        setAttenuation(new Attenuation(1, 0, 0));
        setColor(color);
        setPosition(position);
        setIntensity(intensity);
    }

    public PointLightComponent(Vector3f color, Vector3f position, float intensity, Attenuation attenuation) {
        this(color, position, intensity);
        setAttenuation(attenuation);
    }

    public PointLightComponent(PointLightComponent pointLightComponent) {
        this(new Vector3f(pointLightComponent.getColor()), new Vector3f(pointLightComponent.getPosition()), pointLightComponent.getIntensity(), pointLightComponent.getAttenuation());
    }
}
