package com.horizon.engine;

import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.mesh.Mesh;
import com.horizon.engine.component.component.mesh.TerrainMesh;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.render.Renderer;
import com.horizon.engine.input.other.MouseInput;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractGameLogic {

    @Getter @Setter private GameEngine gameEngine;
    @Getter @Setter private Renderer renderer;

    @Getter @Setter private Scene scene;
    @Getter @Setter private Canvas canvas;

    @Getter @Setter private boolean sceneInitialized = false;

    public abstract void onEnable() throws Exception;

    public abstract void onDisable();

    public abstract void onInput(Window window, MouseInput mouseInput);

    public abstract void onUpdate(float interval, MouseInput mouseInput);

    public abstract void onRender(Window window);

    public void cleanup(Renderer renderer) {
        renderer.cleanup();
        for (GameObject gameObject : getScene().getSceneObjects().values()) {
            if(!gameObject.getComponents().containsKey(ComponentType.MESH))
                continue;

            if(gameObject.getMesh() != null)
                gameObject.getMesh().cleanUp();

            if(gameObject.getComponent(ComponentType.MESH) instanceof TerrainMesh) {
                ((TerrainMesh) gameObject.getComponent(ComponentType.MESH)).cleanUp();
            }

            gameObject.getMesh().cleanUpTexture();
        }
    }
}
