package com.horizon.engine.model;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.graphics.data.ObjectData;
import com.horizon.engine.graphics.data.Vertex;

import java.util.*;

public class ModelManager extends AbstractManager {

    private final Map<String, ObjectData> objectDataMap = new HashMap<>();

    public ModelManager(GameEngine engine) {
        super(engine, "Model Manager");

        initialize();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void initialize(){
        createCubeObject();
        createEmptySquare();
        createTestModel();
    }

    public void createCubeObject(){
        ObjectData data = new ObjectData("Cube");

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

        data.setVertices(vertices);

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

        data.setTextCoordinates(new float[]{
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
        });

        data.setIndices(new int[]{
                0, 1, 3, 3, 1, 2,
                8, 10, 11, 9, 8, 11,
                12, 13, 7, 5, 12, 7,
                14, 15, 6, 4, 14, 6,
                16, 18, 19, 17, 16, 19,
                4, 6, 7, 5, 4, 7});

        objectDataMap.put(data.getName(), data);
    }

    public void createEmptySquare(){
        ObjectData data = new ObjectData("EmptySquare");

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

    public ObjectData createGridData(int rows, int columns){
        ObjectData objectData = new ObjectData("Grid");

        for(int x = 0; x < rows; x++){
            for(int z = 0; z < columns; z++){

            }
        }

        return objectData;
    }

    public void createSquare(){
        ObjectData data = new ObjectData("Square");

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
        ObjectData data = new ObjectData("Test");

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

    public ObjectData getObjectData(String name){
        if(objectDataMap.containsKey(name))
            return objectDataMap.get(name);

        return new ObjectData("Data Holder");
    }
}
