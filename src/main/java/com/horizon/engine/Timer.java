package com.horizon.engine;

import lombok.Getter;
import lombok.Setter;

public class Timer {

    private double lastLoopTime;
    @Getter @Setter private float deltaTime;

    public void init() {
        lastLoopTime = getTime();
    }

    public double getTime() {
        return System.nanoTime() / 1_000_000_000.0;
    }

    public float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }

    public double getLastLoopTime() {
        return lastLoopTime;
    }
}
