package com.horizon.engine.graphics.object.hud.objects;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.Color;
import com.horizon.engine.component.component.Mesh;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.object.hud.HudObject;
import lombok.Getter;
import lombok.Setter;

public class BoxView extends HudObject {

    @Getter @Setter private float radius;

    public BoxView(GameEngine gameEngine, String objectName, float ySize, float xSize) {
        super(gameEngine, objectName);

        buildBox();
    }

    public void buildBox(){
        float[] positions = new float[]{
                -0.5f,-0.5f,0.0f,
                0.5f,-0.5f,0.0f,
                0.5f,0.5f,0.0f,
                -0.5f,0.5f,0.0f
        };

        float[] textureCoordinates = new float[]{
                -0.5f,-0.5f,
                0.5f,0.5f
        };

        int[] indices = new int[]{
                0,1,2,
                2,1,3
        };

        float[] normals   = new float[0];

        Mesh mesh = new Mesh(positions, textureCoordinates, normals, indices);
        mesh.setMaterial(new Material(Color.RED));
        
        addComponent(mesh);
    }

    @Override
    public void updateObject() {

    }
}
