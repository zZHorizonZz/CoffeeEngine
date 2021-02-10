package com.horizon.kingdom_builder.controlls;

import com.horizon.engine.AbstractGameLogic;
import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.Window;
import com.horizon.engine.common.Raycast;
import com.horizon.engine.common.RaycastDisplay;
import com.horizon.engine.event.event.MouseClickEvent;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.input.other.InputHandler;
import com.horizon.engine.input.other.InputType;
import com.horizon.engine.input.other.MouseInput;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;

import static org.lwjgl.glfw.GLFW.*;

public class ControllerManager extends AbstractManager {

    @Getter private final AbstractGameLogic game;

    @Getter private Camera camera;

    protected Vector3f cameraMovement;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;

    public ControllerManager(GameEngine gameEngine) {
        super(gameEngine, "Controller Manager");

        game = gameEngine.getGameLogic();
        cameraMovement = new Vector3f(0, 0, 0);
    }

    @Override
    public void onEnable() {
        registerSelf();
    }

    @Override
    public void initialize() {
        camera = game.getScene().getSceneCamera();
    }

    public void onCameraMovement(Window window) {
        cameraMovement.set(0, 0, 0);
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            cameraMovement.z = -1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            cameraMovement.z = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            cameraMovement.x = -1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            cameraMovement.x = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_Z)) {
            cameraMovement.y = -1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_X)) {
            cameraMovement.y = 1;
        }
    }

    public void onUpdate(MouseInput mouseInput) {
        if (camera == null)
            return;

        camera.movePosition(cameraMovement.x * CAMERA_POS_STEP, cameraMovement.y * CAMERA_POS_STEP, cameraMovement.z * CAMERA_POS_STEP);

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
