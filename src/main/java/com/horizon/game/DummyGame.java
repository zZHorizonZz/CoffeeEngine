package com.horizon.game;

import com.horizon.engine.GameEngine;
import com.horizon.engine.IGameLogic;
import com.horizon.engine.Window;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.light.Light;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.data.Texture;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.light.PointLight;
import com.horizon.engine.graphics.light.SpotLight;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.objects.CubeObject;
import com.horizon.engine.graphics.object.objects.CylinderObject;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.input.other.MouseInput;
import com.horizon.engine.tool.FPSCounter;
import com.horizon.game.building.house.TestObject;
import com.horizon.game.controlls.ControllerManager;
import com.horizon.game.testing.TestManager;
import lombok.Getter;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class DummyGame implements IGameLogic {

    @Getter private GameEngine gameEngine;
    @Getter private Scene gameScene;
    @Getter private Canvas canvas;

    @Getter private FPSCounter fpsCounter;

    private final Renderer renderer;

    @Getter private TestManager testManager;
    @Getter private ControllerManager controllerManager;

    @Getter private CubeObject player;

    private float x,y,z;

    private Vector3f ambientLight;
    private PointLight pointLight;


    public DummyGame() {
        renderer = new Renderer();
    }

    @Override
    public void initialize() throws Exception {
        renderer.initialize(getGameEngine().getWindow());

        gameScene = new Scene(getGameEngine());
        gameScene.initialize();

        canvas = new Canvas(getGameEngine());

        testManager = new TestManager(getGameEngine());
        controllerManager = new ControllerManager(getGameEngine());
        controllerManager.initialize();

        new TestObject(getGameEngine(), getGameScene());

        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);

        // Point Light
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        PointLight pointLight = new PointLight(getGameEngine(), new Vector3f(1, 1, 1), lightPosition, lightIntensity, new Light.Attenuation(0.0f, 0.0f, 1.0f));

        // Spot Light
        lightPosition = new Vector3f(0, 0.0f, 10f);
        Vector3f coneDir = new Vector3f(0, 0, -1);
        float cutoff = (float) Math.cos(Math.toRadians(140));
        SpotLight spotLight = new SpotLight(getGameEngine(), new Vector3f(1, 1, 1), lightPosition, lightIntensity, new Light.Attenuation(0.0f, 0.0f, 1.0f), coneDir, cutoff);

        lightPosition = new Vector3f(-1, 0, 0);

        getGameScene().initializeObject(pointLight);
        getGameScene().initializeObject(spotLight);

        fpsCounter = new FPSCounter(getGameEngine());
    }

    @Override
    public void onInput(Window window, MouseInput mouseInput) {
        getControllerManager().onCameraMovement(window);

        z = 0;
        x = 0;

        if (window.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            z = -1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            z = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            x = -1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            x = 1;
        }
    }

    @Override
    public void onUpdate(float interval, MouseInput mouseInput) {
        getControllerManager().onUpdate(mouseInput);

        getFpsCounter().update();
        //player.setPosition(player.getPosition().x + x * 0.01f, player.getPosition().y, player.getPosition().z + z * 0.01f);
        //player.getMesh().updatePositions(UtilModel.upScaleModel(player.getMesh().getPositions(), x == 0 ? 1 : x * 0.01f, 1.0f, z == 0 ? 1 : z * 0.01f));
    }

    @Override
    public void onRender(Window window) {
        Camera camera = getControllerManager().getCamera();
        if(camera == null)
            return;

        renderer.render(window, camera, getGameScene(), getCanvas(), ambientLight);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameObject gameObject : getGameScene().getSceneObjects().values()) {
            if(!gameObject.getComponents().containsKey(ComponentType.MESH))
                continue;

            gameObject.getMesh().cleanUp();
            gameObject.getMesh().cleanUpTexture();
        }
    }

    public void assignGameEngine(GameEngine gameEngine){
        this.gameEngine = gameEngine;
    }
}
