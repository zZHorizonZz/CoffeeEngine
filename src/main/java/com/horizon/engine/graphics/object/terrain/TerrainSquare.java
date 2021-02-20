package com.horizon.engine.graphics.object.terrain;

import com.horizon.engine.common.Color;
import lombok.Data;
import org.joml.Vector2f;
import org.joml.Vector3f;

public @Data class TerrainSquare {

    private final Terrain terrain;
    private final Vector3f position;
    private final Vector2f squareIndex;
    private final int squareId;

    private Color color;

    private TerrainCorner bottomLeftCorner;
    private TerrainCorner bottomRightCorner;
    private TerrainCorner topRightCorner;
    private TerrainCorner topLeftCorner;

    public TerrainSquare(Terrain terrain, Vector2f squareIndex,  Vector3f position, Color color) {
        this.terrain = terrain;
        this.squareIndex = squareIndex;
        this.position = position;
        this.color = color;
        this.squareId = calculateSquareId();

        createCorners();
    }

    private int calculateSquareId() {
        if(squareIndex.x() == 0)
            return (int) squareIndex.y();

        return (int) (squareIndex.x() * terrain.getZSize() + squareIndex.y());
    }

    //TODO - For now this generation and indexing will work but in future can be reworked to contain smaller number of indices.
    //That can improve performance.

    public void createCorners() {
        if(squareIndex.x() == 0 && squareIndex.y() == 0) {
            Vector3f bottomLeft = position;
            Vector3f bottomRight = new Vector3f(position.x() + terrain.getSize(), position.y(), position.z());
            Vector3f topRight = new Vector3f(position.x() + terrain.getSize(), position.y(), position.z() + terrain.getSize());
            Vector3f topLeft = new Vector3f(position.x(), position.y(), position.z() + terrain.getSize());

            bottomLeftCorner = new TerrainCorner(squareIndex, bottomLeft, 0);
            bottomRightCorner = new TerrainCorner(squareIndex, bottomRight, 1);
            topRightCorner = new TerrainCorner(squareIndex, topRight, 2);
            topLeftCorner = new TerrainCorner(squareIndex, topLeft, 3);

        } else {
            if(squareIndex.y() == 0) {
                TerrainSquare leftSquare = getTerrain().getTerrainSquareMap().get(new Vector2f(squareIndex.x() - 1, squareIndex.y()));

                Vector3f bottomRight = new Vector3f(position.x() + terrain.getSize(), position.y(), position.z());
                Vector3f topRight = new Vector3f(position.x() + terrain.getSize(), position.y(), position.z() + terrain.getSize());

                bottomLeftCorner = leftSquare.getBottomRightCorner().clone();
                bottomRightCorner = new TerrainCorner(squareIndex, bottomRight, (squareId == 0 ? 0 : squareId * 4) + 1);
                topRightCorner = new TerrainCorner(squareIndex, topRight, (squareId == 0 ? 0 : squareId * 4) + 2);
                topLeftCorner = leftSquare.getTopRightCorner().clone();

            } else if(squareIndex.x() == 0) {
                TerrainSquare bottomSquare = getTerrain().getTerrainSquareMap().get(new Vector2f(squareIndex.x(), squareIndex.y() - 1));

                Vector3f topRight = new Vector3f(position.x() + terrain.getSize(), position.y(), position.z() + terrain.getSize());
                Vector3f topLeft = new Vector3f(position.x(), position.y(), position.z() + terrain.getSize());

                bottomLeftCorner = bottomSquare.getTopLeftCorner().clone();
                bottomRightCorner = bottomSquare.getTopRightCorner().clone();
                topRightCorner = new TerrainCorner(squareIndex, topRight, (squareId == 0 ? 0 : squareId * 4) + 2);
                topLeftCorner = new TerrainCorner(squareIndex, topLeft, (squareId == 0 ? 0 : squareId * 4) + 3);
            } else {
                TerrainSquare bottomSquare = getTerrain().getTerrainSquareMap().get(new Vector2f(squareIndex.x(), squareIndex.y() - 1));
                TerrainSquare leftSquare = getTerrain().getTerrainSquareMap().get(new Vector2f(squareIndex.x() - 1, squareIndex.y()));

                Vector3f topRight = new Vector3f(position.x() + terrain.getSize(), position.y(), position.z() + terrain.getSize());

                bottomLeftCorner = leftSquare.getBottomRightCorner().clone();
                bottomRightCorner = bottomSquare.getTopRightCorner().clone();
                topRightCorner = new TerrainCorner(squareIndex, topRight, (squareId == 0 ? 0 : squareId * 4) + 2);
                topLeftCorner = leftSquare.getTopRightCorner().clone();
            }
        }
    }
}
