package com.horizon.engine.model;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.asset.prefab.data.MeshData;

import java.util.*;

public class ModelManager extends AbstractManager {

    private final Map<String, MeshData> objectDataMap = new HashMap<>();

    public ModelManager(GameEngine engine) {
        super(engine, "Model Manager");

        initialize();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void initialize(){
        /*createCubeObject();
        createEmptySquare();
        createTestModel();*/
    }

    /*public void createCubeObject(){
        List<Vertex> vertices = new LinkedList<>(Arrays.asList(
                new Vertex(-0.5f, 0.5f, 0.5f),
                new Vertex(-0.5f, -0.5f, 0.5f),
                new Vertex(0.5f, -0.5f, 0.5f),
                new Vertex(0.5f, 0.5f, 0.5f),

                new Vertex(-0.5f, 0.5f, -0.5f),
                new Vertex(0.5f, 0.5f, -0.5f),
                new Vertex(-0.5f, -0.5f, -0.5f),
                new Vertex(0.5f, -0.5f, -0.5f),

                new Vertex(-0.5f, 0.5f, -0.5f),
                new Vertex(0.5f, 0.5f, -0.5f),
                new Vertex(-0.5f, 0.5f, 0.5f),
                new Vertex(0.5f, 0.5f, 0.5f),

                new Vertex(0.5f, 0.5f, 0.5f),
                new Vertex(0.5f, -0.5f, 0.5f),
                new Vertex(-0.5f, 0.5f, 0.5f),
                new Vertex(-0.5f, -0.5f, 0.5f),

                new Vertex(-0.5f, -0.5f, -0.5f),
                new Vertex(0.5f, -0.5f, -0.5f),
                new Vertex(-0.5f, -0.5f, 0.5f),
                new Vertex(0.5f, -0.5f, 0.5f)));

        /*data.setPositions(new float[] {
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,

                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,

                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,

                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,

                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
        });*/

        /*float[] textureCoordinates = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.0f,
                0.5f, 0.5f,

                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f
        };

        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
                8, 10, 11, 9, 8, 11,
                12, 13, 7, 5, 12, 7,
                14, 15, 6, 4, 14, 6,
                16, 18, 19, 17, 16, 19,
                4, 6, 7, 5, 4, 7};


        objectDataMap.put(data.getName(), data);
    }

    public void createEmptySquare(){
        MeshData data = new MeshData("EmptySquare");

        List<Vertex> vertices = new LinkedList<>(Arrays.asList(
                new Vertex(0.5f, 0.0f, -0.5f),
                new Vertex(-0.5f, 0.0f, -0.5f),

                new Vertex(0.45f, 0.0f, -0.45f),
                new Vertex(-0.45f, 0.0f, -0.45f),

                new Vertex(-0.5f, 0.0f, 0.5f),
                new Vertex(-0.45f, 0.0f, 0.45f),

                new Vertex(0.45f, 0.0f, 0.45f),
                new Vertex(0.5f, 0.0f, 0.5f)));

        data.setVertices(vertices);

        data.setTextCoordinates(new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,

                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,

                0.0f, 0.5f,
                0.5f, 0.5f
        });

        data.setIndices(new int[]{
                0, 1, 2, 2, 1, 3,
                3, 1, 4, 4, 3, 5,
                5, 4, 7, 7, 5, 6,
                6, 7, 0, 0, 2, 6});

        objectDataMap.put(data.getName(), data);
    }

    public MeshData createGridData(int rows, int columns){
        MeshData meshData = new MeshData("Grid");

        for(int x = 0; x < rows; x++){
            for(int z = 0; z < columns; z++){

            }
        }

        return meshData;
    }

    public void createSquare(){
        MeshData data = new MeshData("Square");

        List<Vertex> vertices = new LinkedList<>(Arrays.asList(
                new Vertex(0.5f, 0.0f, -0.5f),
                new Vertex(-0.5f, 0.0f, -0.5f),
                new Vertex(-0.5f, 0.0f, 0.5f),
                new Vertex(0.5f, 0.0f, 0.5f)));

        data.setVertices(vertices);

        data.setTextCoordinates(new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,

                0.5f, 0.5f,
                0.5f, 0.0f
        });

        data.setIndices(new int[]{
                0, 1, 2, 2, 1, 3});

        objectDataMap.put(data.getName(), data);
    }

    public void createTestModel(){
        MeshData data = new MeshData("Test");

        List<Vertex> vertices = new LinkedList<>(Arrays.asList(
                new Vertex(-0.5f, 0.0f, -0.5f),
                new Vertex(-0.5f, 0.0f, 0.0f),
                new Vertex(0.0f, 0.0f, 0.0f),
                new Vertex(0.0f, 0.0f, -0.5f)));

        data.setVertices(vertices);

        data.setTextCoordinates(new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,

                0.5f, 0.5f,
                0.5f, 0.0f
        });

        data.setIndices(new int[]{
                0, 1, 2, 2, 1, 3});

        objectDataMap.put(data.getName(), data);
    }

    public MeshData getObjectData(String name){
        if(objectDataMap.containsKey(name))
            return objectDataMap.get(name);

        return new MeshData("Data Holder");
    }*/
}
