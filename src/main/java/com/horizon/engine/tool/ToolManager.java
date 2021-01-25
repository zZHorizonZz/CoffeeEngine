package com.horizon.engine.tool;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.input.other.InputHandler;
import com.horizon.engine.input.other.InputType;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class ToolManager extends AbstractManager {

    protected Boolean polygonMode = false;

    public ToolManager(GameEngine engine) {
        super(engine, "Tool Manager");
    }

    @Override
    public void onEnable() {
        registerSelf();
    }

    @Override
    public void initialize() {

    }

    @InputHandler(name = "Polygon Mode", input = GLFW.GLFW_KEY_G, inputType = InputType.KEY_DOWN)
    public void onInput(){
        if(!polygonMode) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            polygonMode = true;
        }else{
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            polygonMode = false;
        }
    }
}
