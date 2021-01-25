package com.horizon.engine.graphics.object;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.UtilModel;
import com.horizon.engine.component.Component;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.Mesh;
import lombok.Data;
import org.joml.Vector3f;

import java.util.LinkedHashMap;
import java.util.Map;

public @Data abstract class GameObject {

    private final GameEngine gameEngine;

    private String gameObjectName;

    private Map<ComponentType, Component> components = new LinkedHashMap<ComponentType, Component>();

    private final Vector3f scale;
    private final Vector3f position;
    private final Vector3f rotation;

    public GameObject(GameEngine gameEngine, String gameObjectName, Mesh mesh) {
        this.gameEngine = gameEngine;
        this.gameObjectName = gameObjectName;
        addComponent(mesh);
        position = new Vector3f();
        scale = new Vector3f(1, 1, 1);
        rotation = new Vector3f();
    }

    public GameObject(GameEngine gameEngine, String gameObjectName) {
        this.gameEngine = gameEngine;
        this.gameObjectName = gameObjectName;
        position = new Vector3f();
        scale = new Vector3f(1, 1, 1);
        rotation = new Vector3f();
    }

    public Vector3f getPosition() {
        return position;
    }

    public GameObject setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;

        return this;
    }

    public GameObject setScale(float x, float y, float z) {
        scale.set(x, y, z);

        if(!getComponents().containsKey(ComponentType.MESH))
            return this;

        Mesh mesh = getMesh();
        mesh.updatePositions(UtilModel.upScalePositions(mesh.getPositions(), x, y, z));

        return this;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public GameObject setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;

        return this;
    }

    public Mesh getMesh() {
        return (Mesh) components.get(ComponentType.MESH);
    }

    public void addComponent(Component component){
        components.put(component.getComponentType(), component);
    }

    public Component getComponent(ComponentType componentType){
        return components.get(componentType);
    }
}
