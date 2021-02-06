package com.horizon.engine.graphics.object.hud;

import com.horizon.engine.GameEngine;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.hud.orientations.DisplayData;
import com.horizon.engine.graphics.object.hud.other.DisplayAnchor;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public abstract class HudObject extends GameObject {

    @Getter private DisplayAnchor displayAnchor;

    @Getter private final List<DisplayData> displayDataList;

    public HudObject(GameEngine gameEngine, String objectName) {
        super(gameEngine, objectName);

        displayDataList = new LinkedList<>();
    }

    public abstract void updateObject();

    public void recalculateAnchors(){
        setPosition(displayAnchor.getWidth(), displayAnchor.getHeight(), 0);

        if(displayDataList.size() > 0){
            for(DisplayData displayData : displayDataList){
                setPosition(displayAnchor.getWidth() + displayData.getX(), displayAnchor.getHeight() + displayData.getY(), 0);
            }
        }
    }

    public void addDisplayData(DisplayData displayData){
        displayDataList.add(displayData);
    }

    public void setDisplayAnchor(DisplayAnchor displayAnchor) {
        this.displayAnchor = displayAnchor;
        recalculateAnchors();
    }
}
