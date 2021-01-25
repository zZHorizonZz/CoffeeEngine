package com.horizon.game.controlls;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.Window;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.input.other.MouseInput;
import com.horizon.game.DummyGame;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class ControllerManager extends AbstractManager {

    private final DummyGame game;

    @Getter
    private Camera camera;

    protected Vector3f cameraMovement;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;

    public ControllerManager(GameEngine gameEngine){
        super(gameEngine, "Controller Manager");

        game = (DummyGame) gameEngine.getGameLogic();
        cameraMovement = new Vector3f(0, 0, 0);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void initialize() {
        camera = game.getGameScene().getSceneCamera();
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
    }

    public void onUpdate(MouseInput mouseInput) {
        if(camera == null)
            return;

        camera.movePosition(cameraMovement.x * CAMERA_POS_STEP, cameraMovement.y * CAMERA_POS_STEP, cameraMovement.z * CAMERA_POS_STEP);

        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
    }
}
