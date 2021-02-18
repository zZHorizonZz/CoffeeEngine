package com.horizon.engine.component.component.mesh;

import com.horizon.engine.common.UtilModel;
import com.horizon.engine.component.Component;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.texture.Texture;
import com.horizon.engine.graphics.data.Vertex;
import com.horizon.engine.graphics.object.GameObject;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh extends Component {

    public static final int MAX_WEIGHTS = 4;

    @Getter @Setter private String name;

    @Getter @Setter private int meshId;
    @Getter private int vaoId;
    @Getter private List<Integer> vboIdList;

    @Getter private int vertexCount;

    @Getter @Setter private Material material;
    @Getter @Setter private boolean visible = false;

    @Getter private float[] positions;
    @Getter private float[] textureCoordinates;
    @Getter private float[] normals;
    @Getter private float[] weights;
    @Getter private int[] indices;
    @Getter private int[] jointIndices;

    @Getter private FloatBuffer positionBuffer;
    @Getter private FloatBuffer textureCoordinatesBuffer;
    @Getter private FloatBuffer normalsBuffer;
    @Getter private IntBuffer indicesBuffer;
    @Getter private IntBuffer jointIndicesBuffer;
    @Getter private FloatBuffer weightsBuffer;

    protected int positionVboId;
    protected int textureVboId;
    protected int normalsVboId;
    protected int indicesVboId;
    protected int jointIndicesVboId;
    protected int weightVboId;

    @Getter @Setter private float boundingRadius = 1.0f;

    public Mesh(){
        super(ComponentType.MESH);
    }

    public Mesh(float[] positions) {
        super(ComponentType.MESH);

        this.positions = positions;

        render(positions);
    }

    public Mesh(float[] positions, float[] textureCoordinates, float[] normals, int[] indices) {
        this(positions, textureCoordinates, normals, indices, Mesh.createEmptyIntArray(Mesh.MAX_WEIGHTS * positions.length / 3, 0), Mesh.createEmptyFloatArray(Mesh.MAX_WEIGHTS * positions.length / 3, 0));
    }

    public Mesh(float[] positions, float[] textureCoordinates, float[] normals, int[] indices, int[] jointIndices, float[] weights) {
        super(ComponentType.MESH);

        this.positions = positions.clone();
        this.textureCoordinates = textureCoordinates.clone();
        this.normals = normals.clone();
        this.weights = weights.clone();
        this.indices = indices.clone();
        this.jointIndices = jointIndices.clone();

        render(positions, textureCoordinates, normals, indices, jointIndices, weights);
    }

    public Mesh(List<Vertex> vertexes, float[] textureCoordinates, float[] normals, int[] indices, int[] jointIndices, float[] weights) {
        super(ComponentType.MESH);
        float[] positions = UtilModel.vertexToArray(vertexes);

        this.positions = positions.clone();
        this.textureCoordinates = textureCoordinates.clone();
        this.normals = normals.clone();
        this.indices = indices.clone();
        this.indices = indices.clone();
        this.jointIndices = jointIndices.clone();

        render(positions, textureCoordinates, normals, indices, jointIndices, weights);
    }

    @Override
    public void update() {
        if(textureVboId == 0) {
            // This is used for hud rendering.
            glBindVertexArray(getVaoId());
            glEnableVertexAttribArray(0);

            glDrawArrays(GL_TRIANGLE_STRIP, 0, getVertexCount());

            glDisableVertexAttribArray(0);
            glBindVertexArray(0);
        } else {
            // This is used for mesh rendering.
            start();

            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);

            glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
            glBindTexture(GL_TEXTURE_2D, 0);
        }
    }

    private void render(float[] positions){
        FloatBuffer positionsBuffer = null;

        try {
            vertexCount = positions.length / 2;
            vboIdList = new LinkedList<>();
            vaoId = glGenVertexArrays();

            glBindVertexArray(vaoId);

            Entry<Integer, FloatBuffer> pair = createResourceBuffers(positions, 0, 2, GL_DYNAMIC_DRAW);
            positionVboId = pair.getKey();
            positionsBuffer = pair.getValue();

            glBindVertexArray(0);
        } finally {
            if (positionsBuffer != null) {
                this.positionBuffer = positionsBuffer;
                MemoryUtil.memFree(positionsBuffer);
            }
        }
    }

    private void render(float[] positions, float[] textureCoordinates, float[] normals, int[] indices, int[] jointIndices, float[] weights) {
        FloatBuffer positionsBuffer = null;
        FloatBuffer textureCoordinatesBuffer = null;
        FloatBuffer normalsBuffer = null;
        FloatBuffer weightsBuffer = null;
        IntBuffer jointIndicesBuffer = null;
        IntBuffer indicesBuffer = null;

        try {
            vertexCount = indices.length;
            vboIdList = new LinkedList<>();
            vaoId = glGenVertexArrays();

            glBindVertexArray(vaoId);

            // Create positions buffer.
            if (positions != null) {
                Entry<Integer, FloatBuffer> pair = createResourceBuffers(positions, 0, 3, GL_DYNAMIC_DRAW);
                positionVboId = pair.getKey();
                positionsBuffer = pair.getValue();
            }

            // Create texture buffer.
            if (textureCoordinates != null) {
                Entry<Integer, FloatBuffer> pair = createResourceBuffers(textureCoordinates, 1, 2, GL_STATIC_DRAW);
                textureVboId = pair.getKey();
                textureCoordinatesBuffer = pair.getValue();
            }

            // Create normals buffer.
            if (normals != null) {
                normalsVboId = glGenBuffers();
                vboIdList.add(normalsVboId);
                normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
                if (normalsBuffer.capacity() > 0) {
                    normalsBuffer.put(normals).flip();
                } else {
                    // Create empty structure
                    normalsBuffer = MemoryUtil.memAllocFloat(positions.length);
                }
                glBindBuffer(GL_ARRAY_BUFFER, normalsVboId);
                glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
                glEnableVertexAttribArray(2);
                glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
            }

            // Create weights buffer
            if (weights != null) {
                Entry<Integer, FloatBuffer> pair = createResourceBuffers(weights, 3, 4, GL_STATIC_DRAW);
                weightVboId = pair.getKey();
                weightsBuffer = pair.getValue();
            }

            // Create joint indices buffer
            if(jointIndices != null) {
                jointIndicesVboId = glGenBuffers();
                vboIdList.add(jointIndicesVboId);
                jointIndicesBuffer = MemoryUtil.memAllocInt(jointIndices.length);
                jointIndicesBuffer.put(jointIndices).flip();
                glBindBuffer(GL_ARRAY_BUFFER, jointIndicesVboId);
                glBufferData(GL_ARRAY_BUFFER, jointIndicesBuffer, GL_STATIC_DRAW);
                glEnableVertexAttribArray(4);
                glVertexAttribPointer(4, 4, GL_FLOAT, false, 0, 0);
            }

            //Create indices buffer.
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
                this.textureCoordinatesBuffer = textureCoordinatesBuffer;
                MemoryUtil.memFree(textureCoordinatesBuffer);
            }
            if (normalsBuffer != null) {
                this.normalsBuffer = normalsBuffer;
                MemoryUtil.memFree(normalsBuffer);
            }
            if (weightsBuffer != null) {
                this.weightsBuffer = weightsBuffer;
                MemoryUtil.memFree(weightsBuffer);
            }
            if (indicesBuffer != null) {
                this.indicesBuffer = indicesBuffer;
                MemoryUtil.memFree(indicesBuffer);
            }
            if (jointIndicesBuffer != null) {
                this.jointIndicesBuffer = jointIndicesBuffer;
                MemoryUtil.memFree(jointIndicesBuffer);
            }
        }
    }

    public void start() {
        Texture texture = material.getTexture();
        if (texture != null) {
            // Activate first texture bank and bind the texture
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }

        // Draw the mesh
        glBindVertexArray(getVaoId());
    }

    public void stop() {
        // Restore state
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void render() {
        start();

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        stop();
    }

    public void updatePositions(float[] positions, int size) {
        try {
            // Activate array buffer and set positions.
            glBindBuffer(GL_ARRAY_BUFFER, positionVboId);
            glBufferSubData(GL_ARRAY_BUFFER, 0, UtilModel.updateFlippedBuffer(positionBuffer, positions, size));

            // Restore state.
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        } finally {
            this.positions = positions;
        }
    }

    public void renderList(List<GameObject> gameObjects, Consumer<GameObject> consumer) {
        if(gameObjects == null)
            return;

        start();

        for (GameObject gameObject : gameObjects) {
            // Set up data required by GameItem
            consumer.accept(gameObject);
            // Render this game item
            glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        }

        stop();
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the texture
        Texture texture = material.getTexture();
        if (texture != null) {
            texture.cleanup();
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public void deleteBuffers() {
        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public void cleanUpTexture(){
        // Check if texture is not null.
        Texture texture = material.getTexture();
        if (texture != null) {
            texture.cleanup();
        }
    }

    public Entry<Integer, FloatBuffer> createResourceBuffers(float[] bufferData, int index, int size, int drawType) {
        // Generate vboid
        int vboId = glGenBuffers();
        vboIdList.add(vboId);

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

    protected static float[] createEmptyFloatArray(int length, float defaultValue) {
        float[] result = new float[length];
        Arrays.fill(result, defaultValue);
        return result;
    }

    protected static int[] createEmptyIntArray(int length, int defaultValue) {
        int[] result = new int[length];
        Arrays.fill(result, defaultValue);
        return result;
    }

    public boolean isTextured() {
        return this.material != null;
    }
}