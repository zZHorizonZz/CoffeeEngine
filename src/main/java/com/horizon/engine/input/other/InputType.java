package com.horizon.engine.input.other;

import lombok.Getter;
import org.lwjgl.glfw.GLFW;

public enum InputType {

    KEY_DOWN(GLFW.GLFW_PRESS),
    KEY_UP(GLFW.GLFW_RELEASE),
    KEY_HOLD(GLFW.GLFW_REPEAT);

    @Getter
    private final Integer action;

    InputType(Integer action){
        this.action = action;
    }
}
