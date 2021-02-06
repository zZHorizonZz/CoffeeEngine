package com.horizon.engine.graphics.data;

import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transformation {

    private final Matrix4f projectionMatrix;
    private final Matrix4f modelMatrix;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f modelLightViewMatrix;
    private final Matrix4f lightViewMatrix;
    private final Matrix4f orthoProjMatrix;
    private final Matrix4f ortho2DMatrix;
    private final Matrix4f orthoModelMatrix;

    public Transformation() {
        projectionMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        modelLightViewMatrix = new Matrix4f();
        orthoProjMatrix = new Matrix4f();
        ortho2DMatrix = new Matrix4f();
        orthoModelMatrix = new Matrix4f();
        lightViewMatrix = new Matrix4f();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        return projectionMatrix.setPerspective(fov, aspectRatio, zNear, zFar);
    }

    public final Matrix4f getOrthoProjectionMatrix() {
        return orthoProjMatrix;
    }

    public Matrix4f updateOrthoProjectionMatrix(float left, float right, float bottom, float top, float zNear, float zFar) {
        return orthoProjMatrix.setOrtho(left, right, bottom, top, zNear, zFar);
    }

    public Matrix4f getLightViewMatrix() {
        return lightViewMatrix;
    }

    public void setLightViewMatrix(Matrix4f lightViewMatrix) {
        this.lightViewMatrix.set(lightViewMatrix);
    }

    public Matrix4f updateLightViewMatrix(Vector3f position, Vector3f rotation) {
        return updateGenericViewMatrix(position, rotation, lightViewMatrix);
    }

    public static  Matrix4f updateGenericViewMatrix(Vector3f position, Vector3f rotation, Matrix4f matrix) {
        return matrix.rotationX((float) Math.toRadians(rotation.x)).rotateY((float) Math.toRadians(rotation.y)).translate(-position.x, -position.y, -position.z);
    }

    public final Matrix4f getOrtho2DProjectionMatrix(float left, float right, float bottom, float top) {
        return ortho2DMatrix.setOrtho2D(left, right, bottom, top);
    }

    public Matrix4f buildModelMatrix(GameObject gameItem) {
        Quaternionf rotation = gameItem.getRotation();
        return modelMatrix.translationRotateScale(
                gameItem.getPosition().x, gameItem.getPosition().y, gameItem.getPosition().z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                gameItem.getScale().x, gameItem.getScale().y, gameItem.getScale().z);
    }

    public Matrix4f buildModelViewMatrix(GameObject gameItem, Matrix4f viewMatrix) {
        return buildModelViewMatrix(buildModelMatrix(gameItem), viewMatrix);
    }

    public Matrix4f buildModelViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix) {
        return viewMatrix.mulAffine(modelMatrix, modelViewMatrix);
    }

    public Matrix4f buildModelLightViewMatrix(GameObject gameItem, Matrix4f lightViewMatrix) {
        return buildModelViewMatrix(buildModelMatrix(gameItem), lightViewMatrix);
    }

    public Matrix4f buildModelLightViewMatrix(Matrix4f modelMatrix, Matrix4f lightViewMatrix) {
        return lightViewMatrix.mulAffine(modelMatrix, modelLightViewMatrix);
    }

    public Matrix4f buildOrthoProjModelMatrix(GameObject gameItem, Matrix4f orthoMatrix) {
        return orthoMatrix.mulOrthoAffine(buildModelMatrix(gameItem), orthoModelMatrix);
    }
}
