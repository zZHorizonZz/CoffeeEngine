package com.horizon.engine.tool;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.Window;
import com.horizon.engine.data.ApplicationData;
import com.horizon.engine.hud.hud.DeveloperMenu;
import com.horizon.engine.input.other.InputHandler;
import com.horizon.engine.input.other.InputType;
import lombok.Getter;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class ToolManager extends AbstractManager {

    protected Boolean polygonMode = false;

    @Getter private DeveloperMenu developerMenu;

    public ToolManager(GameEngine engine) {
        super(engine, "Tool Manager");
    }

    @Override
    public void onEnable() {
        registerSelf();
    }

    @Override
    public void initialize() {
        if(ApplicationData.isDeveloperMenu())
            developerMenu = (DeveloperMenu) getGameEngine().getHudManager().createMenu(new DeveloperMenu(getGameEngine().getCanvas()));
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

    @InputHandler(name = "Developer Menu", input = GLFW.GLFW_KEY_F5, inputType = InputType.KEY_DOWN)
    public void onDeveloperMenu(){
        if(!developerMenu.isShowMenu()) {
            developerMenu.setShowMenu(true);
        }else{
            developerMenu.setShowMenu(false);
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
