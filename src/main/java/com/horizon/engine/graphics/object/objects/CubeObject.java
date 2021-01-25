package com.horizon.engine.graphics.object.objects;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.UtilModelLoader;
import com.horizon.engine.graphics.data.Texture;
import com.horizon.engine.graphics.object.GameObject;

public class CubeObject extends GameObject {

    public CubeObject(GameEngine engine, String name, Texture texture, Float scaleX, Float scaleY, Float scaleZ) {
        super(engine, name, UtilModelLoader.loadMesh("/models/cube.obj"));
    }
}
