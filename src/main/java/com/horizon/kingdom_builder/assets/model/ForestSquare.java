package com.horizon.kingdom_builder.assets.model;

import com.horizon.kingdom_builder.assets.MapSquare;
import com.horizon.kingdom_builder.assets.square.SquareContent;

public class ForestSquare extends SquareContent {

    public ForestSquare(MapSquare squareObject) {
        super(squareObject);

        initialize();
    }

    @Override
    public void initialize() {
        Forest forest = new Forest(getMapSquare().getGameEngine(), getMapSquare().getGameObjectName() + " Forest");

        forest.setPosition(getMapSquare().getCoordinates().x(), 0.0f, getMapSquare().getCoordinates().y());

        getMapSquare().getGameEngine().getScene().instantiate(forest);
    }
}
