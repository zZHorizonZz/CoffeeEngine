package com.horizon.engine.component.component;

import com.horizon.engine.common.UtilModel;
import com.horizon.engine.component.Component;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.data.Texture;
import com.horizon.engine.graphics.data.Vertex;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh extends Component {

    private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);

    @Getter private int vaoId;
    @Getter private List<Integer> vboIdList;

    @Getter private int vertexCount;

    @Getter @Setter private Material material;
    @Getter @Setter private Vector3f colour;

    @Getter private float[] positions;
    @Getter private float[] textureCoordinates;
    @Getter private float[] normals;
    @Getter private int[] indices;

    @Getter private FloatBuffer positionBuffer;
    @Getter private FloatBuffer textureCoordinatesBuffer;
    @Getter private FloatBuffer normalsBuffer;
    @Getter private IntBuffer indicesBuffer;

    protected int positionVboId;
    protected int textureVboId;
    protected int normalsVboId;
    protected int indicesVboId;

    public Mesh(float[] positions, float[] textureCoordinates, float[] normals, int[] indices) {
        super(ComponentType.MESH);

        this.positions = positions;
        this.textureCoordinates = textureCoordinates;
        this.normals = normals;
        this.indices = indices;

        render(positions, textureCoordinates, normals, indices);
    }

    public Mesh(List<Vertex> vertexes, float[] textureCoordinates, float[] normals, int[] indices) {
        super(ComponentType.MESH);
        float[] positions = UtilModel.vertexToArray(vertexes);

        this.positions = positions;
        this.textureCoordinates = textureCoordinates;
        this.normals = normals;
        this.indices = indices;

        render(positions, textureCoordinates, normals, indices);
    }

    @Override
    public void update() {
        Texture texture = material.getTexture();
        if (texture != null) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void render(float[] positions, float[] textureCoordinates, float[] normals, int[] indices){
        FloatBuffer positionsBuffer = null;
        FloatBuffer textureCoordinatesBuffer = null;
        FloatBuffer normalsBuffer = null;
        IntBuffer indicesBuffer = null;

        try {
            colour = Mesh.DEFAULT_COLOUR;
            vertexCount = indices.length;
            vboIdList = new LinkedList<>();
            vaoId = glGenVertexArrays();

            glBindVertexArray(vaoId);

            if(positions != null) {
                Pair<Integer, FloatBuffer> pair = createResourceBuffers(positions, 0, 3, GL_DYNAMIC_DRAW);
                positionVboId = pair.getKey();
                positionsBuffer = pair.getValue();
            } else {
                return;
            }

            if(textureCoordinates != null) {
                Pair<Integer, FloatBuffer> pair = createResourceBuffers(textureCoordinates, 1, 2, GL_STATIC_DRAW);
                textureVboId = pair.getKey();
                textureCoordinatesBuffer = pair.getValue();
            }

            if(normals != null) {
                Pair<Integer, FloatBuffer> pair = createResourceBuffers(normals, 2, 3, GL_STATIC_DRAW);
                normalsVboId = pair.getKey();
                normalsBuffer = pair.getValue();
            } else {
                return;
            }

            indicesVboId = glGenBuffers();
            vboIdList.add(indicesVboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (positionsBuffer != null) {
                this.positionBuffer = positionsBuffer;
                MemoryUtil.memFree(positionsBuffer);
            }
            if (textureCoordinatesBuffer != null) {
                MemoryUtil.memFree(textureCoordinatesBuffer);
            }
            if (normalsBuffer != null) {
                MemoryUtil.memFree(normalsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public void updatePositions(float[] positions) {
        try {
            glBindBuffer(GL_ARRAY_BUFFER, positionVboId);
            glBufferSubData(GL_ARRAY_BUFFER, 0, UtilModel.updateFlippedBuffer(positionBuffer, positions));
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        } finally {
            this.positions = positions;
        }
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }
        Texture texture = material.getTexture();
        if (texture != null) {
            texture.cleanup();
        }
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public Pair<Integer, FloatBuffer> createResourceBuffers(float[] bufferData, int index, int size, int drawType) {
        int vboId = glGenBuffers();

        vboIdList.add(vboId);

        FloatBuffer buffer = MemoryUtil.memAllocFloat(bufferData.length);
        buffer.put(bufferData).flip();

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, buffer, drawType);
        glEnableVertexAttribArray(index);
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);

        return new Pair<>(vboId, buffer);
    }

    public boolean isTextured() {
        return this.material != null;
    }
}