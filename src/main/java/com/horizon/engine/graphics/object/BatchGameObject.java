package com.horizon.engine.graphics.object;

import com.horizon.engine.GameEngine;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BatchGameObject extends GameObject {

    @Getter @Setter private List<GameObject> batchObjects = new LinkedList<>();

    public BatchGameObject(GameEngine gameEngine, String gameObjectName) {
        super(gameEngine, gameObjectName);
    }

    public BatchGameObject(GameEngine gameEngine, String gameObjectName, List<GameObject> batchObjects) {
        super(gameEngine, gameObjectName);

        this.batchObjects = batchObjects;
    }

    @Override
    public void update() {
        for(GameObject gameObject : batchObjects) {
            gameObject.update();
        }
    }

    public void addObjects(GameObject... gameObjects) {
        batchObjects.addAll(Arrays.asList(gameObjects));
    }

    public void addObject(GameObject gameObject) {
        batchObjects.add(gameObject);
    }
}
