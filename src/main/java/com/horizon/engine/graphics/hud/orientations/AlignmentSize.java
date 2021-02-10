package com.horizon.engine.graphics.hud.orientations;

import com.horizon.engine.Window;
import com.horizon.engine.graphics.hud.HudObject;
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

        return getHudObject().getParentObject() == null ? Window.getWidth() / 100.0f * xSize : getHudObject().getParentObject().getWidth() / 100.0f * xSize;
    }

    @Override
    public float getY() {
        if(ySize == 0)
            return getHudObject().getHeight();

        return getHudObject().getParentObject() == null ? Window.getHeight() / 100.0f * ySize : getHudObject().getParentObject().getHeight() / 100.0f * ySize;
    }
}
