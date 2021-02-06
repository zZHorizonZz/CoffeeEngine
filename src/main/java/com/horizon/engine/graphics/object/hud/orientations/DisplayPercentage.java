package com.horizon.engine.graphics.object.hud.orientations;

import com.horizon.engine.GameEngine;
import com.horizon.engine.Window;
import lombok.Getter;
import lombok.Setter;

public class DisplayPercentage extends DisplayData {

    @Getter @Setter private float xPercentage;
    @Getter @Setter private float yPercentage;

    public DisplayPercentage(float xPercentage, float yPercentage){
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
