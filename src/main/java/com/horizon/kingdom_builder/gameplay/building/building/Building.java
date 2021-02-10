package com.horizon.kingdom_builder.gameplay.building.building;

import com.horizon.engine.GameEngine;
import com.horizon.engine.component.component.Mesh;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.kingdom_builder.gameplay.building.building.other.RangeType;

public abstract class Building extends GameObject {

    private String buildingName;

    private RangeType rangeType;
    private int buildingRange;

    public Building(GameEngine gameEngine, String gameObjectName, Mesh mesh) {
        super(gameEngine, gameObjectName, mesh);
    }
}
