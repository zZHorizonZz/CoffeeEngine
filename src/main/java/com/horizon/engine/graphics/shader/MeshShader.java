package com.horizon.engine.graphics.shader;

import com.horizon.engine.common.file.File;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.render.Renderer;

public abstract class MeshShader extends ShaderProgram {


    public MeshShader(Renderer renderer, File vertexFile, File fragmentFile, String... inVariables) {
        super(renderer, vertexFile, fragmentFile, inVariables);
    }

    public void setUniform(Material material) {
        setUniformVector4f("material.ambient", material.getAmbientColour().toVector4f());
        setUniformVector4f("material.diffuse", material.getDiffuseColour().toVector4f());
        setUniformVector4f("material.specular", material.getSpecularColour().toVector4f());
        setUniformInt("material.hasTexture", material.isTextured() ? 1 : 0);
        setUniformFloat("material.reflectance", material.getReflectance());
    }
}
