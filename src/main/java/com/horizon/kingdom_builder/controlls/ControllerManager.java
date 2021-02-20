package com.horizon.kingdom_builder.controlls;

import com.horizon.engine.AbstractGameLogic;
import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.Window;
import com.horizon.engine.data.ApplicationData;
import com.horizon.engine.event.event.MouseClickEvent;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.input.other.InputHandler;
import com.horizon.engine.input.other.InputType;
import com.horizon.engine.input.other.MouseInput;
import com.horizon.kingdom_builder.KingdomBuilder;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public class ControllerManager extends AbstractManager {

    @Getter private final AbstractGameLogic game;

    @Getter private Camera camera;

    @Getter private final Vector3f panMovement;
    @Getter private final Vector3f cameraOrigin;
    @Getter private float panSpeed;

    private static final float MOUSE_SENSITIVITY = 0.2f;

    // Testing
    private float lightAngle = 0.0f;
    private float modelAngle = 0.0f;

    public ControllerManager(GameEngine gameEngine) {
        super(gameEngine, "Controller Manager");

        game = gameEngine.getGameLogic();
        panMovement = new Vector3f(0, 0, 0);
        cameraOrigin = new Vector3f(0, 0, 0);
    }

    @Override
    public void onEnable() {
        registerSelf();
    }

    @Override
    public void initialize() {
        camera = game.getScene().getSceneCamera();
        camera.setPosition(0, 25, 0);

        panSpeed = ApplicationData.getPanSpeed();
    }

    //TODO: This can be added to tool manager for developer mode.
    public void onCameraMovement(Window window) {
        panMovement.zero();
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            panMovement.z = -1 * panSpeed * getGameEngine().getTimer().getDeltaTime();
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            panMovement.z = 1 * panSpeed * getGameEngine().getTimer().getDeltaTime();
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            panMovement.x = -1 * panSpeed * getGameEngine().getTimer().getDeltaTime();
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            panMovement.x = 1 * panSpeed * getGameEngine().getTimer().getDeltaTime();
        }

        // Testing
        float angleInc = 0.0f;
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            angleInc = 0.5f;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            angleInc = -0.5f;
        }

        if (lightAngle + angleInc >= 0 && lightAngle + angleInc <= 180) {
            lightAngle += angleInc;
        }

        float modelAngleInc = 0.0f;
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            modelAngleInc = 0.5f;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            modelAngleInc = -0.5f;
        }

        if (modelAngle + modelAngleInc >= 0 && modelAngle + modelAngleInc <= 180) {
            modelAngle += modelAngleInc;
        }

        ((KingdomBuilder) getGame()).getTestObject().setRotation(0.0f, modelAngle, 0.0f);

        float zValue = (float) Math.cos(Math.toRadians(lightAngle));
        float yValue = (float) Math.sin(Math.toRadians(lightAngle));
        Vector3f lightDirection = getGameEngine().getScene().getSceneLight().getDirectionalLight().getDirectionalLight().getDirection();
        lightDirection.x = 0;
        lightDirection.y = yValue;
        lightDirection.z = zValue;
        lightDirection.normalize();
        getGameEngine().getScene().getSceneLight().getDirectionalLight().getDirectionalLight().setDirection(lightDirection);
    }

    public void onUpdate(MouseInput mouseInput) {
        if (camera == null)
            return;

        camera.movePosition(panMovement.x, panMovement.y, panMovement.z);

        if (mouseInput.isLeftButtonPressed()) {
            getGameEngine().getEventManager().callEvent(new MouseClickEvent(GLFW_MOUSE_BUTTON_1));
            //GameObject gameObject = raycast.selectGameObject(getGame().getScene().getSceneObjects().values(), getGame().getGameEngine().getWindow(), mouseInput.getCurrentPosition(), getCamera());
        }

        if (mouseInput.isRightButtonPressed()) {
            getGameEngine().getEventManager().callEvent(new MouseClickEvent(GLFW_MOUSE_BUTTON_2));
        } else {
            Vector2f rotVec = mouseInput.getDisplayVector();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        camera.updateViewMatrix();
    }

    @InputHandler(name = "Close Game", input = GLFW.GLFW_KEY_ESCAPE, inputType = InputType.KEY_UP)
    public void onGameClose() {
        logAtInfo("Closing the game window...");
        glfwSetWindowShouldClose(getGameEngine().getWindow().getWindowHandle(), true);
    }
}
