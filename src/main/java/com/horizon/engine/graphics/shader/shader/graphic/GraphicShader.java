package com.horizon.engine.graphics.shader.shader.graphic;

import com.horizon.engine.common.file.File;
import com.horizon.engine.component.component.light.DirectionalLightComponent;
import com.horizon.engine.component.component.light.PointLightComponent;
import com.horizon.engine.component.component.light.SpotLightComponent;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.shader.ShaderProgram;
import com.horizon.engine.graphics.shader.uniform.Uniform;
import lombok.Getter;

public class GraphicShader extends ShaderProgram {

    @Getter private final String materialName;

    public GraphicShader(String materialName) {
        super(new File("shaders/graphic/vertex.vs"), new File("shaders/graphic/fragment.fs"));
        super.storeAllUniformLocations(new Uniform("projectionMatrix"), new Uniform("modelViewMatrix"),
                new Uniform("texture_sampler"), new Uniform("colour"), new Uniform("useColour"),
                new Uniform("specularPower"), new Uniform("ambientLight"));

        super.storeAllUniformLocations(new Uniform(materialName + ".ambient"),
                new Uniform(materialName + ".diffuse"),
                new Uniform(materialName + ".specular"),
                new Uniform(materialName + ".hasTexture"),
                new Uniform(materialName + ".reflectance"));

        this.materialName = materialName;
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
}
