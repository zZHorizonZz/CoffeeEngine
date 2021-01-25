package com.horizon.game.building.house;

import com.horizon.engine.GameEngine;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.data.Texture;
import com.horizon.engine.graphics.object.objects.CubeObject;
import com.horizon.engine.graphics.object.objects.PlaneObject;
import com.horizon.engine.graphics.object.scene.Scene;
import lombok.Data;

public @Data class TestObject {

    private GameEngine gameEngine;
    private Scene scene;

    public TestObject(GameEngine engine, Scene scene){
        this.gameEngine = engine;
        this.scene = scene;

        initialize();
    }

    public void initialize(){
        try{
            Texture gridTexture = new Texture("src/textures/grid.png");

            for(float x = -1.5f; x <= 1.5f; x += 0.5f){
                for(float z = -1.5f; z <= 1.5f; z += 0.5f){
                    PlaneObject plane = new PlaneObject(getGameEngine(), "Corner" + x + "," + z);
                    plane.getMesh().setMaterial(new Material(gridTexture));
                    plane.setScale(1.0f, 1.0f, 1.0f).setPosition(x * 4.0f, 0.0f, z * 4.0f);
                    scene.initializeObject(plane);
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
            return;
        }
    }
}
