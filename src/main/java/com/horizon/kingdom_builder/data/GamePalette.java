package com.horizon.kingdom_builder.data;

import com.horizon.engine.common.Color;
import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GamePalette {

    @Getter private static final List<Color> grassColors = new LinkedList<Color>(Arrays.asList(new Color(130.0f, 170.0f, 50.0f, 255.0f),
                                                                                    new Color(100.0f, 145.0f, 40.0f, 255.0f),
                                                                                    new Color(125.0f, 175.0f, 60.0f, 255.0f)));
}
