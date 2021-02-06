package com.horizon.engine.common;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class UtilFont {

    public static java.awt.Font loadFontFrom(File file, Float size){
        java.awt.Font font = new java.awt.Font("Arial", java.awt.Font.PLAIN, 20);
        try{
            font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, file).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

        }
        catch (IOException | FontFormatException exception){
            exception.printStackTrace();
        }

        return font;
    }
}
