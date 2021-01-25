package com.horizon.engine.graphics.object.objects;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.UtilModel;
import com.horizon.engine.graphics.data.Texture;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.component.component.Mesh;
import lombok.Getter;

public class SquareObject extends GameObject {

    @Getter
    private final Float scaleX, scaleY, scaleZ;

    public SquareObject(GameEngine engine, String name, Texture texture, Float scaleX, Float scaleY, Float scaleZ) {
        super(engine, name, new Mesh(UtilModel.upScaleModel(engine.getModelManager().getObjectData("EmptySquare").cloneVertices(), scaleX, scaleY, scaleZ),
                null, null,
                engine.getModelManager().getObjectData("EmptySquare").getIndices()));

        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }
}
