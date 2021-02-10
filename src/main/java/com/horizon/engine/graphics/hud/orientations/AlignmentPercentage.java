package com.horizon.engine.graphics.hud.orientations;

import com.horizon.engine.Window;
import com.horizon.engine.graphics.hud.HudObject;
import lombok.Getter;
import lombok.Setter;

public class AlignmentPercentage extends AlignmentData {

    @Getter @Setter private float xPercentage;
    @Getter @Setter private float yPercentage;

    public AlignmentPercentage(HudObject hudObject, float xPercentage, float yPercentage){
        super(hudObject);
        this.xPercentage = xPercentage;
        this.yPercentage = yPercentage;
    }


    @Override
    public float getX() {
        if(xPercentage == 0)
            return 0;

        return (((float) Window.getWidth() / 2) / 100) * xPercentage;
    }

    @Override
    public float getY() {
        if(yPercentage == 0)
            return 0;

        return (((float) Window.getHeight() / 2) / 100) * yPercentage;
    }
}
