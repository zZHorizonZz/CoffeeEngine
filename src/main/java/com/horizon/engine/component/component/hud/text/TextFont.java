package com.horizon.engine.component.component.hud.text;

import com.horizon.engine.common.Font;
import com.horizon.engine.data.ApplicationSetting;
import lombok.Getter;

public enum TextFont {

    AERIAL(new Font(new java.awt.Font("Arial", java.awt.Font.PLAIN, 20), ApplicationSetting.getCharset()));

    @Getter private final Font font;

    TextFont(Font font){
        this.font = font;
    }

    public Font setSize(int size){
        return new Font(new java.awt.Font(getFont().getFont().getFontName(), getFont().getFont().getStyle(), size), ApplicationSetting.getCharset());
    }
}
