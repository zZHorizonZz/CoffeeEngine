package com.horizon.game.controlls;

import com.horizon.engine.AbstractGameLogic;
import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.Window;
import com.horizon.engine.common.RaycastDisplay;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.hud.text.TextView;
import com.horizon.engine.input.other.InputHandler;
import com.horizon.engine.input.other.InputType;
import com.horizon.engine.input.other.MouseInput;
import com.horizon.game.DummyGame;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class ControllerManager extends AbstractManager {

    @Getter private final AbstractGameLogic game;

    @Getter private Camera camera;
    @Getter private RaycastDisplay raycast;

    protected Vector3f cameraMovement;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;

    public ControllerManager(GameEngine gameEngine){
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

    public void onCameraMovement(Window window){
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

        raycast = new RaycastDisplay();
    }

    public void onUpdate(MouseInput mouseInput) {
        if(camera == null)
            return;

        camera.movePosition(cameraMovement.x * CAMERA_POS_STEP, cameraMovement.y * CAMERA_POS_STEP, cameraMovement.z * CAMERA_POS_STEP);

        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplayVector();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        camera.updateViewMatrix();

        if (mouseInput.isLeftButtonPressed()) {
            GameObject gameObject = raycast.selectGameObject(getGame().getScene().getSceneObjects().values(), getGame().getGameEngine().getWindow(), mouseInput.getCurrentPosition(), getCamera());
        }
    }

    @InputHandler(name = "Close Game", input = GLFW.GLFW_KEY_ESCAPE, inputType = InputType.KEY_UP)
    public void onGameClose(){
        logAtInfo("Closing the game window...");
        glfwSetWindowShouldClose(getGameEngine().getWindow().getWindowHandle(), true);
    }
}
