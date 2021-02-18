package com.horizon.kingdom_builder.data;

import com.horizon.engine.common.Color;
import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GamePalette {

    @Getter private static final List<Color> grassColors = new LinkedList<Color>(Arrays.asList(new Color(255.0f, 255.0f, 255.0f, 255.0f),
                                                                                    new Color(239.0f, 250.0f, 255.0f, 255.0f),
                                                                                    new Color(225.0f, 245.0f, 246.0f, 255.0f)));
}
