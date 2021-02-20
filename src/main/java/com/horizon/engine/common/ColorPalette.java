package com.horizon.engine.common;

import lombok.Data;

public @Data class ColorPalette {

    private Color[] colors;

    public ColorPalette(Color[] colors) {
        this.colors = colors;
    }

    public void addColor(Color color) {
        Color[] colors = new Color[this.colors.length + 1];
        System.arraycopy(this.colors, 0, colors, 0, this.colors.length);
        colors[this.colors.length] = color;
        this.colors = colors;
    }
}
