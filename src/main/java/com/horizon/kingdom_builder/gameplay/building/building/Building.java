package com.horizon.kingdom_builder.gameplay.building.building;

import com.horizon.engine.component.component.mesh.Mesh;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.texture.Texture;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.kingdom_builder.assets.MapSquare;
import com.horizon.kingdom_builder.gameplay.building.BuildingManager;
import com.horizon.kingdom_builder.gameplay.building.building.other.RangeType;
import lombok.Getter;
import lombok.Setter;

public abstract class Building extends GameObject {

    @Getter private final int buildingID;

    @Getter private final BuildingManager buildingManager;
    @Getter @Setter private String buildingName;

    @Getter private String meshName;

    @Getter private RangeType rangeType;
    @Getter private int buildingRange;

    public Building(BuildingManager buildingManager, int buildingID, String gameObjectName, String meshName) {
        super(buildingManager.getGameEngine(), gameObjectName);

        this.buildingID = buildingID;
        this.buildingManager = buildingManager;
        this.meshName = meshName;
    }

    public void placeBuilding(MapSquare mapSquare) {
        Mesh mesh = getGameEngine().getAssetManager().getModel(meshName).getMeshData().createMesh();
        mesh.setMaterial(new Material(new Texture("src/textures/texture.png")));
        addComponent(mesh);
        getBuildingManager().getGameEngine().getScene().instantiate(this);
    }
}
