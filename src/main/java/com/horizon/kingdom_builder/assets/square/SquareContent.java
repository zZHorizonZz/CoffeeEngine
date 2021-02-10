package com.horizon.kingdom_builder.assets.square;

import com.horizon.engine.common.Color;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.kingdom_builder.assets.MapSquare;
import com.horizon.kingdom_builder.data.SquareObject;
import lombok.Data;

public @Data abstract class SquareContent {

    private MapSquare mapSquare;

    private SquareObject squareObject;
    private GameObject gameObject;

    private Color biomeColor;

    public SquareContent(MapSquare mapSquare) {
        this.mapSquare = mapSquare;
        this.squareObject = mapSquare.getSquareObject();
    }

    public abstract void initialize();
}
