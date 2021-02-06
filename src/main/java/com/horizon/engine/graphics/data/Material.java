package com.horizon.engine.graphics.data;

import com.horizon.engine.common.Color;
import lombok.Data;
import org.joml.Vector4f;

public @Data class Material {

    private static final Color DEFAULT_COLOUR = new Color(255.0f, 255.0f, 255.0f);

    private Color ambientColour;
    private Color diffuseColour;
    private Color specularColour;

    private float reflectance;

    private Texture texture;

    public Material() {
        this.ambientColour = DEFAULT_COLOUR;
        this.diffuseColour = DEFAULT_COLOUR;
        this.specularColour = DEFAULT_COLOUR;
        this.texture = null;
        this.reflectance = 0;
    }

    public Material(Color colour) {
        this(colour, colour, colour, null, 0);
    }

    public Material(Color colour, float reflectance) {
        this(colour, colour, colour, null, reflectance);
    }

    public Material(Texture texture) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, 0);
    }

    public Material(Texture texture, float reflectance) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, reflectance);
    }

    public Material(Color ambientColour, Color diffuseColour, Color specularColour, Texture texture, float reflectance) {
        this.ambientColour = ambientColour;
        this.diffuseColour = diffuseColour;
        this.specularColour = specularColour;
        this.texture = texture;
        this.reflectance = reflectance;
    }

    public boolean isTextured() {
        return this.texture != null;
    }
}
