package com.horizon.engine.graphics.object;

import com.horizon.engine.common.Color;
import com.horizon.engine.common.Raycast;
import com.horizon.engine.common.RaycastDisplay;
import com.horizon.engine.graphics.data.Transformation;
import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    @Getter private final Vector3f position;
    @Getter private final Vector3f rotation;

    @Getter @Setter private Matrix4f viewMatrix;

    @Getter private final Raycast raycast;
    @Getter private final RaycastDisplay raycastDisplay;

    @Getter @Setter private Color backgroundColor = Color.BACKGROUND;

    public Camera() {
        position = new Vector3f();
        rotation = new Vector3f();
        viewMatrix = new Matrix4f();

        raycast = new Raycast();
        raycastDisplay = new RaycastDisplay();
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;

        raycast = new Raycast();
        raycastDisplay = new RaycastDisplay();
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        if(rotation.x() + offsetX < 80 && rotation.x() + offsetX > -80)
            rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }

    public Matrix4f updateViewMatrix() {
        return Transformation.updateGenericViewMatrix(position, rotation, viewMatrix);
    }
}
