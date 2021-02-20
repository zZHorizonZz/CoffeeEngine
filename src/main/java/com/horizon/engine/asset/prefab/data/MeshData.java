package com.horizon.engine.asset.prefab.data;

import com.horizon.engine.component.component.mesh.Mesh;
import lombok.Data;

/**
 * MeshData can store data of mesh and eventually create Mesh itself
 * from inserted data. MeshData are automatically generated if we
 * loading model through asset manager. Name is generated automatically
 * from asset manager. MeshData supports classic meshes or rigged meshes for
 * animations.
 *
 * @apiNote Try to avoid creating new MeshData outside of asset manager.
 *
 * @author Horizon
 */
public @Data class MeshData {

    private String name;

    private float[] positions;
    private float[] textCoordinates;
    private float[] normals;
    private int[] indices;

    private int[] jointIndices;
    private float[] weights;

    /**
     * This type of MeshData are used for classic mesh.
     * @param positions Corner positions of vertices. <code>Vector3.x(), Vector3.y(), Vector3.z()</code> are used for these positions.
     * @param textureCoordinates Coordinates on texture map.
     * @param normals Normals coordinates are used for lightning calculations.
     * @param indices This is used by gpu for rendering vertices corners in right order.
     */
    public MeshData(float[] positions, float[] textureCoordinates, float[] normals, int[] indices) {
        this.positions = positions;
        this.textCoordinates = textureCoordinates;
        this.normals = normals;
        this.indices = indices;
    }

    /**
     * This type of MeshData are used for classic mesh.
     * @param positions Corner positions of vertices. <code>Vector3.x(), Vector3.y(), Vector3.z()</code> are used for these positions.
     * @param textureCoordinates Coordinates on texture map.
     * @param normals Normals coordinates are used for lightning calculations.
     * @param indices This is used by gpu for rendering vertices corners in right order.
     * @param jointIndices Joint Indices are used are order in which should be weights connected.
     * @param weights - Are something like bones.
     */
    public MeshData(float[] positions, float[] textureCoordinates, float[] normals, int[] indices, int[] jointIndices, float[] weights) {
        this.positions = positions;
        this.textCoordinates = textureCoordinates;
        this.normals = normals;
        this.indices = indices;

        this.jointIndices = jointIndices;
        this.weights = weights;
    }

    /**
     * This function creates mesh from mesh data. Should not be used outside of
     * the asset manager with current engine data structure because asset manager
     * handling every single mesh and giving them to the rendering systems.
     * @return Mesh instance that is automatically created and inserted into memory.
     */
    public Mesh createMesh() {
        if (jointIndices == null)
            return new Mesh(positions.clone(), textCoordinates.clone(), normals.clone(), indices.clone());
        else
            return new Mesh(positions.clone(), textCoordinates.clone(), normals.clone(), indices.clone(), jointIndices.clone(), weights.clone());
    }
}
