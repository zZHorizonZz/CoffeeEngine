package com.horizon.kingdom_builder;

import com.horizon.engine.AbstractGameLogic;
import com.horizon.engine.Window;
import com.horizon.engine.common.Color;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.debug.Debugger;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.objects.ModelObject;
import com.horizon.engine.graphics.object.primitive.PrimitiveObject;
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

    @Getter private Map gameMap;

    @Getter private ModelObject testObject;

    public KingdomBuilder() {
        renderer = new Renderer();
    }

    @Override
    public void initialize() throws Exception {
        renderer.initialize(getGameEngine().getWindow());

        setScene(new Scene(getGameEngine()));
        setSceneInitialized(getScene().initialize());

        setCanvas(new Canvas(getGameEngine()));

        controllerManager = new ControllerManager(getGameEngine());
        buildingManager = new BuildingManager(this);

        preLoadMeshes();
        loadLights();

        //gameMap = new Map(this, 20, 20);

        ModelObject cubeObject = getScene().instantiate(PrimitiveObject.CUBE);
        cubeObject.setRotation(0.0f, 45.0f, 0.0f);
        cubeObject.setPosition(1.0f, 2.5f, 1.0f);

        testObject = getScene().instantiate(PrimitiveObject.CUBE);
        testObject.getMesh().setMaterial(new Material(Color.RED));

        ModelObject planeObject = (ModelObject) getScene().instantiate(PrimitiveObject.PLANE);
        planeObject.getMesh().setMaterial(new Material(Color.GREEN));
        planeObject.setPosition(0.0f, -1.5f, 0.0f);
        planeObject.setRotation(0.0f, 45.0f, 0.0f);
        planeObject.setScale(3.0f, 1.0f, 3.0f);

        controllerManager.initialize();
        buildingManager.initialize();
    }

    protected void preLoadMeshes() {
        getGameEngine().getAssetManager().loadModel("/models", "roof01.obj");
        getGameEngine().getAssetManager().loadModel("/models", "room01.obj");
        getGameEngine().getAssetManager().loadModel("/models", "forest.obj");
    }

    protected void loadLights() {
        getScene().getSceneLight().setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));

        Vector3f lightDirection = new Vector3f(1, 1, 1);
        getScene().getSceneLight().getDirectionalLight().getDirectionalLight().setDirection(lightDirection);
    }

    @Override
    public void onInput(Window window, MouseInput mouseInput) {
        getControllerManager().onCameraMovement(window);
    }

    @Override
    public void onUpdate(float interval, MouseInput mouseInput) {
        controllerManager.onUpdate(mouseInput);
        //buildingManager.update();
    }

    @Override
    public void onRender(Window window) {
        Camera camera = getControllerManager().getCamera();
        if(camera == null)
            return;

        renderer.render(window, camera, getScene(), getCanvas());
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
