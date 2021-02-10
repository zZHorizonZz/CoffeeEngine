package com.horizon.engine.common;

import com.horizon.engine.graphics.data.Vertex;

import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class UtilModel {

    public static float[] vertexToArray(List<Vertex> vertices){
        float[] array = new float[vertices.size() * 3];

        int position = 0;
        for(Vertex vertex : vertices) {
            array[position] = vertex.getX();
            position++;
            array[position] = vertex.getY();
            position++;
            array[position] = vertex.getZ();
            position++;
        }

        return array;
    }

    public static List<Vertex> arrayToVertex(float[] vertices){
        List<Vertex> list = new LinkedList<Vertex>();

        int position = 0;
        for(int i = 1; i <= vertices.length / 3; i++) {
            Vertex vertex = new Vertex(vertices[position], vertices[position + 1], vertices[position + 2]);
            position += 3;
            list.add(vertex);
        }

        return list;
    }

    public static List<Vertex> upScaleModel(List<Vertex> vertices, float scaleX, float scaleY, float scaleZ){
        for(Vertex vertex : vertices){
            vertex.setX(vertex.getX() * scaleX);
            vertex.setY(vertex.getY() * scaleY);
            vertex.setZ(vertex.getZ() * scaleZ);
        }

        return vertices;
    }

    public static float[] upScalePositions(float[] positions, float scaleX, float scaleY, float scaleZ){
        for(int i = 0; i < positions.length; i += 3) {
            positions[i] = positions[i] * scaleX;
            positions[i + 1] = positions[i + 1] * scaleY;
            positions[i + 2] = positions[i + 2] * scaleZ;
        }

        return positions;
    }

    public static FloatBuffer updateFlippedBuffer(FloatBuffer buffer, float[] vectors, int size)
    {
        buffer.clear();

        for(int i = 0; i < vectors.length; i+= size)
        {
            for(int x = 0; x < size; x++) {
                buffer.put(vectors[i + x]);
            }
        }

        buffer.flip();

        return buffer;
    }
}
