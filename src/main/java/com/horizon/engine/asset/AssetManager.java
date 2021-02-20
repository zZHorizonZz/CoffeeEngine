package com.horizon.engine.asset;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.asset.prefab.ModelPrefab;
import com.horizon.engine.asset.prefab.Prefab;
import com.horizon.engine.asset.prefab.data.MeshData;
import com.horizon.engine.common.UtilFont;
import com.horizon.engine.common.UtilModelLoader;
import com.horizon.engine.component.component.mesh.InstancedMesh;
import com.horizon.engine.component.component.mesh.Mesh;
import com.horizon.engine.debug.Debugger;
import com.horizon.engine.graphics.object.GameObject;
import lombok.Getter;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AssetManager extends AbstractManager {

    //Rework meshes to save it here.

    //This map is static because we need to use this in TextureFont enum. For now it works.
    //But later this needs to be fixed to classic map and create custom font manager maybe for custom fonts.
    @Getter private static final Map<String, Font> loadedFonts = new HashMap<>();
    @Getter private final Map<String, Prefab> loadedPrefabs = new HashMap<>();

    @Getter private final Map<Integer, Map.Entry<Mesh, GameObject>> nonInstancedMeshes = new HashMap<>();
    @Getter private final Map<Integer, Map.Entry<InstancedMesh, List<GameObject>>> instancedMeshes = new HashMap<>();

    public AssetManager(GameEngine engine) {
        super(engine, "Asset Manager");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void initialize() {
        loadPrimitives();
    }

    //TODO this can be done with xml file or something like that.
    public void loadPrimitives() {
        Debugger.log(getManagerName(), "Loading primitives...");
        loadModel("/models", "cube.obj");
        loadModel("/models", "cylinder.obj");
        loadModel("/models", "plane.obj");
        loadModel("/models", "error.obj");
        Debugger.log(getManagerName(), "Primitives successfully loaded.");
    }

    public static void loadFont(String name) {
        Font font = UtilFont.loadFontFrom("/font/" + name);
        loadedFonts.put(name, font);
    }

    public static Font getFont(String fontName) {
        return loadedFonts.get(fontName);
    }

    public ModelPrefab loadModel(String path, String modelName) {
        Debugger.log(getManagerName(), "Loading mesh " + path + "/" + modelName + "...");
        MeshData meshData = UtilModelLoader.loadMesh(path + "/" + modelName);
        ModelPrefab prefab = new ModelPrefab(getGameEngine(), loadedPrefabs.containsKey(meshData.getName()) ? generatePrefabName(meshData.getName()) : meshData.getName(), meshData);

        loadedPrefabs.put(prefab.getName(), prefab);
        Debugger.log(getManagerName(), "Mesh " + modelName + " loaded.");
        return prefab;
    }

    public ModelPrefab getModel(String modelName) {
        if(loadedPrefabs.get(modelName) == null) {
            MeshData meshData = UtilModelLoader.loadMesh("/models/" + modelName);
            if(meshData == null) {
                MeshData errorMeshData = UtilModelLoader.loadMesh("/models/error.obj");
                return new ModelPrefab(getGameEngine(), loadedPrefabs.containsKey(errorMeshData.getName()) ? generatePrefabName(errorMeshData.getName()) : errorMeshData.getName(), errorMeshData);
            }
            return new ModelPrefab(getGameEngine(), loadedPrefabs.containsKey(meshData.getName()) ? generatePrefabName(meshData.getName()) : meshData.getName(), meshData);
        }

        return (ModelPrefab) loadedPrefabs.get(modelName);
    }

    public void addMesh(GameObject gameObject) {
        if(gameObject.getMesh() != null) {
            Mesh mesh = gameObject.getMesh();
            if(mesh instanceof InstancedMesh) {
                List<GameObject> list = instancedMeshes.get(mesh.getMeshId()).getValue();
                if(list == null) {
                    list = new ArrayList<>();
                    instancedMeshes.put(generateMeshIdentifier(mesh), new AbstractMap.SimpleEntry<>((InstancedMesh) mesh, list));
                }
                list.add(gameObject);
            } else {
                nonInstancedMeshes.put(generateMeshIdentifier(mesh), new AbstractMap.SimpleEntry<>(mesh, gameObject));
            }
        }
    }

    private String generatePrefabName(String name) {
        String generatedPrefabName;
        int index = 1;

        while(true) {
            generatedPrefabName = name + "(" + index + ")";
            if(!loadedPrefabs.containsKey(generatedPrefabName))
                break;
            else
                index++;
        }

        return generatedPrefabName;
    }

    private int generateMeshIdentifier(Mesh mesh) {
        int identifier = -1;

        if(mesh instanceof InstancedMesh) {
            if(instancedMeshes.size() == 0)
                identifier = 0;

            for(int i = 0; i <= instancedMeshes.size(); i++) {
                if(!instancedMeshes.containsKey(i))
                    identifier = i;
            }

            mesh.setMeshId(identifier);
            return identifier;
        }

        if(nonInstancedMeshes.size() == 0)
            identifier = 0;

        for(int i = 0; i <= nonInstancedMeshes.size(); i++) {
            if(!nonInstancedMeshes.containsKey(i))
                identifier = i;
        }

        mesh.setMeshId(identifier);
        return identifier;
    }

    public void cleanUp() {
        for (int identifier : nonInstancedMeshes.keySet()) {
            nonInstancedMeshes.get(identifier).getKey().cleanUp();
        }

        for (int identifier : instancedMeshes.keySet()) {
            instancedMeshes.get(identifier).getKey().cleanUp();
        }
    }
}
