package com.horizon.engine.common;

import lombok.Data;
import org.joml.Vector4f;

public @Data class Color {

    public static final Color WHITE = new Color(255.0f, 255.0f, 255.0f);
    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f);
    public static final Color RED = new Color(255.0f, 0f, 0f);
    public static final Color GREEN = new Color(0f, 255.0f, 0f);
    public static final Color BLUE = new Color(0f, 0f, 255.0f);
    public static final Color BACKGROUND = new Color(102.0f, 180.0f, 202.0f);
    public static final Color TRANSPARENT = new Color(0f, 0f, 0f, 0f);

    private Float redValue;
    private Float greenValue;
    private Float blueValue;
    private Float alphaValue;

    public Color(Float red, Float green, Float blue, Float alpha){
        this.redValue = red;
        this.greenValue = green;
        this.blueValue = blue;
        this.alphaValue = alpha;
    }

    public Color(Float red, Float green, Float blue){
        this.redValue = red;
        this.greenValue = green;
        this.blueValue = blue;
        this.alphaValue = 255.0f;
    }

    public Color(Vector4f vector4f){
        this.redValue = vector4f.x() * 255;
        this.greenValue = vector4f.y() * 255;
        this.blueValue = vector4f.z() * 255;
        this.alphaValue = vector4f.w() * 255;
    }

    public Vector4f toVector4f(){
        return new Vector4f(1.0f / 255.0f * redValue, 1.0f / 255.0f * greenValue, 1.0f / 255.0f * blueValue, 1.0f / 255.0f * alphaValue);
    }
}
