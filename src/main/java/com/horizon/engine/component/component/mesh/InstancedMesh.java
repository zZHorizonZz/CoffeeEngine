package com.horizon.engine.component.component.mesh;

import com.horizon.engine.graphics.texture.Texture;
import com.horizon.engine.graphics.data.Transformation;
import com.horizon.engine.graphics.object.GameObject;
import lombok.Getter;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class InstancedMesh extends Mesh {

    @Getter private static final int FLOAT_SIZE_BYTES = 4;
    @Getter private static final int VECTOR4F_SIZE_BYTES = 4 * InstancedMesh.FLOAT_SIZE_BYTES;
    @Getter private static final int MATRIX_SIZE_FLOATS = 4 * 4;
    @Getter private static final int MATRIX_SIZE_BYTES = InstancedMesh.MATRIX_SIZE_FLOATS * InstancedMesh.FLOAT_SIZE_BYTES;
    @Getter private static final int INSTANCE_SIZE_BYTES = InstancedMesh.MATRIX_SIZE_BYTES * 2 + InstancedMesh.FLOAT_SIZE_BYTES * 2 + InstancedMesh.FLOAT_SIZE_BYTES;
    @Getter private static final int INSTANCE_SIZE_FLOATS = InstancedMesh.MATRIX_SIZE_FLOATS * 2 + 3;

    @Getter private final int numInstances;
    @Getter private final int instanceDataVBO;

    @Getter private FloatBuffer instanceDataBuffer;

    public InstancedMesh(float[] positions, float[] textureCoordinates, float[] normals, int[] indices, int numInstances) {
        super(positions, textureCoordinates, normals, indices, Mesh.createEmptyIntArray(Mesh.MAX_WEIGHTS * positions.length / 3, 0), Mesh.createEmptyFloatArray(Mesh.MAX_WEIGHTS * positions.length / 3, 0));

        this.numInstances = numInstances;

        glBindVertexArray(getVaoId());

        // Model View Matrix
        instanceDataVBO = glGenBuffers();
        getVboIdList().add(instanceDataVBO);
        instanceDataBuffer = MemoryUtil.memAllocFloat(numInstances * InstancedMesh.INSTANCE_SIZE_FLOATS);
        glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);
        int start = 5;
        int strideStart = 0;
        for (int i = 0; i < 4; i++) {
            glVertexAttribPointer(start, 4, GL_FLOAT, false, InstancedMesh.INSTANCE_SIZE_BYTES, strideStart);
            glVertexAttribDivisor(start, 1);
            glEnableVertexAttribArray(start);
            start++;
            strideStart += InstancedMesh.VECTOR4F_SIZE_BYTES;
        }

        // Light view matrix
        for (int i = 0; i < 4; i++) {
            glVertexAttribPointer(start, 4, GL_FLOAT, false, InstancedMesh.INSTANCE_SIZE_BYTES, strideStart);
            glVertexAttribDivisor(start, 1);
            glEnableVertexAttribArray(start);
            start++;
            strideStart += InstancedMesh.VECTOR4F_SIZE_BYTES;
        }

        // Texture offsets
        glVertexAttribPointer(start, 2, GL_FLOAT, false, InstancedMesh.INSTANCE_SIZE_BYTES, strideStart);
        glVertexAttribDivisor(start, 1);
        glEnableVertexAttribArray(start);
        strideStart += InstancedMesh.FLOAT_SIZE_BYTES * 2;
        start++;

        // Selected
        glVertexAttribPointer(start, 1, GL_FLOAT, false, InstancedMesh.INSTANCE_SIZE_BYTES, strideStart);
        glVertexAttribDivisor(start, 1);
        glEnableVertexAttribArray(start);
        start++;

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        if (this.instanceDataBuffer != null) {
            MemoryUtil.memFree(this.instanceDataBuffer);
            this.instanceDataBuffer = null;
        }
    }

    public void renderListInstanced(List<GameObject> gameObjects, Transformation transformation, Matrix4f viewMatrix) {
        renderListInstanced(gameObjects, false, transformation, viewMatrix);
    }

    public void renderListInstanced(List<GameObject> gameObjects, boolean billBoard, Transformation transformation, Matrix4f viewMatrix) {
        start();

        int chunkSize = numInstances;
        int length = gameObjects.size();
        for (int i = 0; i < length; i += chunkSize) {
            int end = Math.min(length, i + chunkSize);
            List<GameObject> subList = gameObjects.subList(i, end);
            renderChunkInstanced(subList, billBoard, transformation, viewMatrix);
        }

        stop();
    }

    private void renderChunkInstanced(List<GameObject> gameObjects, boolean billBoard, Transformation transformation, Matrix4f viewMatrix) {
        this.instanceDataBuffer.clear();

        int i = 0;

        Texture text = getMaterial().getTexture();
        for (GameObject gameObject : gameObjects) {
            Matrix4f modelMatrix = transformation.buildModelMatrix(gameObject);
            if (viewMatrix != null && billBoard) {
                viewMatrix.transpose3x3(modelMatrix);
            }
            modelMatrix.get(InstancedMesh.INSTANCE_SIZE_FLOATS * i, instanceDataBuffer);
            if (text != null) {
                int col = gameObject.getTexturePosition() % text.getNumCols();
                int row = gameObject.getTexturePosition() / text.getNumCols();
                float textXOffset = (float) col / text.getNumCols();
                float textYOffset = (float) row / text.getNumRows();
                int buffPos = InstancedMesh.INSTANCE_SIZE_FLOATS * i + InstancedMesh.MATRIX_SIZE_FLOATS;
                this.instanceDataBuffer.put(buffPos, textXOffset);
                this.instanceDataBuffer.put(buffPos + 1, textYOffset);
            }

            // Selected data or scaling for billboard
            int buffPos = InstancedMesh.INSTANCE_SIZE_FLOATS * i + InstancedMesh.MATRIX_SIZE_FLOATS + 2;
            this.instanceDataBuffer.put(buffPos, billBoard ? Math.max(gameObject.getScale().x(), Math.max(gameObject.getScale().y(), gameObject.getScale().z())) : 0);

            i++;
        }

        glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);
        glBufferData(GL_ARRAY_BUFFER, instanceDataBuffer, GL_DYNAMIC_READ);

        glDrawElementsInstanced(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0, gameObjects.size());

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
