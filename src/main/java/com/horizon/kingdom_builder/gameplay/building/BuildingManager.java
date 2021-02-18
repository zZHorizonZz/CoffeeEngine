package com.horizon.kingdom_builder.gameplay.building;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.common.RaycastDisplay;
import com.horizon.engine.event.data.EventHandler;
import com.horizon.engine.event.event.MouseClickEvent;
import com.horizon.engine.graphics.object.Camera;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.graphics.object.terrain.Terrain;
import com.horizon.engine.input.other.MouseInput;
import com.horizon.kingdom_builder.KingdomBuilder;
import com.horizon.kingdom_builder.assets.MapSquare;
import com.horizon.kingdom_builder.gameplay.building.building.Building;
import com.horizon.kingdom_builder.gameplay.building.building.Campfire;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public class BuildingManager extends AbstractManager {

    @Getter private final KingdomBuilder game;

    // Building
    private Map<String, Building> loadedBuilding = new LinkedHashMap<>();
    private Building selectedBuilding;
    private MapSquare currentSquare;

    //Testing
    @Getter private Terrain terrain;

    public BuildingManager(KingdomBuilder game) {
        super(game.getGameEngine(), "Building Manager");

        this.game = game;

        loadBuildings();
    }

    @Override
    public void onEnable() {
        registerSelf();
        getGameEngine().getEventManager().registerEventHandlers(this);
    }

    @Override
    public void initialize() {
        terrain = new Terrain(getGameEngine(), "Terrain", 30, 30);
        getGameEngine().getScene().instantiate(terrain);
    }

    @EventHandler
    public void onBuildingPlace(MouseClickEvent event) {
        if(event.getClickType() != GLFW_MOUSE_BUTTON_1)
            return;

    }

    public void loadBuildings() {
        loadedBuilding.put("Campfire", new Campfire(this));
    }

    public void update() {
        if(selectedBuilding != null) {
            // Some basic stuff that is needed for raycasting.
            Scene scene = getGameEngine().getScene();
            Camera camera = scene.getSceneCamera();
            MouseInput mouseInput = getGameEngine().getMouseInput();
            RaycastDisplay raycast = getGameEngine().getScene().getSceneCamera().getRaycastDisplay();

            // Select gameobject from scene with mouse input in 3d space.
            GameObject currentSquare = raycast.selectGameObject(scene.getSceneObjects().values(), getGameEngine().getWindow(), mouseInput.getCurrentPosition(), camera);

            // Check if current square is not null.
            if(currentSquare == null)
                return;

            // Set position of currently selected building.
            selectedBuilding.setPosition(currentSquare.getX(), currentSquare.getY(), currentSquare.getZ());
        }
    }
}
