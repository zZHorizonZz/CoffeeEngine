package com.horizon.engine.common;

import com.horizon.engine.Window;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.scene.Scene;
import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Collection;

public class RaycastDisplay extends Raycast {

    @Getter private final Matrix4f invProjectionMatrix;
    @Getter private final Matrix4f invViewMatrix;
    @Getter private final Vector3f mouseDirection;
    @Getter private final Vector4f tmpVec;

    public RaycastDisplay() {
        super();
        invProjectionMatrix = new Matrix4f();
        invViewMatrix = new Matrix4f();
        mouseDirection = new Vector3f();
        tmpVec = new Vector4f();
    }

    public GameObject selectGameObject(Collection<GameObject> gameObjects, Window window, Vector2d mousePos, Camera camera) {
        int wdwWitdh = Window.getWidth();
        int wdwHeight = Window.getHeight();

        float x = (float)(2 * mousePos.x) / (float)wdwWitdh - 1.0f;
        float y = 1.0f - (float)(2 * mousePos.y) / (float)wdwHeight;
        float z = -1.0f;

        invProjectionMatrix.set(window.getProjectionMatrix());
        invProjectionMatrix.invert();

        tmpVec.set(x, y, z, 1.0f);
        tmpVec.mul(invProjectionMatrix);
        tmpVec.z = -1.0f;
        tmpVec.w = 0.0f;

        Matrix4f viewMatrix = camera.getViewMatrix();
        invViewMatrix.set(viewMatrix);
        invViewMatrix.invert();
        tmpVec.mul(invViewMatrix);

        mouseDirection.set(tmpVec.x, tmpVec.y, tmpVec.z);

        return selectGameObject(gameObjects, camera.getPosition(), mouseDirection);
    }
}
