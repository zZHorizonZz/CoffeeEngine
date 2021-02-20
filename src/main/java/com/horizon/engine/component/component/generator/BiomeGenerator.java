package com.horizon.engine.component.component.generator;

import com.horizon.engine.common.Color;
import com.horizon.engine.common.ColorPalette;
import com.horizon.engine.common.UtilMath;
import com.horizon.engine.common.random.PerlinNoise;
import com.horizon.engine.component.Component;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.debug.Debugger;
import com.horizon.engine.graphics.object.terrain.Terrain;
import lombok.Getter;

public class BiomeGenerator extends Component {

    @Getter private final ColorPalette colorPalette;
    @Getter private final Terrain terrain;

    @Getter private final PerlinNoise perlinNoise;

    @Getter private final float[][] heightMap;
    @Getter private float max;
    @Getter private float min;

    @Getter private final Color[] colors;

    public BiomeGenerator(ColorPalette colorPalette, Terrain terrain) {
        super(ComponentType.GENERATOR);

        this.colorPalette = colorPalette;
        this.terrain = terrain;

        this.perlinNoise = new PerlinNoise(16f, 3, 0.3f, 589454546);

        this.heightMap = perlinNoise.generateHeights(terrain.getXSize());

        for(int x = 0; x < terrain.getXSize(); x++) {
            for(int z = 0; z < terrain.getZSize(); z++) {
                if(heightMap[x][z] > max)
                    max = heightMap[x][z];
                if(heightMap[x][z] < min)
                    min = heightMap[x][z];
            }
        }

        Debugger.log("Min -> " + min);
        Debugger.log("Max -> " + max);

        this.colors = generateColors();
    }

    public Color[] generateColors() {
        Color[] colors = new Color[terrain.getXSize() * 2 * terrain.getZSize()];
        int index = 0;

        for(int x = 0; x < terrain.getXSize(); x++) {
            for(int z = 0; z < terrain.getZSize(); z++) {
                colors[index++] = getHeight(x, z);
            }
        }

        return colors;
    }

    private Color getHeight(int x, int z) {
        float height = heightMap[x][z];
        float difference = UtilMath.calculateDifference(min, max) / colorPalette.getColors().length;

        for(int i = 0; i < colorPalette.getColors().length - 1; i++) {
            if(height > min && height <= min + difference && i == 0) {
                return colorPalette.getColors()[i];
            } else if(height > min + (difference * i) && height <= min + (difference * (i + 1)) && i != 0) {
                return colorPalette.getColors()[i];
            }
        }

        return Color.WHITE;
    }

    @Override
    public void update() {

    }
}
