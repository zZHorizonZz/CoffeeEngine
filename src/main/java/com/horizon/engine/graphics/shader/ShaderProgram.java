package com.horizon.engine.graphics.shader;

import com.horizon.engine.Window;
import com.horizon.engine.common.file.File;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.shader.uniform.Uniform;
import lombok.Data;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public @Data abstract class ShaderProgram {

    private int programID;
    private final Map<String, Integer> uniforms;

    public ShaderProgram(File vertexFile, File fragmentFile, String... inVariables) {
        int vertexShaderID = loadShader(vertexFile, GL_VERTEX_SHADER);
        int fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER);

        programID = glCreateProgram();
        uniforms = new HashMap<>();

        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);

        bindAttributes(inVariables);

        glLinkProgram(programID);
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
    }

    public ShaderProgram(File vertexFile, File geometryFile, File fragmentFile, String... inVariables) {
        int vertexShaderID = loadShader(vertexFile, GL_VERTEX_SHADER);
        int geometryShaderID = loadShader(geometryFile, GL32.GL_GEOMETRY_SHADER);
        int fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER);

        programID = glCreateProgram();
        uniforms = new HashMap<>();

        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, geometryShaderID);
        glAttachShader(programID, fragmentShaderID);

        bindAttributes(inVariables);

        glLinkProgram(programID);
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, geometryShaderID);
        glDetachShader(programID, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(geometryShaderID);
        glDeleteShader(fragmentShaderID);
    }

    public abstract void render(Window window, Camera camera, Scene scene, Canvas canvas, Vector3f ambientLight);

    public abstract void initialize();

    protected void storeAllUniformLocations(Uniform... uniforms){
        for(Uniform uniform : uniforms){
            uniform.storeUniformLocation(programID);
            this.uniforms.put(uniform.getName(), uniform.getLocation());
        }
        glValidateProgram(programID);
    }

    protected void storeSomeUniformLocations(Uniform... uniforms){
        for(Uniform uniform : uniforms){
            uniform.storeUniformLocation(programID);
            this.uniforms.put(uniform.getName(), uniform.getLocation());
        }
    }

    public void setUniformMatrix4(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniformInt(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniformFloat(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniformVector3f(String uniformName, Vector3f value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniformVector4f(String uniformName, Vector4f value) {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    protected void validateProgram(){
        glValidateProgram(programID);
    }

    public void start() {
        glUseProgram(programID);
    }

    public void stop() {
        glUseProgram(0);
    }

    public void cleanUp() {
        glUseProgram(0);
        glDeleteProgram(programID);
    }

    private void bindAttributes(String[] inVariables){
        for(int i=0;i<inVariables.length;i++){
            glBindAttribLocation(programID, i, inVariables[i]);
        }
    }

    private int loadShader(File file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = file.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader "+ file);
            System.exit(-1);
        }
        return shaderID;
    }
}
