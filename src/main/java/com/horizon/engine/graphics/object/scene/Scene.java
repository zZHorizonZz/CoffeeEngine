package com.horizon.engine.graphics.object.scene;

import com.horizon.engine.GameEngine;
import com.horizon.engine.component.Component;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.light.DirectionalLightComponent;
import com.horizon.engine.component.component.light.PointLightComponent;
import com.horizon.engine.component.component.light.SpotLightComponent;
import com.horizon.engine.graphics.light.DirectionalLight;
import com.horizon.engine.graphics.light.PointLight;
import com.horizon.engine.graphics.light.SpotLight;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import lombok.Data;
import org.joml.Vector3f;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public @Data class Scene {

    private final GameEngine gameEngine;

    private Map<String, GameObject> sceneObjects = new LinkedHashMap<>();

    private Camera sceneCamera;

    private DirectionalLight directionalLight;
    private final List<SpotLight> spotLightList;
    private final List<PointLight> pointLightList;

    public Scene(GameEngine gameEngine){
        this.gameEngine = gameEngine;
        this.directionalLight = new DirectionalLight(gameEngine);
        this.spotLightList = new LinkedList<>();
        this.pointLightList = new LinkedList<>();
    }

    public void initialize(){
        sceneCamera = new Camera();
        getGameEngine().getWindow().setClearColor(sceneCamera.getBackgroundColor());
    }

    public GameObject initializeObject(GameObject gameObject) {
        if(gameObject.getComponents().containsKey(ComponentType.LIGHT)) {
            Component component = gameObject.getComponents().get(ComponentType.LIGHT);
            if(component instanceof DirectionalLightComponent) {
                directionalLight = (DirectionalLight) gameObject;
            }

            if(component instanceof SpotLightComponent) {
                spotLightList.add((SpotLight) gameObject);
            }

            if(component instanceof PointLightComponent) {
                pointLightList.add((PointLight) gameObject);
            }
        }
        sceneObjects.put(gameObject.getGameObjectName(), gameObject);

        return gameObject;
    }

    public GameObject getGameObjectByName(String name){
        return sceneObjects.get(name);
    }
}
