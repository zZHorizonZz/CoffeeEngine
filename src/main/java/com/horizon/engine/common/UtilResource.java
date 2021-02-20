package com.horizon.engine.common;

import org.lwjgl.system.MemoryUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class UtilResource {

    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = UtilResource.class.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Class.forName(UtilResource.class.getName()).getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }

    public static Map.Entry<Integer, FloatBuffer> createResourceBuffers(List<Integer> vboList, float[] bufferData, int index, int size, int drawType) {
        // Generate vboid
        int vboId = glGenBuffers();
        vboList.add(vboId);

        // Create float buffer.
        FloatBuffer buffer = MemoryUtil.memAllocFloat(bufferData.length);
        buffer.put(bufferData).flip();

        // Bind buffer.
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, buffer, drawType);
        glEnableVertexAttribArray(index);
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);

        return new AbstractMap.SimpleEntry<Integer, FloatBuffer>(vboId, buffer);
    }
}
