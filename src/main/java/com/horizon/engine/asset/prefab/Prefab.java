package com.horizon.engine.asset.prefab;

import com.horizon.engine.component.Component;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.graphics.data.Transform;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.data.GameObjectTag;
import lombok.Data;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Prefab can be used for creating gameobject with its tags, components, name,
 * scale, rotations and much more and can be used as pre-loaded gameobject.
 *
 * @apiNote This is only abstract class so it can be used for creation of
 *          custom gameobject.
 *
 * @author Horizon
 *
 */
public @Data abstract class Prefab {

    public String name;

    private Map<ComponentType, Component> components = new LinkedHashMap<ComponentType, Component>();
    private List<GameObjectTag> gameObjectTags = new LinkedList<>();

    private Transform transform;

    public Prefab(String name) {
        this.name = name;
        this.transform = new Transform();
    }

    public void addParameters(GameObject gameObject) {
        for(Component component : components.values()) {
            gameObject.addComponent(component);
        }

        gameObject.getGameObjectTags().addAll(gameObjectTags);
        gameObject.setScale(transform.getScale());
        gameObject.setRotation(transform.getRotation());
    }

    /**
     * This function is used for creation of the new GameObject from
     * current prefab.
     */
    public abstract GameObject instantiateObject();
}
