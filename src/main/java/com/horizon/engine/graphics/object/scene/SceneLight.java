package com.horizon.engine.graphics.object.scene;

import com.horizon.engine.graphics.light.DirectionalLight;
import com.horizon.engine.graphics.light.PointLight;
import com.horizon.engine.graphics.light.SpotLight;
import lombok.Data;
import org.joml.Vector3f;

public @Data class SceneLight {

    private Vector3f ambientLight;
    private PointLight[] pointLightList;
    private SpotLight[] spotLightList;
    private DirectionalLight directionalLight;

    public void addPointLight(PointLight pointLight) {
        if(pointLightList == null)
            pointLightList = new PointLight[0];

        PointLight[] pointLights = new PointLight[pointLightList.length + 1];

        for(int i = 0; i <= pointLightList.length; i++) {
            pointLights[i] = i < pointLightList.length ? pointLightList[i] : pointLight;
        }

        pointLightList = pointLights;
    }

    public void addSpotLight(SpotLight pointLight) {
        if(spotLightList == null)
            spotLightList = new SpotLight[0];

        SpotLight[] spotLights = new SpotLight[spotLightList.length + 1];

        for(int i = 0; i <= spotLightList.length; i++) {
            spotLights[i] = i < spotLightList.length ? spotLightList[i] : pointLight;
        }

        spotLightList = spotLights;
    }
}
