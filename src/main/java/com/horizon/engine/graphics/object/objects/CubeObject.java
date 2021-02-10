package com.horizon.engine.graphics.object.objects;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.UtilModelLoader;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.data.Texture;
import com.horizon.engine.graphics.object.GameObject;

public class CubeObject extends GameObject {

    public CubeObject(GameEngine engine, String name) {
        super(engine, name, engine.getAssetManager().getMesh("Cube"));
    }

    @Override
    public void update() {

    }
}
