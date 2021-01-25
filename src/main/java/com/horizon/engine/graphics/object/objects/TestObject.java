package com.horizon.engine.graphics.object.objects;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.UtilModel;
import com.horizon.engine.graphics.data.Texture;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.component.component.Mesh;

public class TestObject extends GameObject {

    public TestObject(GameEngine engine, String name, Texture texture) {
        super(engine, name, new Mesh(UtilModel.vertexToArray(engine.getModelManager().getObjectData("Test").cloneVertices()),
                engine.getModelManager().getObjectData("Test").getTextCoordinates(), null,
                engine.getModelManager().getObjectData("Test").getIndices()));
    }
}
