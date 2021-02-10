package com.horizon.kingdom_builder.assets;

import com.horizon.engine.common.Color;
import com.horizon.kingdom_builder.KingdomBuilder;
import com.horizon.kingdom_builder.data.GamePalette;
import lombok.Getter;
import org.joml.Vector2f;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class Map {

    @Getter private final KingdomBuilder kingdomBuilder;

    @Getter private final int mapWidth;
    @Getter private final int mapHeight;

    @Getter private final TreeMap<Integer, TreeMap<Integer, MapSquare>> gridMap = new TreeMap<>();

    public Map(KingdomBuilder kingdomBuilder, int mapWidth, int mapHeight) {
        this.kingdomBuilder = kingdomBuilder;

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        initialize();
    }

    public void initialize() {
        generateGrassLayer();
    }

    public void generateGrassLayer() {
        for(int x = (int) -(getMapWidth() / 2.0f); x <= getMapWidth() / 2.0f; x += 1.0f) {
            gridMap.put(x, new TreeMap<Integer, MapSquare>());

            for(int z = (int) -(getMapHeight() / 2.0f); z <= getMapHeight() / 2.0f; z += 1.0f) {
                addMapSquare(new MapSquare(getKingdomBuilder().getGameEngine(), new Vector2f(x, z), pickRandomColor()));
            }
        }
    }

    public List<MapSquare> getMapSquares() {
        List<MapSquare> mapSquares = new LinkedList<>();

        for(int x = (int) -(getMapWidth() / 2.0f); x <= getMapWidth() / 2.0f; x += 1.0f) {
            mapSquares.addAll(gridMap.get(x).values());
        }

        return mapSquares;
    }

    //TODO: Rework this to work with perlin noise.
    public Color pickRandomColor(){
        return GamePalette.getGrassColors().get( new Random().nextInt(3));
    }

    public void addMapSquare(MapSquare mapSquare) {
        gridMap.get((int) mapSquare.getCoordinates().x()).put((int) mapSquare.getCoordinates().y(), mapSquare);
    }
}
