package com.horizon.engine.graphics.object.hud.other;

import com.horizon.engine.Window;

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
            case DOWN: return 0;
            case TOP: return (float) Window.getHeight();
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
}
