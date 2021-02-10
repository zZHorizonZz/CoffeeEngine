package com.horizon.engine.graphics.hud.objects;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.Color;
import com.horizon.engine.component.component.Mesh;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.hud.HudObject;
import lombok.Getter;
import lombok.Setter;

public class BoxView extends HudObject {

    private static final Color DEFAULT_COLOR = Color.WHITE;

    @Getter @Setter private float radius;

    @Setter private float width;
    @Setter private float height;

    @Getter private Color color;

    public BoxView(GameEngine gameEngine, String objectName, float width, float height) {
        super(gameEngine, objectName);

        this.width = width;
        this.height = height;

        this.color = DEFAULT_COLOR;

        buildBox();
    }

    public void buildBox(){
        float[] positions = new float[]{
                -(width / 2),-(height / 2),
                -(width / 2),(height / 2),
                (width / 2),-(height / 2),
                (width / 2),(height / 2)
        };

        Mesh mesh = new Mesh(positions);
        mesh.setMaterial(new Material(color));

        addComponent(mesh);
    }

    public void setColor(Color color){
        this.color = color;

        getMesh().setMaterial(new Material(color));
    }

    @Override
    public void update() {
        recalculate();
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void objectCorrections() {

    }

    @Override
    public void setSize(float width, float height) {
        Mesh mesh = getMesh();
        this.width = width;
        this.height = height;

        float[] positions = new float[]{
                -(width / 2),-(height / 2),
                -(width / 2),(height / 2),
                (width / 2),-(height / 2),
                (width / 2),(height / 2)
        };

        mesh.updatePositions(positions, 2);
    }
}
