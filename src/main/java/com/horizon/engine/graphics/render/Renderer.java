package com.horizon.engine.graphics.render;

import com.horizon.engine.Window;
import com.horizon.engine.data.ApplicationData;
import com.horizon.engine.event.event.ScreenResizeEvent;
import com.horizon.engine.graphics.data.Transformation;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.object.terrain.Terrain;
import com.horizon.engine.graphics.shader.shader.DepthShader;
import com.horizon.engine.graphics.shader.shader.GraphicShader;
import com.horizon.engine.graphics.shader.shader.HudShader;
import com.horizon.engine.graphics.shader.shader.TerrainShader;
import lombok.Getter;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    @Getter private Window window;
    @Getter private final Transformation transformation;

    @Getter private GraphicShader graphicShader;
    @Getter private HudShader hudShader;
    @Getter private DepthShader depthShader;
    @Getter private TerrainShader terrainShader;

    @Getter private final FrustumCullingFilter frustumCullingFilter;
    @Getter private final List<GameObject> filteredItems;

    @Getter private final float specularPower;

    public Renderer() {
        transformation = new Transformation();
        specularPower = 10f;
        frustumCullingFilter = new FrustumCullingFilter();
        filteredItems = new ArrayList<>();
    }

    public void initialize(Window window) throws Exception {
        this.window = window;

        setUpSceneShader();
        setUpTerrainShader();
        setUpHudShader();
        setUpDepthShader();
    }

    public void setUpSceneShader() {
        graphicShader = new GraphicShader(this, transformation);
        graphicShader.initialize();
    }

    public void setUpHudShader() {
        hudShader = new HudShader(this,transformation);
        hudShader.initialize();
    }

    public void setUpDepthShader() {
        depthShader = new DepthShader(this,transformation);
        depthShader.initialize();
    }

    public void setUpTerrainShader() {
        terrainShader = new TerrainShader(this,transformation);
        terrainShader.initialize();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, Scene scene, Canvas canvas) {
        clear();

        if (ApplicationData.isFrustumCulling()) {
            frustumCullingFilter.updateFrustum(window.getProjectionMatrix(), camera.getViewMatrix());
            frustumCullingFilter.filterObject(scene.getGameEngine().getAssetManager().getNonInstancedMeshes());
            frustumCullingFilter.filterList(scene.getGameEngine().getAssetManager().getInstancedMeshes());
        }

        depthShader.render(window, camera, scene, canvas);

        glViewport(0, 0, Window.getWidth(), Window.getHeight());

        if (window.isResized()) {
            if(getWindow().getGameEngine().getEventManager() != null)
                getWindow().getGameEngine().getEventManager().callEvent(new ScreenResizeEvent(Window.getWidth(), Window.getHeight()));

            window.setResized(false);
        }

        window.updateProjectionMatrix();

        if(!scene.getGameEngine().getGameLogic().isSceneInitialized())
            return;

        graphicShader.render(window, camera, scene, canvas);
        terrainShader.render(window, camera, scene, canvas);
        hudShader.render(window, camera, scene, canvas);
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