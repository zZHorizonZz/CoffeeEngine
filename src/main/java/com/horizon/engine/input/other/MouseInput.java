package com.horizon.engine.input.other;

import com.horizon.engine.Window;
import lombok.Getter;
import org.joml.Vector2d;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    @Getter private final Vector2d previousPosition;
    @Getter private final Vector2d currentPosition;
    @Getter private final Vector2f displayVector;

    private boolean inWindow = false;

    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    public MouseInput() {
        previousPosition = new Vector2d(-1, -1);
        currentPosition = new Vector2d(0, 0);
        displayVector = new Vector2f();
    }

    public void init(Window window) {
        glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
            currentPosition.x = xpos;
            currentPosition.y = ypos;
        });
        glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> {
            inWindow = entered;
        });
        glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    public void input(Window window) {
        displayVector.x = 0;
        displayVector.y = 0;
        if (previousPosition.x > 0 && previousPosition.y > 0 && inWindow) {
            double deltax = currentPosition.x - previousPosition.x;
            double deltay = currentPosition.y - previousPosition.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displayVector.y = (float) deltax;
            }
            if (rotateY) {
                displayVector.x = (float) deltay;
            }
        }
        previousPosition.x = currentPosition.x;
        previousPosition.y = currentPosition.y;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
