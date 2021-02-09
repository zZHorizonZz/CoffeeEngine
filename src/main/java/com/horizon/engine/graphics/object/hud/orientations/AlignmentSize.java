package com.horizon.engine.graphics.object.hud.orientations;

import com.horizon.engine.GameEngine;
import com.horizon.engine.Window;
import com.horizon.engine.graphics.object.hud.HudObject;
import lombok.Getter;
import lombok.Setter;

public class AlignmentSize extends AlignmentData {

    @Getter @Setter private float xSize;
    @Getter @Setter private float ySize;

    public AlignmentSize(HudObject hudObject, float xSize, float ySize) {
        super(hudObject);
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    public float getX() {
        if(xSize == 0)
            return getHudObject().getWidth();

        return getHudObject().getParentObject() == null ? (int) (Window.getWidth() / 100) * xSize : (int) (getHudObject().getParentObject().getWidth() / 100) * xSize;
    }

    @Override
    public float getY() {
        if(ySize == 0)
            return getHudObject().getHeight();

        return getHudObject().getParentObject() == null ? (int) (Window.getHeight() / 100) * ySize : (int) (getHudObject().getParentObject().getHeight() / 100) * ySize;
    }
}
