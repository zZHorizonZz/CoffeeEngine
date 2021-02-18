package com.horizon.engine.graphics.shader.shader;

import com.horizon.engine.Window;
import com.horizon.engine.asset.AssetManager;
import com.horizon.engine.common.file.File;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.mesh.InstancedMesh;
import com.horizon.engine.component.component.mesh.Mesh;
import com.horizon.engine.component.component.light.DirectionalLightComponent;
import com.horizon.engine.component.component.light.PointLightComponent;
import com.horizon.engine.component.component.light.SpotLightComponent;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.data.Transformation;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.object.BatchGameObject;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.object.scene.SceneLight;
import com.horizon.engine.graphics.postprocessing.Fog;
import com.horizon.engine.graphics.render.Renderer;
import com.horizon.engine.graphics.shader.MeshShader;
import com.horizon.engine.graphics.shader.ShaderProgram;
import com.horizon.engine.graphics.shader.uniform.Uniform;
import com.horizon.engine.graphics.shadow.ShadowCascade;
import com.horizon.engine.graphics.texture.Texture;
import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;

public class GraphicShader extends MeshShader {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private static final int MAX_POINT_LIGHTS = 5;
    private static final int MAX_SPOT_LIGHTS = 5;

    @Getter private final Transformation transformation;

    @Getter @Setter private float specularPower;

    public GraphicShader(Renderer renderer, Transformation transformation) {
        super(renderer, new File("shaders/graphic/vertex.vs"), new File("shaders/graphic/fragment.fs"));
        super.storeAllUniformLocations(new Uniform("projectionMatrix"), new Uniform("viewMatrix"),
                new Uniform("texture_sampler"), new Uniform("specularPower"), new Uniform("ambientLight"));

        // Material uniforms
        super.storeAllUniformLocations(new Uniform("material.ambient"),
                new Uniform("material.diffuse"),
                new Uniform("material.specular"),
                new Uniform("material.hasTexture"),
                new Uniform("material.reflectance"));

        // Post processing uniforms
        super.storeAllUniformLocations(new Uniform("fog.active"),
                new Uniform("fog.colour"),
                new Uniform("fog.density"));

        // Create uniforms for shadow mapping
        for (int i = 0; i < DepthShader.getNUM_CASCADES(); i++) {
            super.storeAllUniformLocations(new Uniform("shadowMap_" + i));
        }

        super.storeListUniform("orthoProjectionMatrix", DepthShader.getNUM_CASCADES());
        super.storeListUniform("lightViewMatrix", DepthShader.getNUM_CASCADES());
        super.storeListUniform("cascadeFarPlanes", DepthShader.getNUM_CASCADES());
        super.storeAllUniformLocations(new Uniform("modelNonInstancedMatrix"),
                new Uniform("renderShadow"));

        super.storeAllUniformLocations(new Uniform("jointsMatrix"),
                new Uniform("isInstanced"),
                new Uniform("numCols"),
                new Uniform("numRows"));

        this.specularPower = 10.0f;
        this.transformation = transformation;
    }

    /**
     * Uniform settings.
     */

    public void setUniform(String uniformName, PointLightComponent pointLightComponent) {
        setUniformVector3f(uniformName + ".colour", pointLightComponent.getColor());
        setUniformVector3f(uniformName + ".position", pointLightComponent.getPosition());
        setUniformFloat(uniformName + ".intensity", pointLightComponent.getIntensity());
        PointLightComponent.Attenuation attenuation = pointLightComponent.getAttenuation();
        setUniformFloat(uniformName + ".att.constant", attenuation.getConstant());
        setUniformFloat(uniformName + ".att.linear", attenuation.getLinear());
        setUniformFloat(uniformName + ".att.exponent", attenuation.getExponent());
    }

    public void setUniform(String uniformName, SpotLightComponent spotLightComponent) {
        setUniformVector3f(uniformName + ".colour", spotLightComponent.getColor());
        setUniformVector3f(uniformName + ".position", spotLightComponent.getPosition());
        setUniformFloat(uniformName + ".intensity", spotLightComponent.getIntensity());
        PointLightComponent.Attenuation attenuation = spotLightComponent.getAttenuation();
        setUniformFloat(uniformName + ".att.constant", attenuation.getConstant());
        setUniformFloat(uniformName + ".att.linear", attenuation.getLinear());
        setUniformFloat(uniformName + ".att.exponent", attenuation.getExponent());
        setUniformVector3f(uniformName + ".conedir", spotLightComponent.getConeDirection());
        setUniformFloat(uniformName + ".cutoff", spotLightComponent.getCutOffAngle());
    }

    public void setUniform(String uniformName, DirectionalLightComponent directionalLight) {
        setUniformVector3f(uniformName + ".colour", directionalLight.getColor());
        setUniformVector3f(uniformName + ".direction", directionalLight.getDirection());
        setUniformFloat(uniformName + ".intensity", directionalLight.getIntensity());
    }

    public void setUniform(Fog fog) {
        setUniformInt("fog.active", fog.isActive() ? fog.getDensity() == 0.0f ? 0 : 1 : 0);
        setUniformVector3f("fog.colour", fog.getColour());
        setUniformFloat("fog.density", fog.getDensity());
    }

    public void createPointLightUniform(String uniformName) {
        storeAllUniformLocations(new Uniform(uniformName + ".colour"),
                new Uniform(uniformName + ".position"),
                new Uniform(uniformName + ".intensity"),
                new Uniform(uniformName + ".att.constant"),
                new Uniform(uniformName + ".att.linear"),
                new Uniform(uniformName + ".att.exponent"));
    }

    public void createSpotLightUniform(String uniformName) {
        storeAllUniformLocations(new Uniform(uniformName + ".conedir"),
                new Uniform(uniformName + ".cutoff"),
                new Uniform(uniformName + ".colour"),
                new Uniform(uniformName + ".position"),
                new Uniform(uniformName + ".intensity"),
                new Uniform(uniformName + ".att.constant"),
                new Uniform(uniformName + ".att.linear"),
                new Uniform(uniformName + ".att.exponent"));
    }

    public void createDirectionalLightUniform(String uniformName) {
        storeAllUniformLocations(new Uniform(uniformName + ".colour"),
                new Uniform(uniformName + ".direction"),
                new Uniform(uniformName + ".intensity"));
    }

    @Override
    public void render(Window window, Camera camera, Scene scene, Canvas canvas) {
        // Start the shader program.
        start();

        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = window.getProjectionMatrix();

        setUniformMatrix4("viewMatrix", viewMatrix);
        setUniformMatrix4("projectionMatrix", projectionMatrix);

        List<ShadowCascade> shadowCascades = getRenderer().getDepthShader().getShadowCascades();
        for (int i = 0; i < DepthShader.getNUM_CASCADES(); i++) {
            ShadowCascade shadowCascade = shadowCascades.get(i);
            setUniformMatrix4("orthoProjectionMatrix", shadowCascade.getOrthographicProjectionMatrix(), i);
            setUniformFloat("cascadeFarPlanes", DepthShader.getCASCADE_SPLITS()[i], i);
            setUniformMatrix4("lightViewMatrix", shadowCascade.getLightViewMatrix(), i);
        }

        SceneLight sceneLight = scene.getSceneLight();
        renderLights(viewMatrix, sceneLight);

        setUniform(scene.getFog());
        setUniformInt("texture_sampler", 0);

        int start = 2;
        for (int i = 0; i < DepthShader.getNUM_CASCADES(); i++) {
            setUniformInt("shadowMap_" + i, start + i);
        }
        setUniformInt("renderShadow", scene.isRenderShadows() ? 1 : 0);

        renderNonInstancedMeshes(scene);
        renderInstancedMeshes(scene, viewMatrix);

        // Stops the shader program.
        stop();
    }

    private void renderNonInstancedMeshes(Scene scene) {
        setUniformInt("isInstanced", 0);

        // Render each mesh with the associated game Items
        AssetManager assetManager = scene.getGameEngine().getAssetManager();
        // Render each mesh with the associated game Items
        Map<Integer, Map.Entry<Mesh, GameObject>> mapMeshes = assetManager.getNonInstancedMeshes();
        for (Integer identifier : mapMeshes.keySet()) {

            Mesh mesh = mapMeshes.get(identifier).getKey();
            GameObject gameObject = mapMeshes.get(identifier).getValue();
            setUniform(mesh.getMaterial());

            Texture text = mesh.getMaterial().getTexture();
            if (text != null) {
                setUniformInt("numCols", text.getNumCols());
                setUniformInt("numRows", text.getNumRows());
            }

            getRenderer().getDepthShader().bindTextures(GL_TEXTURE2);

            //setUniform("selectedNonInstanced", gameItem.isSelected() ? 1.0f : 0.0f);
            Matrix4f modelMatrix = transformation.buildModelMatrix(gameObject);
            setUniformMatrix4("modelNonInstancedMatrix", modelMatrix);

            mesh.render();
        }
    }

    private void renderInstancedMeshes(Scene scene, Matrix4f viewMatrix) {
        setUniformInt("isInstanced", 1);

        // Render each mesh with the associated game Items
        AssetManager assetManager = scene.getGameEngine().getAssetManager();
        // Render each mesh with the associated game Items
        Map<Integer, Map.Entry<InstancedMesh, List<GameObject>>> mapMeshes = assetManager.getInstancedMeshes();
        for (Integer identifier : mapMeshes.keySet()) {

            InstancedMesh mesh = mapMeshes.get(identifier).getKey();
            Texture texture = mesh.getMaterial().getTexture();

            if (texture != null) {
                setUniformInt("numCols", texture.getNumCols());
                setUniformInt("numRows", texture.getNumRows());
            }

            setUniform(mesh.getMaterial());

            getRenderer().getFilteredItems().clear();
            for (GameObject gameObject : mapMeshes.get(identifier).getValue()) {
                if (gameObject.isInsideFrustum()) {
                    getRenderer().getFilteredItems().add(gameObject);
                }
            }
            getRenderer().getDepthShader().bindTextures(GL_TEXTURE2);

            mesh.renderListInstanced(getRenderer().getFilteredItems(), transformation, viewMatrix);
        }
    }

    @Override
    public void initialize(){
        try {
            for(int i = 0; i < MAX_POINT_LIGHTS; i++){
                createPointLightUniform("pointLights" + "[" + i + "]");
            }

            for(int i = 0; i < MAX_SPOT_LIGHTS; i++){
                createSpotLightUniform("spotLights" + "[" + i + "]");
            }

            createDirectionalLightUniform("directionalLight");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void renderLights(Matrix4f viewMatrix, SceneLight sceneLight) {

        setUniformVector3f("ambientLight", sceneLight.getAmbientLight());
        setUniformFloat("specularPower", specularPower);

        // Process Point Lights
        int numLights = sceneLight.getPointLightList() != null ? sceneLight.getPointLightList().length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLightComponent currPointLightComponent = new PointLightComponent(sceneLight.getPointLightList()[i].getPointLight());
            Vector3f lightPos = currPointLightComponent.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            setUniform("pointLights" + "[" + i + "]", currPointLightComponent);
        }

        // Process Spot Ligths
        numLights = sceneLight.getSpotLightList() != null ? sceneLight.getSpotLightList().length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
            SpotLightComponent currSpotLightComponent = new SpotLightComponent(sceneLight.getSpotLightList()[i].getSpotLight());
            Vector4f dir = new Vector4f(currSpotLightComponent.getConeDirection(), 0);
            dir.mul(viewMatrix);
            currSpotLightComponent.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
            Vector3f lightPos = currSpotLightComponent.getPosition();

            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            setUniform("spotLights" + "[" + i + "]", currSpotLightComponent);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLightComponent currDirLight = new DirectionalLightComponent(sceneLight.getDirectionalLight().getDirectionalLight());
        Vector4f direction = new Vector4f(currDirLight.getDirection(), 0);
        direction.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(direction.x, direction.y, direction.z));

        setUniform("directionalLight", currDirLight);
    }
}
