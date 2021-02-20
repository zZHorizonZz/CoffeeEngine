package com.horizon.engine.data;

import com.horizon.engine.common.Color;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

import java.util.Random;

public class ApplicationData {

    @Getter @Setter private static String charset = "ISO-8859-1";

    //Window settings
    @Getter @Setter private static String windowTitle = "Test Game";
    @Getter @Setter private static boolean vSync = true;
    @Getter @Setter private static Vector2f windowSize = new Vector2f(1080,720);
    @Getter @Setter private static boolean compatibleProfile = true;
    @Getter @Setter private static boolean backFaceCulling = false;
    @Getter @Setter private static boolean frustumCulling = true;
    @Getter @Setter private static boolean antialiasing = true;

    //Camera settings
    @Getter @Setter private static float panSpeed = 50.0f;

    //System Stuff
    @Getter @Setter private static Random random = new Random();
    @Getter @Setter private static boolean showFPS = true;
    @Getter @Setter private static boolean developerMenu = true;

    //Color Pallet
    @Getter @Setter private static Color mainBackground = new Color(50.0f, 50.0f, 50.0f, 200.0f);
    @Getter @Setter private static Color subBackground = new Color(60.0f, 60.0f, 60.0f, 255.0f);
    @Getter @Setter private static Color topBar = new Color(255.0f, 210.0f, 130.0f, 255.0f);
    @Getter @Setter private static Color accept = new Color(0.0f, 160.0f, 0.0f, 255.0f);
    @Getter @Setter private static Color deny = new Color(160.0f, 0.0f, 0.0f, 255.0f);

    //Post Processing
    @Getter @Setter private static Color defaultFogColor = new Color(239.0f, 250.0f, 255.0f, 255.0f);
    @Getter @Setter private static float fogDensity = 0.0f;
}
