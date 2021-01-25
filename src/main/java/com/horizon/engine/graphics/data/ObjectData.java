package com.horizon.engine.graphics.data;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

public @Data class ObjectData {

    private final String name;

    private List<Vertex> vertices = new LinkedList<>();

    private float[] positions = new float[] {};
    private float[] textCoordinates = new float[] {};
    private int[] indices = new int[] {};

    public ObjectData(String name){
        this.name = name;
    }

    public List<Vertex> cloneVertices(){
        List<Vertex> vertexList = new LinkedList<>();

        vertices.forEach(vertex -> {
            vertexList.add(vertex.clone());
        });

        return vertexList;
    }
}
