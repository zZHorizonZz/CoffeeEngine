package com.horizon.engine.graphics.object;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.UtilModel;
import com.horizon.engine.component.Component;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.Mesh;
import com.horizon.engine.graphics.object.data.GameObjectTag;
import lombok.Data;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public @Data abstract class GameObject {

    private final GameEngine gameEngine;

    private String gameObjectName;

    private Map<ComponentType, Component> components = new LinkedHashMap<ComponentType, Component>();
    private List<GameObjectTag> gameObjectTags = new LinkedList<>();

    private final Vector3f scale;
    private final Vector3f position;
    private final Quaternionf rotation;

    private float xAngle;
    private float yAngle;
    private float zAngle;

    private Boolean selected;

    public GameObject(GameEngine gameEngine, String gameObjectName, Mesh mesh) {
        this.gameEngine = gameEngine;
        this.gameObjectName = gameObjectName;
        addComponent(mesh);
        position = new Vector3f();
        scale = new Vector3f(1, 1, 1);
        rotation = new Quaternionf();
    }

    public GameObject(GameEngine gameEngine, String gameObjectName) {
        this.gameEngine = gameEngine;
        this.gameObjectName = gameObjectName;
        position = new Vector3f();
        scale = new Vector3f(1, 1, 1);
        rotation = new Quaternionf();
    }

    public abstract void update();

    public Vector3f getPosition() {
        return position;
    }

    public float getX(){
        return getPosition().x();
    }

    public float getY(){
        return getPosition().y();
    }

    public float getZ(){
        return getPosition().z();
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
        mesh.updatePositions(UtilModel.upScalePositions(mesh.getPositions(), x, y, z), 3);

        return this;
    }

    public GameObject setRotation(float x, float y, float z) {
        this.xAngle = x;
        this.yAngle = y;
        this.zAngle = z;

        getRotation().rotateXYZ((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));

        return this;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public GameObject setRotation(Quaternionf q) {
        this.rotation.set(q);
        return this;
    }

    public boolean hasTag(GameObjectTag tag) {
        return gameObjectTags.contains(tag);
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
