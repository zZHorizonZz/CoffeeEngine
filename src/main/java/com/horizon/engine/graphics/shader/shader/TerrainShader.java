package com.horizon.engine.graphics.shader.shader;

import com.horizon.engine.Window;
import com.horizon.engine.common.file.File;
import com.horizon.engine.graphics.data.Transformation;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.object.terrain.Terrain;
import com.horizon.engine.graphics.render.Renderer;
import com.horizon.engine.graphics.shader.ShaderProgram;
import com.horizon.engine.graphics.shader.uniform.Uniform;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

public class TerrainShader extends ShaderProgram {

    private Transformation transformation;

    @Getter @Setter private Terrain terrain;

    public TerrainShader(Renderer renderer, Transformation transformation) {
        super(renderer, new File("shaders/graphic/terrain_vertex.vs"), new File("shaders/graphic/terrain_fragment.fs"));
        super.storeAllUniformLocations(new Uniform("projectionMatrix"), new Uniform("lightDirection"),
                new Uniform("lightColour"), new Uniform("lightBias"));

        this.transformation = transformation;
    }

    @Override
    public void render(Window window, Camera camera, Scene scene, Canvas canvas) {
        if(terrain == null)
            return;

        start();

        setUniformVector2f("lightBias", new Vector2f(0.3f, 0.8f));
        setUniformVector3f("lightDirection", scene.getSceneLight().getDirectionalLight().getDirectionalLight().getDirection());
        setUniformVector3f("lightColour", scene.getSceneLight().getDirectionalLight().getDirectionalLight().getColor());
        setUniformMatrix4("projectionMatrix", window.updateProjectionMatrix());

        terrain.getMesh().render();

        stop();
    }

    @Override
    public void initialize() {

    }
}
