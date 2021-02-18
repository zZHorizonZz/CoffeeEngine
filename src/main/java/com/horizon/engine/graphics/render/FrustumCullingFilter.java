package com.horizon.engine.graphics.render;

import com.horizon.engine.component.component.mesh.InstancedMesh;
import com.horizon.engine.component.component.mesh.Mesh;
import com.horizon.engine.graphics.object.GameObject;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public class FrustumCullingFilter {

    private final Matrix4f prjViewMatrix;
    private final FrustumIntersection frustumIntersection;

    public FrustumCullingFilter() {
        prjViewMatrix = new Matrix4f();
        frustumIntersection = new FrustumIntersection();
    }

    public void updateFrustum(Matrix4f projMatrix, Matrix4f viewMatrix) {
        // Calculate projection view matrix
        prjViewMatrix.set(projMatrix);
        prjViewMatrix.mul(viewMatrix);
        // Update frustum intersection class
        frustumIntersection.set(prjViewMatrix);
    }

    public void filterObject(Map<Integer, Map.Entry<Mesh, GameObject>> mapMesh) {
        float boundingRadius;
        Vector3f position;

        for (Map.Entry<Integer, Map.Entry<Mesh, GameObject>> entry : mapMesh.entrySet()) {
            Mesh mesh = entry.getValue().getKey();
            GameObject gameObject = entry.getValue().getValue();

            if (!gameObject.isDisableFrustumCulling()) {
                float gameObjectScale = Math.max(gameObject.getScale().x(), Math.max(gameObject.getScale().y(), gameObject.getScale().z()));
                boundingRadius = gameObjectScale * mesh.getBoundingRadius();
                position = gameObject.getPosition();
                gameObject.setInsideFrustum(insideFrustum(position.x(), position.y(), position.z(), boundingRadius));
            }
        }
    }

    public void filterList(Map<Integer, Map.Entry<InstancedMesh, List<GameObject>>> mapMesh) {
        for (Map.Entry<Integer, Map.Entry<InstancedMesh, List<GameObject>>> entry : mapMesh.entrySet()) {
            List<GameObject> gameObjects = entry.getValue().getValue();
            filter(gameObjects, entry.getValue().getKey().getBoundingRadius());
        }
    }

    public void filter(List<GameObject> gameObjects, float meshBoundingRadius) {
        float boundingRadius;
        Vector3f position;
        for (GameObject gameObject : gameObjects) {
            if (!gameObject.isDisableFrustumCulling()) {
                float gameObjectScale = Math.max(gameObject.getScale().x(), Math.max(gameObject.getScale().y(), gameObject.getScale().z()));
                boundingRadius = gameObjectScale * meshBoundingRadius;
                position = gameObject.getPosition();
                gameObject.setInsideFrustum(insideFrustum(position.x(), position.y(), position.z(), boundingRadius));
            }
        }
    }

    public boolean insideFrustum(float x0, float y0, float z0, float boundingRadius) {
        return frustumIntersection.testSphere(x0, y0, z0, boundingRadius);
    }
}
