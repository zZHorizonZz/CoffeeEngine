package com.horizon.engine.asset;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.common.UtilFont;
import com.horizon.engine.common.UtilModelLoader;
import com.horizon.engine.component.component.Mesh;
import com.horizon.engine.graphics.data.MeshData;
import lombok.Getter;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class AssetManager extends AbstractManager {

    //This map is static because we need to use this in TextureFont enum. For now it works.
    //But later this needs to be fixed to classic map and create custom font manager maybe for custom fonts.
    @Getter private static final Map<String, Font> loadedFonts = new LinkedHashMap<>();
    @Getter private final Map<String, MeshData> loadedMeshes = new LinkedHashMap<>();

    public AssetManager(GameEngine engine) {
        super(engine, "Asset Manager");
    }

    public static void loadFont(String name) {
        Font font = UtilFont.loadFontFrom("/font/" + name);
        loadedFonts.put(name, font);
    }

    public static Font getFont(String fontName) {
        return loadedFonts.get(fontName);
    }

    public void loadMesh(String path, String modelName) {
        MeshData mesh = UtilModelLoader.loadMesh(path + "/" + modelName);
        loadedMeshes.put(mesh.getName() == null || mesh.getName().equalsIgnoreCase("No Name") ? "Mesh: " + loadedMeshes.size() + 1 : mesh.getName(), mesh);
    }

    public Mesh getMesh(String modelName) {
        if(getLoadedMeshes().get(modelName) == null) {
            MeshData mesh = UtilModelLoader.loadMesh(modelName);
            if(mesh == null)
                return null;
            else
                return mesh.createMesh();
        }

        return getLoadedMeshes().get(modelName).createMesh();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void initialize() {

    }
}
