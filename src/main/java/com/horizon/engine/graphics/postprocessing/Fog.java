package com.horizon.engine.graphics.postprocessing;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

public class Fog {

    @Getter @Setter private boolean active;
    @Getter @Setter private float density;

    @Getter @Setter private Vector3f colour;

    public static Fog NOFOG = new Fog();

    public Fog() {
        active = false;
        this.colour = new Vector3f(0, 0, 0);
        this.density = 0;
    }

    public Fog(boolean active, Vector3f colour, float density) {
        this.colour = colour;
        this.density = density;
        this.active = active;
    }
}
