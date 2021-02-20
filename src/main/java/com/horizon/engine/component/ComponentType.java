package com.horizon.engine.component;

import com.horizon.engine.component.component.generator.BiomeGenerator;
import com.horizon.engine.component.component.mesh.Mesh;
import com.horizon.engine.component.component.light.Light;
import com.horizon.engine.component.component.hud.text.TextComponent;

public enum ComponentType {

    MESH(Mesh.class),
    LIGHT(Light.class),
    TEXT(TextComponent.class),
    GENERATOR(BiomeGenerator.class),
    GRAVITY(null),
    CUSTOM(null);

    private final Object clazz;

    ComponentType(Object clazz){
        this.clazz = clazz;
    }

    public Object getClazz() {
        return clazz;
    }
}
