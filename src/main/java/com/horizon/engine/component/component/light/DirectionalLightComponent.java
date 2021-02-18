package com.horizon.engine.component.component.light;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

public class DirectionalLightComponent extends Light {

    @Getter @Setter private Vector3f direction;

    @Getter private final OrthographicCoordinates orthographicCoordinates;
    @Getter private float shadowPosMulti;

    public DirectionalLightComponent(Vector3f color, Vector3f direction, float intensity) {
        super(LightType.DIRECTIONAL_LIGHT);
        this.orthographicCoordinates = new OrthographicCoordinates();
        this.shadowPosMulti = 1;
        this.direction = direction;

        setColor(color);
        setIntensity(intensity);
    }

    public DirectionalLightComponent(DirectionalLightComponent light) {
        super(LightType.DIRECTIONAL_LIGHT);
        this.orthographicCoordinates = light.getOrthographicCoordinates();
        this.shadowPosMulti = light.getShadowPosMulti();
        this.direction = light.getDirection();

        setColor(light.getColor());
        setIntensity(light.getIntensity());
    }

    public void setShadowPosMulti(float shadowPosMulti) {
        this.shadowPosMulti = shadowPosMulti;
    }

    public void setOrthographicCoordinates(float left, float right, float bottom, float top, float near, float far) {
        orthographicCoordinates.left = left;
        orthographicCoordinates.right = right;
        orthographicCoordinates.bottom = bottom;
        orthographicCoordinates.top = top;
        orthographicCoordinates.near = near;
        orthographicCoordinates.far = far;
    }

    public static class OrthographicCoordinates {

        public float left;

        public float right;

        public float bottom;

        public float top;

        public float near;

        public float far;
    }
}
