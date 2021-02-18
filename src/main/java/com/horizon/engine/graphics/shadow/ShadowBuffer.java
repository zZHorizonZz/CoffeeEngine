package com.horizon.engine.graphics.shadow;

import com.horizon.engine.graphics.shader.shader.DepthShader;
import com.horizon.engine.graphics.texture.ArrayTexture;
import lombok.Data;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.*;

public @Data class ShadowBuffer {

    public static final int SHADOW_MAP_WIDTH = (int)Math.pow(65, 2); //65 2
    public static final int SHADOW_MAP_HEIGHT = SHADOW_MAP_WIDTH;

    private final int depthMapFBO;
    private final ArrayTexture depthMap;

    public ShadowBuffer() {
        // Create a FBO to render the depth map
        depthMapFBO = glGenFramebuffers();

        // Create the depth map textures
        depthMap = new ArrayTexture(DepthShader.getNUM_CASCADES(), SHADOW_MAP_WIDTH, SHADOW_MAP_HEIGHT, GL_DEPTH_COMPONENT);

        try {
            // Attach the the depth map texture to the FBO
            glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap.getIds()[0], 0);

            // Set only depth
            glDrawBuffer(GL_NONE);
            glReadBuffer(GL_NONE);

            if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
                throw new Exception("Could not create FrameBuffer");
            }

            // Unbind
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void bindTextures(int start) {
        for (int i = 0; i < DepthShader.getNUM_CASCADES(); i++) {
            glActiveTexture(start + i);
            glBindTexture(GL_TEXTURE_2D, depthMap.getIds()[i]);
        }
    }

    public void cleanup() {
        glDeleteFramebuffers(depthMapFBO);
        depthMap.cleanup();
    }
}
