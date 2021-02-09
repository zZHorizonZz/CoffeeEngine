package com.horizon.kingdom_builder.data;

import com.horizon.engine.common.Color;
import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GamePalette {

    @Getter private static final List<Color> grassColors = new LinkedList<Color>(Arrays.asList(new Color(210.0f, 255.0f, 70.0f, 255.0f),
                                                                                    new Color(150.0f, 200.0f, 55.0f, 255.0f),
                                                                                    new Color(100.0f, 125.0f, 35.0f, 255.0f)));
}
