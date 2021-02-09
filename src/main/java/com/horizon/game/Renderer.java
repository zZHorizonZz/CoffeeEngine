package com.horizon.game;

import com.horizon.engine.Window;
import com.horizon.engine.event.event.ScreenResizeEvent;
import com.horizon.engine.graphics.data.Transformation;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.shader.shader.graphic.GraphicShader;
import com.horizon.engine.graphics.shader.shader.hud.HudShader;
import lombok.Getter;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    @Getter private Window window;
    @Getter private final Transformation transformation;

    @Getter private GraphicShader graphicShader;
    @Getter private HudShader hudShader;

    @Getter private final float specularPower;

    public Renderer() {
        transformation = new Transformation();
        specularPower = 10f;
    }

    public void initialize(Window window) throws Exception {
        this.window = window;

        setUpSceneShader();
        setUpHudShader();
    }

    public void setUpSceneShader() throws Exception {
        graphicShader = new GraphicShader("material", transformation);
        graphicShader.initialize();
    }

    public void setUpHudShader(){
        hudShader = new HudShader(transformation);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, Scene scene, Canvas canvas, Vector3f ambientLight) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, Window.getWidth(), Window.getHeight());

            if(getWindow().getGameEngine().getEventManager() != null)
                getWindow().getGameEngine().getEventManager().callEvent(new ScreenResizeEvent(Window.getWidth(), Window.getHeight()));

            window.setResized(false);
        }

        window.updateProjectionMatrix();

        graphicShader.render(window, camera, scene, canvas, ambientLight);
        hudShader.render(window, camera, scene, canvas, ambientLight);
    }

    public void cleanup() {
        if (graphicShader != null) {
            graphicShader.cleanUp();
        }
        if (hudShader != null) {
            hudShader.cleanUp();
        }
    }
}