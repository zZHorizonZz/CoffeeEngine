package com.horizon.game;

import com.horizon.engine.Window;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.Mesh;
import com.horizon.engine.component.component.light.DirectionalLightComponent;
import com.horizon.engine.component.component.light.PointLightComponent;
import com.horizon.engine.component.component.light.SpotLightComponent;
import com.horizon.engine.graphics.data.Transformation;
import com.horizon.engine.graphics.light.DirectionalLight;
import com.horizon.engine.graphics.light.PointLight;
import com.horizon.engine.graphics.light.SpotLight;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.shader.shader.graphic.GraphicShader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private static final int MAX_POINT_LIGHTS = 5;
    private static final int MAX_SPOT_LIGHTS = 5;

    private final Transformation transformation;

    private GraphicShader shaderProgram;
    private float specularPower;

    public Renderer() {
        transformation = new Transformation();
        specularPower = 10f;
    }

    public void initialize(Window window) throws Exception {
        shaderProgram = new GraphicShader("material");
        for(int i = 0; i < MAX_POINT_LIGHTS; i++){
            shaderProgram.createPointLightUniform("pointLights" + "[" + i + "]");
        }

        for(int i = 0; i < MAX_SPOT_LIGHTS; i++){
            shaderProgram.createSpotLightUniform("spotLights" + "[" + i + "]");
        }

        shaderProgram.createDirectionalLightUniform("directionalLight");
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, Scene scene, Vector3f ambientLight) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.start();

        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniformMatrix4("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // Update Light Uniforms
        renderLights(viewMatrix, ambientLight, scene.getPointLightList(), scene.getSpotLightList(), scene.getDirectionalLight());

        shaderProgram.setUniformInt("texture_sampler", 0);
        for (GameObject gameObject : scene.getSceneObjects().values()) {
            if(!gameObject.getComponents().containsKey(ComponentType.MESH))
                continue;

            Mesh mesh = gameObject.getMesh();
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameObject, viewMatrix);
            shaderProgram.setUniformMatrix4("modelViewMatrix", modelViewMatrix);
            shaderProgram.setUniform("material", mesh.getMaterial());
            mesh.update();
        }

        shaderProgram.stop();
    }

    private void renderLights(Matrix4f viewMatrix, Vector3f ambientLight, List<PointLight> pointLightComponentList, List<SpotLight> spotLightComponentList, DirectionalLight directionalLight) {

        shaderProgram.setUniformVector3f("ambientLight", ambientLight);
        shaderProgram.setUniformFloat("specularPower", specularPower);

        // Process Point Lights
        int numLights = pointLightComponentList != null ? pointLightComponentList.size() : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLightComponent currPointLightComponent = new PointLightComponent(pointLightComponentList.get(i).getPointLight());
            Vector3f lightPos = currPointLightComponent.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shaderProgram.setUniform("pointLights" + "[" + i + "]", currPointLightComponent);
        }

        // Process Spot Ligths
        numLights = spotLightComponentList != null ? spotLightComponentList.size() : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
            SpotLightComponent currSpotLightComponent = new SpotLightComponent(spotLightComponentList.get(i).getSpotLight());
            Vector4f dir = new Vector4f(currSpotLightComponent.getConeDirection(), 0);
            dir.mul(viewMatrix);
            currSpotLightComponent.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
            Vector3f lightPos = currSpotLightComponent.getPosition();

            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            shaderProgram.setUniform("spotLights" + "[" + i + "]", currSpotLightComponent);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLightComponent currDirLight = new DirectionalLightComponent(directionalLight.getDirectionalLight());
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        shaderProgram.setUniform("directionalLight", currDirLight);

    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanUp();
        }
    }
}