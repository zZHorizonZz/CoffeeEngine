package com.horizon.engine.asset.prefab;

import com.horizon.engine.GameEngine;
import com.horizon.engine.asset.prefab.data.MeshData;
import com.horizon.engine.component.component.mesh.Mesh;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.objects.ModelObject;
import com.horizon.engine.graphics.texture.Texture;
import lombok.Getter;
import lombok.Setter;

public class ModelPrefab extends Prefab {

    @Getter private final GameEngine gameEngine;

    @Getter private final MeshData meshData;
    @Getter @Setter private Material material;

    public ModelPrefab(GameEngine gameEngine, String name, MeshData meshData) {
        super(name);

        this.meshData = meshData;
        this.gameEngine = gameEngine;
    }

    @Override
    public GameObject instantiateObject() {
        ModelObject modelObject = new ModelObject(gameEngine, getGameEngine().getScene().generateObjectName(name));
        Mesh mesh = meshData.createMesh();

        mesh.setMaterial(material == null ? new Material(new Texture("src/textures/error.png")) : material);
        modelObject.addComponent(mesh);

        return modelObject;
    }
}
