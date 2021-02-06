package com.horizon.engine.tool;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.Window;
import com.horizon.engine.input.other.InputHandler;
import com.horizon.engine.input.other.InputType;
import org.joml.Vector2f;
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
    public void onPolygonMode(){
        if(!polygonMode) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            polygonMode = true;
        }else{
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            polygonMode = false;
        }
    }

    //TODO - Automatically update position of hud objects.

    @InputHandler(name = "Developer Data", input = GLFW.GLFW_KEY_J, inputType = InputType.KEY_DOWN)
    public void onDeveloperData(){
        Vector2f windowSize = getGameEngine().getWindow().getScreenSize();

        GameEngine.getLogger().atInfo().log("Window Frame Size -> Width: " + windowSize.x() + " Height: " + windowSize.y());
        GameEngine.getLogger().atInfo().log("Window Size -> Width: " + Window.getWidth() + " Height: " + Window.getHeight());
        GameEngine.getLogger().atInfo().log("Window Resize -> Resized: " + getGameEngine().getWindow().isResized());
    }
}
