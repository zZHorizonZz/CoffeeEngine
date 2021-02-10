package com.horizon.kingdom_builder;

import com.horizon.engine.AbstractGameLogic;
import com.horizon.engine.Window;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.light.PointLight;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.render.Renderer;
import com.horizon.engine.input.other.MouseInput;
import com.horizon.game.testing.TestManager;
import com.horizon.kingdom_builder.assets.Map;
import com.horizon.kingdom_builder.controlls.ControllerManager;
import com.horizon.kingdom_builder.gameplay.building.BuildingManager;
import lombok.Getter;
import org.joml.Vector3f;

public class KingdomBuilder  extends AbstractGameLogic {

    private final Renderer renderer;

    @Getter private TestManager testManager;
    @Getter private ControllerManager controllerManager;
    @Getter private BuildingManager buildingManager;

    private Vector3f ambientLight;
    private PointLight pointLight;

    @Getter private Map gameMap;


    public KingdomBuilder() {
        renderer = new Renderer();
    }

    @Override
    public void initialize() throws Exception {
        renderer.initialize(getGameEngine().getWindow());

        setScene(new Scene(getGameEngine()));
        getScene().initialize();

        setCanvas(new Canvas(getGameEngine()));

        controllerManager = new ControllerManager(getGameEngine());
        buildingManager = new BuildingManager(this);

        preLoadMeshes();
        loadLights();

        gameMap = new Map(this, 20, 20);

        controllerManager.initialize();
        buildingManager.initialize();
    }

    protected void preLoadMeshes() {
        getGameEngine().getAssetManager().loadMesh("/models", "roof01.obj");
        getGameEngine().getAssetManager().loadMesh("/models", "room01.obj");
        getGameEngine().getAssetManager().loadMesh("/models", "tree.obj");
    }

    protected void loadLights() {
        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);

        getScene().getDirectionalLight().getDirectionalLight().setDirection(ambientLight);
    }

    @Override
    public void onInput(Window window, MouseInput mouseInput) {
        getControllerManager().onCameraMovement(window);
    }

    @Override
    public void onUpdate(float interval, MouseInput mouseInput) {
        getControllerManager().onUpdate(mouseInput);
        getBuildingManager().update();
    }

    @Override
    public void onRender(Window window) {
        Camera camera = getControllerManager().getCamera();
        if(camera == null)
            return;

        renderer.render(window, camera, getScene(), getCanvas(), ambientLight);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameObject gameObject : getScene().getSceneObjects().values()) {
            if(!gameObject.getComponents().containsKey(ComponentType.MESH))
                continue;

            gameObject.getMesh().cleanUp();
            gameObject.getMesh().cleanUpTexture();
        }
    }
}
