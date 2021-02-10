package com.horizon.engine.graphics.shader.shader.hud;

import com.horizon.engine.Window;
import com.horizon.engine.common.file.File;
import com.horizon.engine.component.component.Mesh;
import com.horizon.engine.graphics.data.Transformation;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.hud.objects.BoxView;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.shader.ShaderProgram;
import com.horizon.engine.graphics.shader.uniform.Uniform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class HudShader extends ShaderProgram {

    private final Transformation transformation;

    public HudShader(Transformation transformation) {
        super(new File("shaders/graphic/ui_vertex.vs"), new File("shaders/graphic/ui_fragment.fs"));
        super.storeAllUniformLocations(new Uniform("projModelMatrix"), new Uniform("colour"), new Uniform("hasTexture"), new Uniform("uiWidth"), new Uniform("uiHeight"), new Uniform("radius"));

        this.transformation = transformation;
    }

    @Override
    public void render(Window window, Camera camera, Scene scene, Canvas canvas, Vector3f ambientLight) {
        start();

        Matrix4f ortho = transformation.getOrtho2DProjectionMatrix(0, Window.getWidth(), Window.getHeight(), 0);

        glDisable(GL_DEPTH_TEST);

        for (GameObject gameObject : canvas.getCanvasObjects().values()) {
            Mesh mesh = gameObject.getMesh();

            //GameEngine.getLogger().atInfo().log("Text location -> x: " + gameObject.getX() + " y: " + gameObject.getY());
            Matrix4f projModelMatrix = transformation.buildOrthoProjModelMatrix(gameObject, ortho);
            setUniformMatrix4("projModelMatrix", projModelMatrix);
            setUniformVector4f("colour", gameObject.getMesh().getMaterial().getAmbientColour().toVector4f());
            setUniformInt("hasTexture", gameObject.getMesh().getMaterial().isTextured() ? 1 : 0);

            if(gameObject instanceof BoxView){
                BoxView boxView = (BoxView) gameObject;

                /*setUniformFloat("uiWidth", boxView.getXSize());
                setUniformFloat("uiHeight", boxView.getYSize());
                setUniformFloat("radius", 0.01f);*/
            }
            mesh.update();
        }

        glEnable(GL_DEPTH_TEST);

        stop();
    }

    @Override
    public void initialize() {

    }
}
