package com.horizon.engine.graphics.object.objects;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.UtilModelLoader;
import com.horizon.engine.graphics.data.Texture;
import com.horizon.engine.graphics.object.GameObject;
import lombok.Getter;

public class CylinderObject extends GameObject {

    @Getter private final Float scaleX, scaleY, scaleZ;

    public CylinderObject(GameEngine engine, String name, Texture texture, Float scaleX, Float scaleY, Float scaleZ) {
        super(engine, name, UtilModelLoader.loadMesh("/models/cylinder.obj"));

        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }
}
