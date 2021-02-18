package com.horizon.game.building;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.graphics.object.terrain.Terrain;
import lombok.Getter;

public class BuildingManager extends AbstractManager {

    @Getter private Terrain terrain;

    public BuildingManager(GameEngine engine) {
        super(engine, "Building Manager");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void initialize() {
        terrain = new Terrain(getGameEngine(), "Terrain", 3, 3);
        getGameEngine().getScene().instantiate(terrain);
    }
}
