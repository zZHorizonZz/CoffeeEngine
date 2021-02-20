package com.horizon.engine.graphics.object.terrain;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.Color;
import com.horizon.engine.common.ColorPalette;
import com.horizon.engine.common.UtilDataStore;
import com.horizon.engine.common.UtilNormalGenerator;
import com.horizon.engine.common.random.PerlinNoise;
import com.horizon.engine.component.component.generator.BiomeGenerator;
import com.horizon.engine.component.component.mesh.TerrainMesh;
import com.horizon.engine.debug.Debugger;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.object.GameObject;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Map;

public class Terrain extends GameObject {

    @Getter private PerlinNoise heightNoise;
    @Getter private BiomeGenerator biomeGenerator;

    @Getter private final int xSize;
    @Getter private final int zSize;

    protected int xVertexCount;
    protected int zVertexCount;

    private final float[][] heightMap;

    @Getter private final float size = 10.0f;
    @Getter private final Map<Vector2f, TerrainSquare> terrainSquareMap = new HashMap<>();

    public Terrain(GameEngine gameEngine, String terrainName, int xSize, int zSize) {
        this(gameEngine, terrainName, xSize, zSize, new PerlinNoise(5f, 3, 0.3f, 589454546), new ColorPalette(new Color[]{new Color(240.0f, 250.0f, 255.0f)}));
    }

    public Terrain(GameEngine gameEngine, String terrainName, int xSize, int zSize, PerlinNoise heightNoise, ColorPalette biomePalette) {
        super(gameEngine, terrainName);

        this.heightNoise = heightNoise;

        this.xSize = xSize;
        this.zSize = zSize;

        this.xVertexCount = xSize * 2;
        this.zVertexCount = zSize * 2;

        this.heightMap = this.heightNoise.generateHeights(xSize);
        this.biomeGenerator = new BiomeGenerator(biomePalette, this);

        Debugger.log("Terrain", "Generating terrain mesh...");

        TerrainMesh terrainMesh = generateTerrain();
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
    public TerrainMesh generateTerrain() {
        float[] vertices = new float[(zVertexCount * xSize) * 9];
        float[] colors = new float[(zVertexCount * xSize) * 8];
        float[] normals = new float[(zVertexCount * xSize) * 8];
        int[] indices = new int[(zVertexCount * xSize) * 3];

        for(int x = 0; x < xSize; x++) {
            for(int z = 0; z < zSize; z++) {
                TerrainSquare square = new TerrainSquare(this, new Vector2f(x, z),
                        new Vector3f(x == 0 ? 0 : size * x,
                                0 + getHeight(x, z),
                                z == 0 ? 0 : size * z),
                        null);

                square.setColor(biomeGenerator.getColors()[square.getSquareId()]);

                storeSquareVertices(vertices, square);
                storeSquareColors(colors, square);
                storeSquareNormals(normals, square);
                storeSquareIndices(indices, square);

                terrainSquareMap.put(square.getSquareIndex(), square);
            }
        }

        return new TerrainMesh(vertices, colors, normals, indices);
    }

    private void storeSquareVertices(float[] vertices, TerrainSquare square) {
        int index = 0;
        int position = square.getSquareId() == 0 ? 0 : square.getSquareId() * 12;

        UtilDataStore.storeVector3f(position, vertices, square.getBottomLeftCorner().getPosition());
        index += 3;

        UtilDataStore.storeVector3f(position + index, vertices, square.getBottomRightCorner().getPosition());
        index += 3;

        UtilDataStore.storeVector3f(position + index, vertices, square.getTopRightCorner().getPosition());
        index += 3;

        UtilDataStore.storeVector3f(position + index, vertices, square.getTopLeftCorner().getPosition());
    }

    private void storeSquareColors(float[] colors, TerrainSquare square) {
        int index = 0;
        int position = square.getSquareId() == 0 ? 0 : square.getSquareId() * 16;

        Vector4f color = square.getColor().toVector4f();

        UtilDataStore.storeVector4f(position, colors, color);
        index += 4;
        UtilDataStore.storeVector4f(position + index, colors, color);
        index += 4;
        UtilDataStore.storeVector4f(position + index, colors, color);
        index += 4;
        UtilDataStore.storeVector4f(position + index, colors, color);
    }

    private void storeSquareNormals(float[] normals, TerrainSquare square) {
        Vector4f normalTopLeft = new Vector4f(UtilNormalGenerator.calculateNormal((int) square.getSquareIndex().x(), (int) square.getSquareIndex().y(), heightMap), 0);
        Vector4f normalBottomRight = new Vector4f(UtilNormalGenerator.calculateNormal((int) square.getSquareIndex().x(), (int) square.getSquareIndex().y(), heightMap), 0);

        int index = 0;
        int position = square.getSquareId() == 0 ? 0 : square.getSquareId() * 8;

        UtilDataStore.storeVector4f(position, normals, normalBottomRight);
        index += 4;
        UtilDataStore.storeVector4f(position + index, normals, normalTopLeft);
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
        return heightMap[x][z];
    }
}
