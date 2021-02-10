package com.horizon.kingdom_builder.assets.model;

import com.horizon.engine.GameEngine;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.data.Texture;
import com.horizon.engine.graphics.object.GameObject;

public class Tree extends GameObject {

    public Tree(GameEngine gameEngine, String gameObjectName) {
        super(gameEngine, gameObjectName, gameEngine.getAssetManager().getMesh("Tree"));

        getMesh().setMaterial(new Material(new Texture("src/textures/texture.png")));
    }

    @Override
    public void update() {

    }
}
