package com.horizon.engine;

import com.horizon.engine.input.other.MouseInput;

public interface IGameLogic {
    
    void initialize() throws Exception;

    void onInput(Window window, MouseInput mouseInput);

    void onUpdate(float interval, MouseInput mouseInput);

    void onRender(Window window);

    void cleanup();
}
