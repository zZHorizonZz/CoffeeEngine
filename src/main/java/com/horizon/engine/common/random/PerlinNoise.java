package com.horizon.engine.common.random;

import lombok.Data;

import java.util.Random;

/**
 * Perlin Noise class is used for randomizing usually height of the terrain.
 * Author Horizon and big thanks to Thin Matrix.
 */
public @Data class PerlinNoise {

    private final float amplitude;
    private int octaves;
    private float roughness;

    private Random random = new Random();
    private int seed;

    public PerlinNoise() {
        this.amplitude = 70f;
        this.octaves = 3;
        this.roughness = 0.3f;
        this.seed = new Random().nextInt(5000000);
    }

    public PerlinNoise(float amplitude, int octaves, float roughness, int seed) {
        this.amplitude = amplitude;
        this.octaves = octaves;
        this.roughness = roughness;
        this.seed = seed;
    }

    /**
     * This function generates a random height based on x and z location.
     * Height is generated from number of inserted octaves that is number of
     * curves.
     * @param x - coordinate on perlin noise.
     * @param z - coordinate on perlin noise.
     * @return - Height of the map at inserted points.
     */
    public float getHeight(int x, int z) {
        float height = 0;
        float d = (float) Math.pow(2, octaves - 1);
        for(int i = 0; i < octaves; i++) {
            float frequency = (float) (Math.pow(2, i) / d);
            float heightAmplitude = (float) Math.pow(roughness, i) * amplitude;
            height += getInterpolatedNoise((x) * frequency, (z) * frequency) * heightAmplitude;
        }

        return height;
    }


    private float getInterpolatedNoise(float x, float z){
        int intX = (int) x;
        int intZ = (int) z;
        float fracX = x - intX;
        float fracZ = z - intZ;

        float v1 = getSmoothNoise(intX, intZ);
        float v2 = getSmoothNoise(intX + 1, intZ);
        float v3 = getSmoothNoise(intX, intZ + 1);
        float v4 = getSmoothNoise(intX + 1, intZ + 1);
        float i1 = interpolate(v1, v2, fracX);
        float i2 = interpolate(v3, v4, fracX);
        return interpolate(i1, i2, fracZ);
    }

    private float interpolate(float a, float b, float blend){
        double theta = blend * Math.PI;
        float f = (float)(1f - Math.cos(theta)) * 0.5f;
        return a * (1f - f) + b * f;
    }

    private float getSmoothNoise(int x, int z) {
        float corners = (getNoise(x - 1, z - 1) + getNoise(x + 1, z - 1) + getNoise(x - 1, z + 1) + getNoise(x + 1, z + 1)) / 16f;
        float sides = (getNoise(x - 1, z) + getNoise(x + 1, z) + getNoise(x, z - 1) + getNoise(x, z + 1)) / 8f;
        float center = getNoise(x, z) / 4f;
        return corners + sides + center;
    }

    private float getNoise(int x, int z) {
        random.setSeed(x * 91348 + z * 56465 + seed);
        return random.nextFloat() * 2f - 1f;
    }
}
