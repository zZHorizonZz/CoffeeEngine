package com.horizon.engine.component.component.mesh;

import com.horizon.engine.common.UtilModel;
import com.horizon.engine.common.UtilResource;
import com.horizon.engine.component.Component;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.texture.Texture;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class TerrainMesh extends Component {

    @Getter @Setter private String name;

    @Getter @Setter private int meshId;
    @Getter private int vaoId;
    @Getter private List<Integer> vboIdList;

    @Getter private int vertexCount;

    @Getter @Setter private Material material;
    @Getter @Setter private boolean visible = false;

    @Getter private float[] positions;
    @Getter private float[] colors;
    @Getter private float[] normals;
    @Getter private int[] indices;

    @Getter private FloatBuffer positionBuffer;
    @Getter private FloatBuffer colorsBuffer;
    @Getter private FloatBuffer normalsBuffer;
    @Getter private IntBuffer indicesBuffer;

    protected int positionVboId;
    protected int colorsVboId;
    protected int normalsVboId;
    protected int indicesVboId;

    public TerrainMesh(float[] positions, float[] colors, float[] normals, int[] indices) {
        super(ComponentType.MESH);

        this.positions = positions.clone();
        this.colors = colors.clone();
        this.normals = normals.clone();
        this.indices = indices.clone();

        render(this.positions, this.colors, this.normals, this.indices);
    }

    @Override
    public void update() {

    }

    private void render(float[] positions, float[] colors, float[] normals, int[] indices) {
        FloatBuffer positionsBuffer = null;
        FloatBuffer colorsBuffer = null;
        FloatBuffer normalsBuffer = null;
        IntBuffer indicesBuffer = null;

        try {
            vertexCount = indices.length;
            vboIdList = new LinkedList<>();
            vaoId = glGenVertexArrays();

            glBindVertexArray(vaoId);

            // Create positions buffer.
            if (positions != null) {
                Map.Entry<Integer, FloatBuffer> pair = UtilResource.createResourceBuffers(vboIdList, positions, 0, 3, GL_DYNAMIC_DRAW);
                positionVboId = pair.getKey();
                positionsBuffer = pair.getValue();
            }

            // Create texture buffer.
            if (colors != null) {
                Map.Entry<Integer, FloatBuffer> pair = UtilResource.createResourceBuffers(vboIdList, colors, 1, 4, GL_STATIC_DRAW);
                colorsVboId = pair.getKey();
                colorsBuffer = pair.getValue();
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
                glVertexAttribPointer(2, 4, GL_FLOAT, false, 0, 0);
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
            if (colorsBuffer != null) {
                this.colorsBuffer = colorsBuffer;
                MemoryUtil.memFree(colorsBuffer);
            }
            if (normalsBuffer != null) {
                this.normalsBuffer = normalsBuffer;
                MemoryUtil.memFree(normalsBuffer);
            }
            if (indicesBuffer != null) {
                this.indicesBuffer = indicesBuffer;
                MemoryUtil.memFree(indicesBuffer);
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
