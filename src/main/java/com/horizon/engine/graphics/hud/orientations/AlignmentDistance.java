package com.horizon.engine.graphics.hud.orientations;

import com.horizon.engine.graphics.hud.HudObject;
import lombok.Getter;
import lombok.Setter;

public class AlignmentDistance extends AlignmentData {

    @Getter @Setter private float xDistance;
    @Getter @Setter private float yDistance;

    public AlignmentDistance(HudObject hudObject, float xDistance, float yDistance) {
        super(hudObject);
        this.xDistance = xDistance;
        this.yDistance = yDistance;
    }

    @Override
    public float getX() {
        return xDistance;
    }

    @Override
    public float getY() {
        return yDistance;
    }
}
