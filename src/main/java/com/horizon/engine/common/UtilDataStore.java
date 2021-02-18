package com.horizon.engine.common;

import org.joml.Vector3f;

public class UtilDataStore {

    public static void storeCorner(int position, float[] array, Vector3f corner) {
        int index = 0;

        array[position + index++] = corner.x();
        array[position + index++] = corner.y();
        array[position + index] = corner.z();
    }
}
