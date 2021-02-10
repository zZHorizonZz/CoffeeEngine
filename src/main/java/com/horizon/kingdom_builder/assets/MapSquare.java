package com.horizon.kingdom_builder.assets;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.Color;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.objects.PlaneObject;
import com.horizon.kingdom_builder.assets.model.ForestSquare;
import com.horizon.kingdom_builder.assets.model.Tree;
import com.horizon.kingdom_builder.assets.square.SquareContent;
import com.horizon.kingdom_builder.data.SquareObject;
import lombok.Getter;
import org.joml.Quaternionf;
import org.joml.Vector2f;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MapSquare extends GameObject {

    @Getter private final Vector2f coordinates;

    @Getter private PlaneObject model;
    @Getter protected Color color;
    @Getter protected SquareObject squareObject;

    @Getter private SquareContent squareContent;

    public MapSquare(GameEngine gameEngine, Vector2f coordinates, Color color) {
        super(gameEngine,"Map Square: " + coordinates.x() + "," + coordinates.y());
        this.coordinates = coordinates;
        this.color = color;

        buildModel();
    }

    @Override
    public void update() {

    }

    public void buildModel() {
        model = (PlaneObject) getGameEngine().getGameLogic().getScene().
                initializeObject(new PlaneObject(getGameEngine(), "Map Square Model: " + coordinates.x() + "," + coordinates.y()));

        model.setPosition(getCoordinates().x(), 0.0f, getCoordinates().y());
        model.getMesh().setMaterial(new Material(getColor()));

        int random = new Random().nextInt(3);
        if(random == 0) {
            setSquareObject(SquareObject.FOREST);
        } else if(random == 1) {
            setSquareObject(SquareObject.BUILDING);
        } else {
            setSquareObject(SquareObject.ROCK);
        }
    }

    public void setSquareObject(SquareObject squareObject) {
        this.squareObject = squareObject;

        switch (squareObject) {
            case FOREST:
                squareContent = new ForestSquare(this);
                break;
            case ROCK:
                break;
            case BUILDING:
                break;
        }
    }
}
