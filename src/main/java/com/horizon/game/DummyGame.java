package com.horizon.game;

import com.horizon.engine.AbstractGameLogic;
import com.horizon.engine.Window;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.light.Light;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.light.PointLight;
import com.horizon.engine.graphics.light.SpotLight;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.render.Renderer;
import com.horizon.engine.input.other.MouseInput;
import com.horizon.engine.tool.FPSCounter;
import com.horizon.game.controlls.ControllerManager;
import com.horizon.game.testing.TestManager;
import lombok.Getter;
import org.joml.Vector3f;

public class DummyGame extends AbstractGameLogic {

    @Getter private FPSCounter fpsCounter;

    private final Renderer renderer;

    @Getter private TestManager testManager;
    @Getter private ControllerManager controllerManager;

    private Vector3f ambientLight;
    private PointLight pointLight;


    public DummyGame() {
        renderer = new Renderer();
    }

    @Override
    public void onEnable() throws Exception {
        renderer.initialize(getGameEngine().getWindow());

        setScene(new Scene(getGameEngine()));
        getScene().initialize();

        setCanvas(new Canvas(getGameEngine()));

        testManager = new TestManager(getGameEngine());
        controllerManager = new ControllerManager(getGameEngine());
        controllerManager.initialize();

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

        getScene().instantiate(pointLight);
        getScene().instantiate(spotLight);

        fpsCounter = new FPSCounter(getGameEngine());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onInput(Window window, MouseInput mouseInput) {
        getControllerManager().onCameraMovement(window);
    }

    @Override
    public void onUpdate(float interval, MouseInput mouseInput) {
        getControllerManager().onUpdate(mouseInput);

        getFpsCounter().update();
        //player.setPosition(player.getPosition().x + x * 0.01f, player.getPosition().y, player.getPosition().z + z * 0.01f);
        //player.getModel().updatePositions(UtilModel.upScaleModel(player.getModel().getPositions(), x == 0 ? 1 : x * 0.01f, 1.0f, z == 0 ? 1 : z * 0.01f));
    }

    @Override
    public void onRender(Window window) {
        Camera camera = getControllerManager().getCamera();
        if(camera == null)
            return;

        renderer.render(window, camera, getScene(), getCanvas());
    }
}
