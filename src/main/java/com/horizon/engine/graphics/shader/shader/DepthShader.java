package com.horizon.engine.graphics.shader.shader;

import com.horizon.engine.Window;
import com.horizon.engine.asset.AssetManager;
import com.horizon.engine.common.file.File;
import com.horizon.engine.component.component.light.DirectionalLightComponent;
import com.horizon.engine.component.component.mesh.InstancedMesh;
import com.horizon.engine.component.component.mesh.Mesh;
import com.horizon.engine.graphics.data.Transformation;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.light.DirectionalLight;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.object.scene.SceneLight;
import com.horizon.engine.graphics.render.Renderer;
import com.horizon.engine.graphics.shader.MeshShader;
import com.horizon.engine.graphics.shader.uniform.Uniform;
import com.horizon.engine.graphics.shadow.ShadowBuffer;
import com.horizon.engine.graphics.shadow.ShadowCascade;
import lombok.Getter;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL30.*;

public class DepthShader extends MeshShader {

    @Getter private static final int NUM_CASCADES = 3;
    @Getter private static final float[] CASCADE_SPLITS = new float[]{Window.getZ_FAR() / 20.0f, Window.getZ_FAR() / 10.0f, Window.getZ_FAR()};

    @Getter private List<ShadowCascade> shadowCascades;
    @Getter private final List<GameObject> filteredItems;

    @Getter private final Transformation transformation;
    @Getter private ShadowBuffer shadowBuffer;

    public DepthShader(Renderer renderer, Transformation transformation) {
        super(renderer, new File("shaders/graphic/depth_vertex.vs"), new File("shaders/graphic/depth_fragment.fs"));
        super.storeAllUniformLocations(new Uniform("orthographicProjectionMatrix"), new Uniform("lightViewMatrix"),
                new Uniform("isInstanced"), new Uniform("jointsMatrix"), new Uniform("modelNonInstancedMatrix"));

        this.transformation = transformation;
        this.filteredItems = new ArrayList<>();
    }

    @Override
    public void initialize() {
        shadowBuffer = new ShadowBuffer();
        shadowCascades = new ArrayList<>();

        float zNear = Window.getZ_NEAR();
        for (int i = 0; i < NUM_CASCADES; i++) {
            ShadowCascade shadowCascade = new ShadowCascade(zNear, CASCADE_SPLITS[i]);
            shadowCascades.add(shadowCascade);
            zNear = CASCADE_SPLITS[i];
        }
    }

    @Override
    public void render(Window window, Camera camera, Scene scene, Canvas canvas) {
        if (scene.isRenderShadows()) {
            update(window, camera.getViewMatrix(), scene);

            // Setup view port to match the texture size
            glBindFramebuffer(GL_FRAMEBUFFER, shadowBuffer.getDepthMapFBO());
            glViewport(0, 0, ShadowBuffer.SHADOW_MAP_WIDTH, ShadowBuffer.SHADOW_MAP_HEIGHT);
            glClear(GL_DEPTH_BUFFER_BIT);

            // Start the shader program.
            start();

            DirectionalLight light = scene.getSceneLight().getDirectionalLight();
            DirectionalLightComponent directionalLightComponent = light.getDirectionalLight();

            // Render scene for each cascade map
            for (int i = 0; i < NUM_CASCADES; i++) {
                ShadowCascade shadowCascade = shadowCascades.get(i);

                setUniformMatrix4("orthographicProjectionMatrix", shadowCascade.getOrthographicProjectionMatrix());
                setUniformMatrix4("lightViewMatrix", shadowCascade.getLightViewMatrix());

                glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, shadowBuffer.getDepthMap().getIds()[i], 0);
                glClear(GL_DEPTH_BUFFER_BIT);

                renderNonInstancedMeshes(scene, transformation);
                renderInstancedMeshes(scene, transformation);
            }

            // Stop the shader program.
            stop();
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
        }
    }

    private void renderNonInstancedMeshes(Scene scene, Transformation transformation) {
        setUniformInt("isInstanced", 0);

        // Render each mesh with the associated game Items
        AssetManager assetManager = scene.getGameEngine().getAssetManager();
        // Render each mesh with the associated game Items
        Map<Integer, Map.Entry<Mesh, GameObject>> mapMeshes = assetManager.getNonInstancedMeshes();
        for (Integer identifier : mapMeshes.keySet()) {

            Mesh mesh = mapMeshes.get(identifier).getKey();
            GameObject gameObject = mapMeshes.get(identifier).getValue();

            Matrix4f modelMatrix = transformation.buildModelMatrix(gameObject);
            setUniformMatrix4("modelNonInstancedMatrix", modelMatrix);

            mesh.render();
        }
    }

    private void renderInstancedMeshes(Scene scene, Transformation transformation) {
        setUniformInt("isInstanced", 1);

        // Render each mesh with the associated game Items
        AssetManager assetManager = scene.getGameEngine().getAssetManager();
        // Render each mesh with the associated game Items
        Map<Integer, Map.Entry<InstancedMesh, List<GameObject>>> mapMeshes = assetManager.getInstancedMeshes();
        for (Integer identifier : mapMeshes.keySet()) {

            InstancedMesh mesh = mapMeshes.get(identifier).getKey();

            filteredItems.clear();
            for (GameObject gameItem : mapMeshes.get(identifier).getValue()) {
                if (gameItem.isInsideFrustum()) {
                    filteredItems.add(gameItem);
                }
            }

            bindTextures(GL_TEXTURE2);

            mesh.renderListInstanced(filteredItems, transformation, null);
        }
    }

    private void update(Window window, Matrix4f viewMatrix, Scene scene) {
        SceneLight sceneLight = scene.getSceneLight();
        DirectionalLight directionalLight = sceneLight != null ? sceneLight.getDirectionalLight() : null;

        for (int i = 0; i < NUM_CASCADES; i++) {
            ShadowCascade shadowCascade = shadowCascades.get(i);
            shadowCascade.update(window, viewMatrix, directionalLight);
        }
    }

    public void bindTextures(int textures) {
        this.shadowBuffer.bindTextures(textures);
    }

    public void cleanUp() {
        if (shadowBuffer != null) {
            shadowBuffer.cleanup();
        }

        super.cleanUp();
    }
}
