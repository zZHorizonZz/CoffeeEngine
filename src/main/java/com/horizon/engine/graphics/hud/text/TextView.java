package com.horizon.engine.graphics.hud.text;

import com.horizon.engine.GameEngine;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.hud.text.TextComponent;
import com.horizon.engine.component.component.hud.text.TextFont;
import com.horizon.engine.graphics.hud.HudObject;

public class TextView extends HudObject {

    public TextView(GameEngine gameEngine, String text, String name){
        this(gameEngine, text, TextFont.AERIAL, name);
    }

    public TextView(GameEngine gameEngine, String text, TextFont font, String name) {
        super(gameEngine, name);

        addComponent(new TextComponent(this));
        getTextComponent().setText(text);
        getTextComponent().setFont(font);
        getTextComponent().initializeText();
    }

    @Override
    public void update() {
        recalculate();
    }

    @Override
    public float getWidth() {
        return getTextComponent().getTextWidth();
    }

    @Override
    public float getHeight() {
        return getTextComponent().getTextHeight();
    }

    @Override
    public void objectCorrections() {
        setPosition(getX() - (getWidth() / 2), getY() - (getHeight() / 2), 0);
    }

    @Override
    public void setSize(float xSize, float ySize) {

    }

    public void setSize(int size) {
        getTextComponent().setSize(size);
        update();
    }

    public void setText(String text) {
        getTextComponent().setText(text);
        update();
    }

    public void setBold(boolean bold) {
        getTextComponent().setBold(bold);
        update();
    }

    public void cleanUp(){
        if(getDisplayMesh() != null) {
            getDisplayMesh().cleanUp();
            getDisplayMesh().cleanUpTexture();
        }
    }

    public TextComponent getTextComponent(){
        return (TextComponent) getComponent(ComponentType.TEXT);
    }
}
