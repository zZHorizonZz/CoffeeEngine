package com.horizon.kingdom_builder.assets.model;

import com.horizon.engine.common.UtilMath;
import com.horizon.engine.graphics.object.BatchGameObject;
import com.horizon.kingdom_builder.assets.MapSquare;
import com.horizon.kingdom_builder.assets.square.SquareContent;

public class ForestSquare extends SquareContent {

    public ForestSquare(MapSquare squareObject) {
        super(squareObject);

        initialize();
    }

    @Override
    public void initialize() {
        BatchGameObject treeBatch = new BatchGameObject(getMapSquare().getGameEngine(), getMapSquare().getGameObjectName() + " Tree Batch");
        Tree tree1 = new Tree(getMapSquare().getGameEngine(), getMapSquare().getGameObjectName() + " Tree " + 1);
        Tree tree2 = new Tree(getMapSquare().getGameEngine(), getMapSquare().getGameObjectName() + " Tree " + 2);
        Tree tree3 = new Tree(getMapSquare().getGameEngine(), getMapSquare().getGameObjectName() + " Tree " + 3);

        float scale1 = (float) UtilMath.getRandomNumberInRange(-0.025, 0.025);
        float scale2 = (float) UtilMath.getRandomNumberInRange(-0.025, 0.025);
        float scale3 = (float) UtilMath.getRandomNumberInRange(-0.025, 0.025);

        tree1.setScale(0.25f + scale1, 0.25f + scale1, 0.25f + scale1);
        tree2.setScale(0.25f + scale2, 0.25f + scale2, 0.25f + scale2);
        tree3.setScale(0.25f + scale3, 0.25f + scale3, 0.25f + scale3);

        tree1.setPosition(getMapSquare().getCoordinates().x() + 0.15f, 0.0f, getMapSquare().getCoordinates().y() + 0.15f);
        tree2.setPosition(getMapSquare().getCoordinates().x() - 0.20f, 0.0f, getMapSquare().getCoordinates().y() + 0.20f);
        tree3.setPosition(getMapSquare().getCoordinates().x() - 0.10f, 0.0f, getMapSquare().getCoordinates().y() - 0.10f);

        treeBatch.addObject(tree1);
        treeBatch.addObject(tree2);
        treeBatch.addObject(tree3);

        getMapSquare().getGameEngine().getScene().initializeObject(treeBatch);
    }
}
