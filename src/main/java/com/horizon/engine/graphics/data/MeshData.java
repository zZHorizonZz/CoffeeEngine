package com.horizon.engine.graphics.data;

import com.horizon.engine.component.component.Mesh;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

public @Data class MeshData {

    private String name;

    private float[] positions;
    private float[] textCoordinates;
    private float[] normals;
    private int[] indices;

    public MeshData(float[] positions, float[] textureCoordinates, float[] normals, int[] indices) {
        this.positions = positions;
        this.textCoordinates = textureCoordinates;
        this.normals = normals;
        this.indices = indices;
    }

    public Mesh createMesh() {
        return new Mesh(positions, textCoordinates, normals, indices);
    }
}
