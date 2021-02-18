package com.horizon.engine;

import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.object.scene.Scene;
import com.horizon.engine.input.other.MouseInput;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractGameLogic {

    @Getter @Setter private GameEngine gameEngine;

    @Getter @Setter private Scene scene;
    @Getter @Setter private Canvas canvas;

    @Getter @Setter private boolean sceneInitialized = false;

    public abstract void initialize() throws Exception;

    public abstract void onInput(Window window, MouseInput mouseInput);

    public abstract void onUpdate(float interval, MouseInput mouseInput);

    public abstract void onRender(Window window);

    public abstract void cleanup();
}
