package com.horizon.engine.graphics.object.grid;

import com.horizon.engine.GameEngine;
import com.horizon.engine.graphics.data.Texture;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.component.component.Mesh;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class Grid extends GameObject {

    @Getter private final List<GridSquare> squares = new LinkedList<>();

    @Getter private float[] coordinates, textureCoordinates;
    @Getter private int[] indices;

    @Getter private final float size;
    @Getter private final int gridSize;

    private final int[] gridCalculationSchema = new int[]{0, 1, 2, 2, 0, 3};

    protected Texture texture;

    public Grid(GameEngine gameEngine, float size, int gridSize, Texture texture) {
        super(gameEngine, "Grid");

        this.size = size;
        this.gridSize = gridSize;
        this.texture = texture;

        generateGrid();
    }

    protected void generateGrid(){
        try{
            float squareSize = size / gridSize;

            int squareIndex = 1;

            coordinates = new float[(gridSize * 12) * gridSize];
            indices = new int[(gridSize * 6) * gridSize];
            textureCoordinates = new float[]{
                    0.0f, 0.0f,
                    0.0f, 0.5f,

                    0.5f, 0.5f,
                    0.5f, 0.0f
            };

            for(float x = ((gridSize / 2.0f) * -1) * squareSize; x < (gridSize / 2.0f) * squareSize; x += squareSize){
                for(float z = ((gridSize / 2.0f) * -1) * squareSize; z < (gridSize / 2.0f) * squareSize; z += squareSize){
                    GridSquare square = new GridSquare(squareIndex, x, z, squareSize);

                    for(int i = 1; i <= 4; i++){
                        coordinates[(((squareIndex * 12) - 12) + ((i * 3) - 2)) - 1] = square.getCorners()[i - 1].x();
                        coordinates[(((squareIndex * 12) - 12) + ((i * 3) - 1)) - 1] = square.getCorners()[i - 1].y();
                        coordinates[(((squareIndex * 12) - 12) + ((i * 3))) - 1] = square.getCorners()[i - 1].z();
                    }

                    calculateGridSquareIndices(square);

                    squares.add(square);
                    squareIndex++;
                }
            }

            addComponent(new Mesh(coordinates, textureCoordinates, new float[] { }, indices));

        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void calculateGridSquareIndices(GridSquare square){
        int squareIndex = square.getSquareId();

        int index = 0;

        for(int schema : gridCalculationSchema){
            indices[((squareIndex * 6) - 6) + index] = ((squareIndex * 4) - 4) + schema;
            index++;
        }
    }
}
