package com.horizon.engine.graphics.object.terrain;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.Color;
import com.horizon.engine.common.UtilDataStore;
import com.horizon.engine.common.UtilMath;
import com.horizon.engine.common.random.PerlinNoise;
import com.horizon.engine.component.component.mesh.Mesh;
import com.horizon.engine.debug.Debugger;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.kingdom_builder.data.GamePalette;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class Terrain extends GameObject {

    @Getter private PerlinNoise perlinNoise;

    @Getter private final int xSize;
    @Getter private final int zSize;

    protected int xVertexCount;
    protected int zVertexCount;

    @Getter private final float size = 1.0f;
    @Getter private final Map<Vector2f, TerrainSquare> terrainSquareMap = new HashMap<>();

    public Terrain(GameEngine gameEngine, String terrainName, int xSize, int zSize) {
        super(gameEngine, terrainName);

        this.perlinNoise = new PerlinNoise(5f, 3, 0.3f, 589454546);

        this.xSize = xSize;
        this.zSize = zSize;

        this.xVertexCount = xSize * 2;
        this.zVertexCount = zSize * 2;

        Debugger.log("Terrain", "Generating terrain mesh...");

        Mesh terrainMesh = generateTerrain();
        terrainMesh.setMaterial(new Material(Color.WHITE));
        addComponent(terrainMesh);

        Debugger.log("Terrain", "Terrain mesh generated.");
        Debugger.log("Terrain", "Vertices: " + terrainMesh.getVertexCount());
        Debugger.log("Terrain", "Indices: " + terrainMesh.getIndices().length);
        Debugger.log("Terrain", "Normals: " + terrainMesh.getNormals().length);
    }

    @Override
    public void update() {

    }

    /**
     * This function returns generated terrain mesh.
     * Heights of the terrain are based on the Perlin noise.
     * @return - Terrain mesh
     */
    public Mesh generateTerrain() {
        float[] vertices = new float[(zVertexCount * xSize) * 9];
        float[] textureCoordinates = new float[(zVertexCount * xSize) * 4];
        float[] normals = new float[(zVertexCount * xSize) * 3];
        int[] indices = new int[(zVertexCount * xSize) * 3];

        for(int x = 0; x < xSize; x++) {
            for(int z = 0; z < zSize; z++) {
                TerrainSquare square = new TerrainSquare(this, new Vector2f(x, z), new Vector3f(x == 0 ? 0 : size * x, 0 + perlinNoise.getHeight(x, z), z == 0 ? 0 : size * z));

                storeSquareVertices(vertices, square);
                storeSquareTexture(textureCoordinates, square);
                storeSquareNormals(normals, square);
                storeSquareIndices(indices, square);

                terrainSquareMap.put(square.getSquareIndex(), square);
            }
        }

        return new Mesh(vertices, textureCoordinates, normals, indices);
    }

    private void storeSquareVertices(float[] vertices, TerrainSquare square) {
        int index = 0;
        int position = square.getSquareId() == 0 ? 0 : square.getSquareId() * 12;

        UtilDataStore.storeCorner(position, vertices, square.getBottomLeftCorner().getPosition());
        index += 3;

        UtilDataStore.storeCorner(position + index, vertices, square.getBottomRightCorner().getPosition());
        index += 3;

        UtilDataStore.storeCorner(position + index, vertices, square.getTopRightCorner().getPosition());
        index += 3;

        UtilDataStore.storeCorner(position + index, vertices, square.getTopLeftCorner().getPosition());
    }

    private void storeSquareTexture(float[] texture, TerrainSquare square) {
        int index = 0;
        int position = square.getSquareId() == 0 ? 0 : square.getSquareId() * 4;

        texture[position + index++] = square.getBottomLeftCorner().getPosition().x();
        texture[position + index++] = square.getBottomLeftCorner().getPosition().z();

        texture[position + index++] = square.getTopRightCorner().getPosition().x();
        texture[position + index] = square.getTopRightCorner().getPosition().z();
    }

    private void storeSquareNormals(float[] normals, TerrainSquare square) {
        Vector3f normalTopLeft = UtilMath.calculateNormal(square.getBottomLeftCorner().getPosition(), square.getBottomRightCorner().getPosition(), square.getTopRightCorner().getPosition());
        Vector3f normalBottomRight = UtilMath.calculateNormal(square.getTopRightCorner().getPosition(), square.getTopLeftCorner().getPosition(), square.getBottomLeftCorner().getPosition());

        int index = 0;
        int position = square.getSquareId() == 0 ? 0 : square.getSquareId() * 6;

        UtilDataStore.storeCorner(position, normals, normalTopLeft);
        index += 3;

        UtilDataStore.storeCorner(position + index, normals, normalBottomRight);
    }

    private void storeSquareIndices(int[] vertices, TerrainSquare square) {
        int index = 0;
        int position = square.getSquareId() == 0 ? 0 : square.getSquareId() * 6;

        vertices[position + index++] = square.getBottomLeftCorner().getIndice();
        vertices[position + index++] = square.getBottomRightCorner().getIndice();
        vertices[position + index++] = square.getTopRightCorner().getIndice();

        vertices[position + index++] = square.getTopRightCorner().getIndice();
        vertices[position + index++] = square.getTopLeftCorner().getIndice();
        vertices[position + index] = square.getBottomLeftCorner().getIndice();

    }

    public float getHeight(int x, int z) {
        return perlinNoise.getHeight(x, z);
    }
}
