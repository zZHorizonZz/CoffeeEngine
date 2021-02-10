package com.horizon.engine.graphics.hud.other;

import com.horizon.engine.Window;
import com.horizon.engine.graphics.hud.HudObject;

public enum DisplayAnchor {

    CENTER,
    TOP,
    DOWN,
    LEFT,
    RIGHT;

    public float getHeight(){
        switch (this){
            case CENTER:
            case RIGHT:
            case LEFT: return (float) Window.getHeight() / 2;
            case DOWN: return (float) Window.getHeight();
            case TOP: return 0;
        }

        return 0;
    }

    public float getWidth(){
        switch (this){
            case CENTER:
            case DOWN:
            case TOP: return (float) Window.getWidth() / 2;
            case LEFT: return 0;
            case RIGHT: return (float) Window.getWidth();
        }

        return 0;
    }

    public float getHeight(HudObject hudObject){
        switch (this){
            case CENTER:
            case RIGHT:
            case LEFT: return (float) hudObject.getHeight() / 2;
            case DOWN: return (float) hudObject.getHeight();
            case TOP: return 0;
        }

        return 0;
    }

    public float getWidth(HudObject hudObject){
        switch (this){
            case CENTER:
            case DOWN:
            case TOP: return (float) hudObject.getWidth() / 2;
            case LEFT: return 0;
            case RIGHT: return (float) hudObject.getWidth();
        }

        return 0;
    }

    public void setParentPosition(HudObject hudObject){
        HudObject parentObject = hudObject.getParentObject();

        switch (this) {
            case RIGHT:
                hudObject.setPosition(parentObject.getRight().x, parentObject.getRight().y, 0);
                break;
            case LEFT:
                hudObject.setPosition(parentObject.getLeft().x, parentObject.getLeft().y, 0);
                break;
            case TOP:
                hudObject.setPosition(parentObject.getTop().x, parentObject.getTop().y, 0);
                break;
            case DOWN:
                hudObject.setPosition(parentObject.getBottom().x, parentObject.getBottom().y, 0);
                break;
            case CENTER:
                hudObject.setPosition(parentObject.getCenter().x, parentObject.getCenter().y, 0);
                break;
        }
    }
}
