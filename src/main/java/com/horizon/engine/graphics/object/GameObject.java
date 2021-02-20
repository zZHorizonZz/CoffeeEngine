package com.horizon.engine.graphics.object;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.UtilModel;
import com.horizon.engine.component.Component;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.mesh.Mesh;
import com.horizon.engine.graphics.data.Transform;
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
    private Transform transform;

    private String gameObjectName;

    private Map<ComponentType, Component> components = new LinkedHashMap<ComponentType, Component>();
    private List<GameObjectTag> gameObjectTags = new LinkedList<>();

    private boolean selected;

    private int texturePosition;

    private boolean disableFrustumCulling;
    private boolean insideFrustum;

    public GameObject(GameEngine gameEngine, String gameObjectName, Mesh mesh) {
        this.gameEngine = gameEngine;
        this.gameObjectName = gameObjectName;
        this.transform = new Transform();
        addComponent(mesh);
    }

    public GameObject(GameEngine gameEngine, String gameObjectName) {
        this.gameEngine = gameEngine;
        this.gameObjectName = gameObjectName;
        this.transform = new Transform();
    }

    public abstract void update();

    public Vector3f getPosition() {
        return transform.getPosition();
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

    public GameObject setPosition(Vector3f position) {
        return setPosition(position.x(), position.y(), position.z());
    }

    public GameObject setPosition(float x, float y, float z) {
        this.transform.getPosition().set(x, y, z);
        return this;
    }

    public GameObject setScale(Vector3f scale) {
        return setScale(scale.x(), scale.y(), scale.z());
    }

    public GameObject setScale(float x, float y, float z) {
        this.transform.getScale().set(x, y, z);

        if(!getComponents().containsKey(ComponentType.MESH))
            return this;

        Mesh mesh = getMesh();
        mesh.updatePositions(UtilModel.upScalePositions(mesh.getPositions(), x, y, z), 3);

        return this;
    }

    public GameObject setRotation(Quaternionf q) {
        this.transform.getRotation().set(q);
        return this;
    }

    public GameObject setRotation(float x, float y, float z) {
        this.transform.getRotation().rotateXYZ((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
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
