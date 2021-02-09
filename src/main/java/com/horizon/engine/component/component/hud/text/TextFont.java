package com.horizon.engine.component.component.hud.text;

import com.horizon.engine.asset.AssetManager;
import com.horizon.engine.common.Font;
import com.horizon.engine.common.UtilFont;
import com.horizon.engine.data.ApplicationSetting;
import lombok.Getter;

public enum TextFont {

    AERIAL(new Font(new java.awt.Font("Arial", java.awt.Font.PLAIN, 20), ApplicationSetting.getCharset())),
    IMPACT(new Font(new java.awt.Font("Impact", java.awt.Font.PLAIN, 20), ApplicationSetting.getCharset())),
    LIBERATION_SANS(new Font(new java.awt.Font("Liberation Sans", java.awt.Font.PLAIN, 20), ApplicationSetting.getCharset())),
    BABA(new Font(UtilFont.loadFontFrom("/font/Baba.otf"), ApplicationSetting.getCharset())),
    MODERN_SANS(new Font(AssetManager.getFont("ModernSans-Light.otf"), ApplicationSetting.getCharset()));

    @Getter private final Font font;

    TextFont(Font font){
        this.font = font;
    }

    public Font setSize(int size){
        return new Font(new java.awt.Font(getFont().getFont().getFontName(), getFont().getFont().getStyle(), size), ApplicationSetting.getCharset());
    }
}
