package com.horizon.engine.common;

import com.horizon.engine.data.ApplicationData;

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
}
