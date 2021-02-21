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
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public class ControllerManager extends AbstractManager {

    @Getter private final AbstractGameLogic game;

    @Getter private Camera camera;

    @Getter private final Quaternionf mouseMovement;
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
        mouseMovement = new Quaternionf(0, 0, 0, 0);
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
        camera.setRotation(45.0f, 0.0f, 0.0f);

        panSpeed = ApplicationData.getPanSpeed();
    }

    //TODO: This can be added to tool manager for developer mode.
    public void onCameraMovement(Window window) {
        panMovement.zero();
        mouseMovement.set(0, 0, 0, 0);

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

        if (window.isKeyPressed(GLFW.GLFW_KEY_Q)) {
            mouseMovement.x = MOUSE_SENSITIVITY;
            mouseMovement.z = MOUSE_SENSITIVITY;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_E)) {
            panMovement.x = -MOUSE_SENSITIVITY;
            mouseMovement.z = -MOUSE_SENSITIVITY;
        }

        test(window);
    }

    public void test(Window window) {
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

        updateOrigin(panMovement.x(), panMovement.y(), panMovement.z());
        camera.movePosition(panMovement.x(), panMovement.y(), panMovement.z());

        checkMouse(mouseInput);

        //Testing

        Vector2f rotVec = mouseInput.getDisplayVector();
        camera.moveRotation(mouseMovement.x(), 0, mouseMovement.z());

        camera.updateViewMatrix();
    }

    public void updateOrigin(float offsetX, float offsetY, float offsetZ) {
        if (offsetZ != 0) {
            cameraOrigin.x += (float)Math.sin(Math.toRadians(camera.getRotation().y)) * -1.0f * offsetZ;
            cameraOrigin.z += (float)Math.cos(Math.toRadians(camera.getRotation().y)) * offsetZ;
        }
        if (offsetX != 0) {
            cameraOrigin.x += (float)Math.sin(Math.toRadians(camera.getRotation().y - 90)) * -1.0f * offsetX;
            cameraOrigin.z += (float)Math.cos(Math.toRadians(camera.getRotation().y - 90)) * offsetX;
        }
        cameraOrigin.y += offsetY;
    }

    public void checkMouse(MouseInput mouseInput) {
        if (mouseInput.isLeftButtonPressed())
            getGameEngine().getEventManager().callEvent(new MouseClickEvent(GLFW_MOUSE_BUTTON_1));

        if (mouseInput.isRightButtonPressed())
            getGameEngine().getEventManager().callEvent(new MouseClickEvent(GLFW_MOUSE_BUTTON_2));
    }

    /*private void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = angleAroundPlayer.get();
        position.x = Configs.TERRAIN_SIZE / 2f + (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        position.y = verticDistance + Y_OFFSET;
        position.z = Configs.TERRAIN_SIZE / 2f + (float) (horizDistance * Math.cos(Math.toRadians(theta)));
    }

    /**
     * @return The horizontal distance of the camera from the origin.
     */
    /*private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer.get() * Math.cos(Math.toRadians(pitch.get())));
    }

    /**
     * @return The height of the camera from the aim point.
     */
    /*private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer.get() * Math.sin(Math.toRadians(pitch.get())));
    }

    /**
     * Calculate the pitch and change the pitch if the user is moving the mouse
     * up or down with the LMB pressed.
     */
   /* private void calculatePitch() {
        if (Mouse.isButtonDown(0)) {
            float pitchChange = Mouse.getDY() * PITCH_SENSITIVITY;
            pitch.increaseTarget(-pitchChange);
            clampPitch();
        }
        pitch.update(1f / 60);
    }

    private void calculateZoom() {
        float targetZoom = distanceFromPlayer.getTarget();
        float zoomLevel = Mouse.getDWheel() * 0.0008f * targetZoom;
        targetZoom -= zoomLevel;
        if (targetZoom < 1) {
            targetZoom = 1;
        }
        distanceFromPlayer.setTarget(targetZoom);
        distanceFromPlayer.update(0.01f);
    }

    /**
     * Calculate the angle of the camera around the player (when looking down at
     * the camera from above). Basically the yaw. Changes the yaw when the user
     * moves the mouse horizontally with the LMB down.
     */
   /* private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * YAW_SENSITIVITY;
            angleAroundPlayer.increaseTarget(-angleChange);
        }
        angleAroundPlayer.update(1f / 60);
    }

    /**
     * Ensures the camera's pitch isn't too high or too low.
     */
    /*private void clampPitch() {
        if (pitch.getTarget() < 0) {
            pitch.setTarget(0);
        } else if (pitch.getTarget() > MAX_PITCH) {
            pitch.setTarget(MAX_PITCH);
        }
    }*/

    // This will be removed in future realeases.
    @InputHandler(name = "Close Game", input = GLFW.GLFW_KEY_ESCAPE, inputType = InputType.KEY_UP)
    public void onGameClose() {
        logAtInfo("Closing the game window...");
        glfwSetWindowShouldClose(getGameEngine().getWindow().getWindowHandle(), true);
    }
}
