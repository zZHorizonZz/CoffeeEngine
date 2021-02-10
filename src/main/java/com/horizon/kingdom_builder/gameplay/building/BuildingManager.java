package com.horizon.kingdom_builder.gameplay.building;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.common.Color;
import com.horizon.engine.common.RaycastDisplay;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.data.Texture;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.objects.CubeObject;
import com.horizon.kingdom_builder.KingdomBuilder;
import lombok.Getter;

public class BuildingManager extends AbstractManager {

    @Getter private final KingdomBuilder game;

    private boolean buildMode = true;

    //Testing
    protected GameObject building;

    public BuildingManager(KingdomBuilder game) {
        super(game.getGameEngine(), "Building Manager");

        this.game = game;
    }

    public void update() {
        if(buildMode) {
            RaycastDisplay raycast = getGameEngine().getScene().getSceneCamera().getRaycastDisplay();

            GameObject currentSquare = raycast.selectGameObject(getGameEngine().getScene().getSceneObjects().values(),
                    getGameEngine().getWindow(), getGameEngine().getMouseInput().getCurrentPosition(),
                    getGameEngine().getScene().getSceneCamera());

            if(currentSquare != null)
                building.setPosition(currentSquare.getX(), currentSquare.getY() + 1.0f, currentSquare.getZ());
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void initialize() {
        building = getGameEngine().getScene().initializeObject(new CubeObject(getGameEngine(),"Mouse Position"));
        building.getMesh().setMaterial(new Material(Color.RED));
    }
}
