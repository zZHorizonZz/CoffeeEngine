package com.horizon.engine.graphics.object.hud.text;

import com.horizon.engine.GameEngine;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.hud.text.TextComponent;
import com.horizon.engine.component.component.hud.text.TextFont;
import com.horizon.engine.graphics.object.hud.HudObject;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

public class TextView extends HudObject {

    @Getter @Setter private boolean centerText = true;

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
    public void updateObject() {
        recalculateAnchors();

        if(isCenterText())
            centerText();
    }

    public Vector2f getCenter(){
        return new Vector2f(getTextComponent().getTextWidth() / 2, getTextComponent().getTextHeight() / 2);
    }

    public void centerText(){
        setPosition(getX() - (getTextComponent().getTextWidth() / 2), getY() - (getTextComponent().getTextHeight() / 2), 0);
    }

    public void setSize(int size){
        getTextComponent().setSize(size);
        updateObject();
    }

    public void setText(String text){
        getTextComponent().setText(text);
        updateObject();
    }

    public void cleanUp(){
        if(getMesh() != null) {
            getMesh().cleanUp();
            getMesh().cleanUpTexture();
        }
    }

    public TextComponent getTextComponent(){
        return (TextComponent) getComponent(ComponentType.TEXT);
    }
}
