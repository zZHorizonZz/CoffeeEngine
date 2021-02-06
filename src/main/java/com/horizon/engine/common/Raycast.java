package com.horizon.engine.common;

import com.horizon.engine.GameEngine;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import lombok.Data;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.primitives.Intersectionf;

import java.util.Collection;
import java.util.List;

public @Data class Raycast {

    private final Vector3f max;
    private final Vector3f min;
    private final Vector2f nearFar;
    private Vector3f direction;

    public Raycast() {
        direction = new Vector3f();
        min = new Vector3f();
        max = new Vector3f();
        nearFar = new Vector2f();
    }

    public void selectGameObject(Collection<GameObject> gameObjects, Camera camera) {
        direction = camera.getViewMatrix().positiveZ(direction).negate();
        selectGameObject(gameObjects, camera.getPosition(), direction);
    }

    public GameObject selectGameObject(Collection<GameObject> gameObjects, Vector3f center, Vector3f dir) {
        GameObject selectedGameObject = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        for (GameObject gameObject : gameObjects) {
            if(gameObject.getComponent(ComponentType.MESH) == null)
                continue;

            gameObject.setSelected(false);
            min.set(gameObject.getPosition());
            max.set(gameObject.getPosition());
            min.add(-gameObject.getScale().x(), -gameObject.getScale().y(), -gameObject.getScale().z());
            max.add(gameObject.getScale().x(), gameObject.getScale().y(), gameObject.getScale().z());
            if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x;
                selectedGameObject = gameObject;
            }
        }

        if (selectedGameObject != null) {
            selectedGameObject.setSelected(true);
            GameEngine.getLogger().atInfo().log("Object Selected -> X: " + selectedGameObject.getPosition().x() + " Y: " + selectedGameObject.getPosition().y() + " Z: " + selectedGameObject.getPosition().z());
        }

        return selectedGameObject;
    }
}
