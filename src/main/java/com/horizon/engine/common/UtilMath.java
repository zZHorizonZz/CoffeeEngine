package com.horizon.engine.common;

import com.horizon.engine.data.ApplicationData;
import org.joml.Vector3f;

public class UtilMath {

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return ApplicationData.getRandom().nextInt((max - min) + 1) + min;
    }

    public static double getRandomNumberInRange(double min, double max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return min + (max - min) * ApplicationData.getRandom().nextDouble();
    }

    /**
     * Calculates the normal of the triangle made from the 3 vertices. The vertices must be specified in counter-clockwise order.
     * @param vertex0
     * @param vertex1
     * @param vertex2
     * @return
     */
    public static Vector3f calculateNormal(Vector3f vertex0, Vector3f vertex1, Vector3f vertex2) {
        Vector3f tangentA = new Vector3f().sub(vertex1, vertex0);
        Vector3f tangentB = new Vector3f().sub(vertex2, vertex0);
        Vector3f normal = new Vector3f().cross(tangentA, tangentB);
        normal.normalize();
        return normal;
    }
}
