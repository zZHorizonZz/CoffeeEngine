package com.horizon.engine.common;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class UtilDataStore {

    public static void storeVector3f(int position, float[] array, Vector3f corner) {
        int index = 0;

        array[position + index++] = corner.x();
        array[position + index++] = corner.y();
        array[position + index] = corner.z();
    }

    public static void storeVector4f(int position, float[] array, Vector4f corner) {
        int index = 0;

        array[position + index++] = corner.x();
        array[position + index++] = corner.y();
        array[position + index++] = corner.z();
        array[position + index] = corner.w();
    }
}
