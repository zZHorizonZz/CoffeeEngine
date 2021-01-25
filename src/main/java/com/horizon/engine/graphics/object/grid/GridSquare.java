package com.horizon.engine.graphics.object.grid;

import lombok.Data;
import org.joml.Vector2f;
import org.joml.Vector3f;

public @Data class GridSquare {

    private int squareId = -1;
    private float size;

    private final Vector2f position;
    private Vector3f[] corners = new Vector3f[]{ };

    public GridSquare(Integer squareId, float x, float z, float size){
        this.position = new Vector2f(x, z);
        this.squareId = squareId;
        this.size = size;

        corners = new Vector3f[]{
                new Vector3f(x, 0, z),
                new Vector3f(x + size, 0, z),
                new Vector3f(x + size, 0, z + size),
                new Vector3f(x, 0, z + size)
        };
    }

    public Vector3f[] getNorthSide(){
        return new Vector3f[] {corners[3], corners[4]};
    }

    public Vector3f[] getSouthSide(){
        return new Vector3f[] {corners[0], corners[1]};
    }

    public Vector3f[] getWestSide(){
        return new Vector3f[] {corners[4], corners[0]};
    }

    public Vector3f[] getEastSide(){
        return new Vector3f[] {corners[1], corners[2]};
    }
}
