package com.horizon.engine.graphics.data;

import lombok.Data;

public @Data class Vertex {

    private float x;
    private float y;
    private float z;

    public Vertex(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex clone(){
        return new Vertex(x, y, z);
    }
}
