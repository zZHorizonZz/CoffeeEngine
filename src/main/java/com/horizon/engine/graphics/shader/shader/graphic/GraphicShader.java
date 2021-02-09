package com.horizon.engine.graphics.shader.shader.graphic;

import com.horizon.engine.Window;
import com.horizon.engine.common.file.File;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.Mesh;
import com.horizon.engine.component.component.light.DirectionalLightComponent;
import com.horizon.engine.component.component.light.PointLightComponent;
import com.horizon.engine.component.component.light.SpotLightComponent;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.data.Transformation;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.light.DirectionalLight;
import com.horizon.engine.graphics.light.PointLight;
import com.horizon.engine.graphics.light.SpotLight;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.shader.ShaderProgram;
import com.horizon.engine.graphics.shader.uniform.Uniform;
import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class GraphicShader extends ShaderProgram {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private static final int MAX_POINT_LIGHTS = 5;
    private static final int MAX_SPOT_LIGHTS = 5;

    @Getter private final String materialName;
    @Getter private final Transformation transformation;

    @Getter @Setter private float specularPower;

    public GraphicShader(String materialName, Transformation transformation) {
        super(new File("shaders/graphic/vertex.vs"), new File("shaders/graphic/fragment.fs"));
        super.storeAllUniformLocations(new Uniform("projectionMatrix"), new Uniform("modelViewMatrix"),
                new Uniform("texture_sampler"), new Uniform("colour"), new Uniform("useColour"),
                new Uniform("specularPower"), new Uniform("ambientLight"), new Uniform("selectedNonInstanced"));

        super.storeAllUniformLocations(new Uniform(materialName + ".ambient"),
                new Uniform(materialName + ".diffuse"),
                new Uniform(materialName + ".specular"),
                new Uniform(materialName + ".hasTexture"),
                new Uniform(materialName + ".reflectance"));

        this.materialName = materialName;
        this.specularPower = 10.0f;
        this.transformation = transformation;
    }

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

    public void setUniform(String uniformName, Material material) {
        setUniformVector4f(uniformName + ".ambient", material.getAmbientColour().toVector4f());
        setUniformVector4f(uniformName + ".diffuse", material.getDiffuseColour().toVector4f());
        setUniformVector4f(uniformName + ".specular", material.getSpecularColour().toVector4f());
        setUniformInt(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        setUniformFloat(uniformName + ".reflectance", material.getReflectance());
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

    public void createDirectionalLightUniform(String uniformName) throws Exception {
        storeAllUniformLocations(new Uniform(uniformName + ".colour"),
                new Uniform(uniformName + ".direction"),
                new Uniform(uniformName + ".intensity"));
    }

    @Override
    public void render(Window window, Camera camera, Scene scene, Canvas canvas, Vector3f ambientLight) {
        start();
        transformation.updateProjectionMatrix(FOV, Window.getWidth(), Window.getHeight(), Z_NEAR, Z_FAR);

        Matrix4f projectionMatrix = transformation.getProjectionMatrix();
        setUniformMatrix4("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = camera.getViewMatrix();

        // Update Light Uniforms
        renderLights(viewMatrix, ambientLight, scene.getPointLightList(), scene.getSpotLightList(), scene.getDirectionalLight());

        setUniformInt("texture_sampler", 0);
        for (GameObject gameObject : scene.getSceneObjects().values()) {
            Matrix4f modelMatrix = transformation.buildModelMatrix(gameObject);

            if(!gameObject.getComponents().containsKey(ComponentType.MESH))
                continue;

            Mesh mesh = gameObject.getMesh();
            Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
            setUniformMatrix4("modelViewMatrix", modelViewMatrix);
            setUniform("material", mesh.getMaterial());
            mesh.update();
        }

        stop();
    }

    @Override
    public void initialize(){
        for(int i = 0; i < MAX_POINT_LIGHTS; i++){
            createPointLightUniform("pointLights" + "[" + i + "]");
        }

        for(int i = 0; i < MAX_SPOT_LIGHTS; i++){
            createSpotLightUniform("spotLights" + "[" + i + "]");
        }

        try {
            createDirectionalLightUniform("directionalLight");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void renderLights(Matrix4f viewMatrix, Vector3f ambientLight, List<PointLight> pointLightComponentList, List<SpotLight> spotLightComponentList, DirectionalLight directionalLight) {

        setUniformVector3f("ambientLight", ambientLight);
        setUniformFloat("specularPower", specularPower);

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
            setUniform("pointLights" + "[" + i + "]", currPointLightComponent);
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

            setUniform("spotLights" + "[" + i + "]", currSpotLightComponent);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLightComponent currDirLight = new DirectionalLightComponent(directionalLight.getDirectionalLight());
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        setUniform("directionalLight", currDirLight);

    }
}
