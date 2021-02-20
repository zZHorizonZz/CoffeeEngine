package com.horizon.engine.graphics.data;

import lombok.Data;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public @Data class Transform {

    private final Vector3f scale;
    private final Vector3f position;
    private final Quaternionf rotation;

    public Transform() {
        this(new Vector3f(1, 1, 1), new Vector3f(), new Quaternionf());
    }

    public Transform(Vector3f scale, Vector3f position, Quaternionf rotation) {
        this.scale = scale;
        this.position = position;
        this.rotation = rotation;
    }

    public float getScaleX() {
        return this.scale.x();
    }

    public float getScaleY() {
        return this.scale.y();
    }

    public float getScaleZ() {
        return this.scale.z();
    }

    public float getX() {
        return this.position.x();
    }

    public float getY() {
        return this.position.y();
    }

    public float getZ() {
        return this.position.z();
    }

    public float getRotationX() {
        return (float) Math.toDegrees(this.rotation.x());
    }

    public float getRotationY() {
        return (float) Math.toDegrees(this.rotation.z());
    }

    public float getRotationZ() {
        return (float) Math.toDegrees(this.rotation.y());
    }
}

