package com.horizon.engine.graphics.object.primitive;

import lombok.Getter;

public enum PrimitiveObject {

    CUBE("Cube"),
    CYLINDER("Cylinder"),
    PLANE("Plane");

    @Getter private final String meshName;

    PrimitiveObject(String meshName) {
        this.meshName = meshName;
    }
}
