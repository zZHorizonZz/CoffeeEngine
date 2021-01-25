package com.horizon.game.testing;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.input.other.InputHandler;
import com.horizon.engine.input.other.InputType;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class TestManager extends AbstractManager {

    public TestManager(GameEngine engine){
        super(engine, "Testing Manager");
    }

    @Override
    public void onEnable() {
        registerSelf();
    }

    @Override
    public void initialize() {

    }

    @InputHandler(name = "Testing Input T", input = GLFW.GLFW_KEY_T, inputType = InputType.KEY_DOWN)
    public void onKeyDown(){
        logAtInfo("Key 'T' pressed....");
    }

    @InputHandler(name = "Testing Input N", input = GLFW.GLFW_KEY_N, inputType = InputType.KEY_UP)
    public void onKeyUp(){
        logAtInfo("Key 'N' pressed....");
    }

    @InputHandler(name = "Close Game", input = GLFW.GLFW_KEY_ESCAPE, inputType = InputType.KEY_UP)
    public void onGameClose(){
        logAtInfo("Closing the game window...");
        glfwSetWindowShouldClose(getGameEngine().getWindow().getWindowHandle(), true);
    }
}
