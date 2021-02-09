package com.horizon.engine.common;

import com.horizon.engine.GameEngine;

import java.awt.*;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;

public class UtilFont {

    public static java.awt.Font loadFontFrom(String file){
        java.awt.Font font = null;
        GameEngine.getLogger().atInfo().log("Font -> Loading new font " + file);

        try{
            InputStream is = UtilFont.class.getResourceAsStream(file);
            font = java.awt.Font.createFont(Font.TRUETYPE_FONT, is);
            GameEngine.getLogger().atInfo().log("Font -> Font successfully loaded " + font.getFontName());
            return font;
            //font = font.deriveFont(Font.BOLD, 20);
        }
        catch (IOException | FontFormatException exception){
            exception.printStackTrace();
        }

        return font;
    }
}
