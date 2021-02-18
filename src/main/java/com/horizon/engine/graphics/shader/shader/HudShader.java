package com.horizon.engine.graphics.shader.shader;

import com.horizon.engine.Window;
import com.horizon.engine.common.file.File;
import com.horizon.engine.component.component.mesh.DisplayMesh;
import com.horizon.engine.component.component.mesh.Mesh;
import com.horizon.engine.graphics.data.Transformation;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.hud.HudObject;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.hud.objects.BoxView;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.render.Renderer;
import com.horizon.engine.graphics.shader.ShaderProgram;
import com.horizon.engine.graphics.shader.uniform.Uniform;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class HudShader extends ShaderProgram {

    private final Transformation transformation;

    public HudShader(Renderer renderer, Transformation transformation) {
        super(renderer, new File("shaders/graphic/ui_vertex.vs"), new File("shaders/graphic/ui_fragment.fs"));
        super.storeAllUniformLocations(new Uniform("projModelMatrix"), new Uniform("colour"), new Uniform("hasTexture"));

        this.transformation = transformation;
    }

    @Override
    public void render(Window window, Camera camera, Scene scene, Canvas canvas) {
        start();

        Matrix4f orthographic2DProjectionMatrix = transformation.getOrtho2DProjectionMatrix(0, Window.getWidth(), Window.getHeight(), 0);

        glDisable(GL_DEPTH_TEST);

        for (HudObject gameObject : canvas.getCanvasObjects().values()) {
            DisplayMesh mesh = gameObject.getDisplayMesh();

            Matrix4f projModelMatrix = transformation.buildOrthographicProjectionModelMatrix(gameObject, orthographic2DProjectionMatrix);
            setUniformMatrix4("projModelMatrix", projModelMatrix);
            setUniformVector4f("colour", mesh.getMaterial().getAmbientColour().toVector4f());
            setUniformInt("hasTexture", mesh.getMaterial().isTextured() ? 1 : 0);

            mesh.update();
        }

        glEnable(GL_DEPTH_TEST);

        stop();
    }

    @Override
    public void initialize() {

    }
}
