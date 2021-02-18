package com.horizon.engine.asset.prefab;

import com.horizon.engine.component.Component;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.data.GameObjectTag;
import lombok.Data;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public @Data abstract class Prefab {

    public String name;

    private Map<ComponentType, Component> components = new LinkedHashMap<ComponentType, Component>();
    private List<GameObjectTag> gameObjectTags = new LinkedList<>();

    private final Vector3f scale;
    private final Quaternionf rotation;

    public Prefab(String name) {
        this.name = name;
        this.scale = new Vector3f();
        this.rotation = new Quaternionf();
    }

    public abstract GameObject instantiateObject();
}
