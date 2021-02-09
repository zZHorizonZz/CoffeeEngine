package com.horizon.engine.graphics.object.objects;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.UtilModelLoader;
import com.horizon.engine.graphics.object.GameObject;

public class PlaneObject extends GameObject {

    public PlaneObject(GameEngine engine, String name) {
        super(engine, name, engine.getAssetManager().getMesh("Plane"));
    }
}
