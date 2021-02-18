package com.horizon.engine.asset.prefab.data;

import com.horizon.engine.component.component.mesh.Mesh;
import lombok.Data;

public @Data class MeshData {

    private String name;

    private float[] positions;
    private float[] textCoordinates;
    private float[] normals;
    private int[] indices;

    private int[] jointIndices;
    private float[] weights;

    public MeshData(float[] positions, float[] textureCoordinates, float[] normals, int[] indices) {
        this.positions = positions;
        this.textCoordinates = textureCoordinates;
        this.normals = normals;
        this.indices = indices;
    }

    public MeshData(float[] positions, float[] textureCoordinates, float[] normals, int[] indices, int[] jointIndices, float[] weights) {
        this.positions = positions;
        this.textCoordinates = textureCoordinates;
        this.normals = normals;
        this.indices = indices;

        this.jointIndices = jointIndices;
        this.weights = weights;
    }

    public Mesh createMesh() {
        if (jointIndices == null)
            return new Mesh(positions.clone(), textCoordinates.clone(), normals.clone(), indices.clone());
        else
            return new Mesh(positions.clone(), textCoordinates.clone(), normals.clone(), indices.clone(), jointIndices.clone(), weights.clone());
    }
}
