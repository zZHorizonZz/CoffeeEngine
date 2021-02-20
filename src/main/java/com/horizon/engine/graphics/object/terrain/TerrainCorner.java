package com.horizon.engine.graphics.object.terrain;

import lombok.Data;
import org.joml.Vector2f;
import org.joml.Vector3f;

public @Data class TerrainCorner {

    private final Vector2f squareIndex;
    private final Vector3f position;
    private final int indice;

    public TerrainCorner(Vector2f squareIndex, Vector3f position, int indice) {
        this.squareIndex = squareIndex;
        this.position = position;
        this.indice = indice;
    }

    public TerrainCorner clone() {
        return new TerrainCorner(new Vector2f(squareIndex), new Vector3f(position), indice);
    }
}
