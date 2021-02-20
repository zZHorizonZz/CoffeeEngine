package com.horizon.engine.graphics.shader.shader;

import com.horizon.engine.Window;
import com.horizon.engine.common.file.File;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.mesh.TerrainMesh;
import com.horizon.engine.debug.Debugger;
import com.horizon.engine.graphics.data.Transformation;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.object.terrain.Terrain;
import com.horizon.engine.graphics.render.Renderer;
import com.horizon.engine.graphics.shader.ShaderProgram;
import com.horizon.engine.graphics.shader.uniform.Uniform;
import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class TerrainShader extends ShaderProgram {

    @Getter private final Transformation transformation;

    public TerrainShader(Renderer renderer, Transformation transformation) {
        super(renderer, new File("shaders/graphic/terrain_vertex.vs"), new File("shaders/graphic/terrain_fragment.fs"));
        super.storeAllUniformLocations(new Uniform("projectionMatrix"), new Uniform("viewMatrix"), new Uniform("lightDirection"),
                new Uniform("lightColour"), new Uniform("lightBias"), new Uniform("modelMatrix"));

        this.transformation = transformation;
    }

    @Override
    public void render(Window window, Camera camera, Scene scene, Canvas canvas) {
        if(scene.getTerrain() == null)
            return;

        start();

        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = window.getProjectionMatrix();

        setUniformMatrix4("viewMatrix", viewMatrix);
        setUniformMatrix4("projectionMatrix", projectionMatrix);

        setUniformVector2f("lightBias", new Vector2f(0.5f, 0.6f));
        setUniformVector3f("lightDirection", scene.getSceneLight().getDirectionalLight().getDirectionalLight().getDirection().negate());
        setUniformVector3f("lightColour", scene.getSceneLight().getDirectionalLight().getDirectionalLight().getColor());

        Matrix4f modelMatrix = transformation.buildModelMatrix(scene.getTerrain());
        setUniformMatrix4("modelMatrix", modelMatrix);

        ((TerrainMesh) scene.getTerrain().getComponent(ComponentType.MESH)).render();

        stop();
    }

    @Override
    public void initialize() {

    }
}
