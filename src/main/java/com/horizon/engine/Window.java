package com.horizon.engine;

import com.horizon.engine.common.Color;
import com.horizon.engine.event.event.ScreenResizeEvent;
import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;

    @Getter private final GameEngine gameEngine;
    @Getter private final String title;

    @Getter private static int width;
    @Getter private static int height;

    @Getter private long windowHandle;

    private boolean resized;
    private boolean vSync;

    private Matrix4f projectionMatrix;

    protected GLFWFramebufferSizeCallback framebufferSizeCallback;

    public Window(GameEngine gameEngine, String title, int width, int height, boolean vSync) {
        this.gameEngine = gameEngine;
        this.title = title;
        Window.width = width;
        Window.height = height;
        this.vSync = vSync;
        this.resized = false;
        projectionMatrix = new Matrix4f();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window.");
        }

        framebufferSizeCallback = glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            Window.width = width;
            Window.height = height;
            this.setResized(true);
        });

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if(videoMode == null){
            throw new RuntimeException("Failed to recognize video mode.");
        }

        glfwSetWindowPos(windowHandle, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);

        glfwMakeContextCurrent(windowHandle);

        if (isvSync()) {
            glfwSwapInterval(1);
        }

        glfwShowWindow(windowHandle);
        enableCapabilities();
    }

    private void enableCapabilities(){
        GL.createCapabilities();

        glClearColor(0.2f, 0.5f, 0.65f, 1.0f);
        glEnable(GL_DEPTH_TEST);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public void setClearColor(Color color) {
        Vector4f vector4f = color.toVector4f();
        setClearColor(vector4f.x(), vector4f.y(), vector4f.z(), vector4f.w());
    }

    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float)width / (float)height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public boolean isKeyReleased(int keyCode){
        return glfwGetKey(windowHandle, keyCode) == GLFW_RELEASE;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setVSync(boolean vSync) {
        this.vSync = vSync;
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public Vector2f getScreenSize(){
        IntBuffer w = MemoryUtil.memAllocInt(1);
        IntBuffer h = MemoryUtil.memAllocInt(1);

        glfwGetWindowSize(windowHandle, w, h);

        int width = w.get(0);
        int height = h.get(0);

        return new Vector2f(width, height);
    }
}
