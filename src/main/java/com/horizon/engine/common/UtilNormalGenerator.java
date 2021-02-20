package com.horizon.engine.common;

import org.joml.Vector3f;

/**
 * @author Karl
 */
public class UtilNormalGenerator {

    /**
     * Generates smooth normals for every vertex in the terrain, based on the
     * terrain heights. The normal at each vertex is basically the average of
     * the normals of all the surrounding faces.
     *
     * @param heights - The heights of all the vertices.
     * @return The normals of all the vertices.
     */
    public static Vector3f[][] generateNormals(float[][] heights) {
        Vector3f[][] normals = new Vector3f[heights.length][heights.length];
        for (int z = 0; z < normals.length; z++) {
            for (int x = 0; x < normals[z].length; x++) {
                normals[z][x] = calculateNormal(x, z, heights);
            }
        }
        return normals;
    }

    /**
     * Calculates the normal of the vertex at the given coordinates
     * of the height map.
     */
    public static Vector3f calculateNormal(int x, int z, float[][] heights) {
        float heightL = getHeight(x - 1, z, heights);
        float heightR = getHeight(x + 1, z, heights);
        float heightD = getHeight(x, z - 1, heights);
        float heightU = getHeight(x, z + 1, heights);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalize();
        return normal;
    }

    private static float getHeight(int x, int z, float[][] heights) {
        x = Math.max(x, 0);
        z = Math.max(z, 0);
        x = x >= heights.length ? heights.length - 1 : x;
        z = z >= heights.length ? heights.length - 1 : z;
        return heights[z][x];
    }

}
