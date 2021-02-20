package com.horizon.engine.common;

import com.horizon.engine.data.ApplicationData;
import com.horizon.engine.debug.Debugger;
import org.joml.Math;
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
     * Calculates the normal of the triangle made from the 3 vertices.
     * The vertices must be specified in counter-clockwise order.
     * Because subdivision function does not work properly its hard coded subdivision.
     */
    public static Vector3f calculateNormal(Vector3f vertex0, Vector3f vertex1, Vector3f vertex2) {
        Vector3f tangentA = new Vector3f(vertex0.x() - vertex1.x(), vertex0.y() - vertex1.y(), vertex0.z() - vertex1.z());
        Vector3f tangentB = new Vector3f(vertex0.x() - vertex2.x(), vertex0.y() - vertex2.y(), vertex0.z() - vertex2.z());
        Vector3f normal = new Vector3f().cross(tangentA, tangentB);

        normalize(normal);

        return normal;
    }

    public static float calculateDifference(float arg1, float arg2) {
        return Math.max(arg1, arg2) - Math.min(arg1, arg2);
    }

    public static Vector3f normalize(Vector3f vector3f) {
        vector3f.normalize();

        if(!vector3f.isFinite())
            vector3f.set(!Math.isFinite(vector3f.x()) ? 0 : vector3f.x(), !Math.isFinite(vector3f.y()) ? 0 : vector3f.y(), !Math.isFinite(vector3f.z()) ? 0 : vector3f.z());

        return vector3f;
    }
}
