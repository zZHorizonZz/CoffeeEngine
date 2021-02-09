package com.horizon.kingdom_builder.assets;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.Color;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.objects.PlaneObject;
import lombok.Getter;
import org.joml.Vector2f;

public class MapSquare extends GameObject {

    @Getter private final Vector2f coordinates;

    @Getter private PlaneObject model;
    @Getter protected Color color;

    public MapSquare(GameEngine gameEngine, Vector2f coordinates, Color color) {
        super(gameEngine,"Map Square: " + coordinates.x() + "," + coordinates.y());
        this.coordinates = coordinates;
        this.color = color;

        buildModel();
    }

    public void buildModel() {
        model = (PlaneObject) getGameEngine().getGameLogic().getScene().
                initializeObject(new PlaneObject(getGameEngine(), "Map Square Model: " + coordinates.x() + "," + coordinates.y()));

        model.setPosition(getCoordinates().x(), 0.0f, getCoordinates().y());
        model.getMesh().setMaterial(new Material(getColor()));
    }
}
