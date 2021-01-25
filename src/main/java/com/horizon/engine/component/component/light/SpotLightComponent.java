package com.horizon.engine.component.component.light;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

public class SpotLightComponent extends Light {

    @Getter @Setter private Vector3f coneDirection;

    @Getter @Setter private float cutOffAngle;

    public SpotLightComponent(Vector3f color, Vector3f position, float intensity, Vector3f coneDirection, float cutOffAngle) {
        super(LightType.POINT_LIGHT);

        this.coneDirection = coneDirection;
        this.cutOffAngle = cutOffAngle;

        setAttenuation(new Attenuation(1, 0, 0));
        setColor(color);
        setPosition(position);
        setIntensity(intensity);
    }

    public SpotLightComponent(Vector3f color, Vector3f position, float intensity, Attenuation attenuation, Vector3f coneDirection, float cutOffAngle) {
        this(color, position, intensity, coneDirection, cutOffAngle);

        setAttenuation(attenuation);
    }

    public SpotLightComponent(SpotLightComponent spotLightComponent) {
        this(new Vector3f(spotLightComponent.getColor()), new Vector3f(spotLightComponent.getPosition()), spotLightComponent.getIntensity(), spotLightComponent.getAttenuation(), new Vector3f(spotLightComponent.getConeDirection()), 0);

        setCutOffAngle(spotLightComponent.getCutOffAngle());
    }
}
