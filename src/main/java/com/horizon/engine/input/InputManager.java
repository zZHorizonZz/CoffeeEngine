package com.horizon.engine.input;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.input.other.InputHandler;
import lombok.Getter;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

public class InputManager extends AbstractManager {

    @Getter
    private final Map<String, Map.Entry<Method, Object>> updateMap = new HashMap<String, Map.Entry<Method, Object>>();

    public InputManager(GameEngine engine){
        super(engine, "Input Manager");
    }

    @Override
    public void onEnable() {
        GLFWKeyCallback callback = glfwSetKeyCallback(getGameEngine().getWindow().getWindowHandle(), (window, key, scancode, action, mods) -> {
            try {
                for(String handler : getUpdateMap().keySet()) {
                    Method method = getUpdateMap().get(handler).getKey();
                    Object methodObject = getUpdateMap().get(handler).getValue();
                    InputHandler inputHandler = method.getAnnotation(InputHandler.class);

                    if (key == inputHandler.input() && action == inputHandler.inputType().getAction()) {
                        method.invoke(methodObject);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initialize() {

    }

    /**
     * Registers all input handler.
     *
     * @param object The object that includes @InputHandler annotations.
     */
    public void registerInputHandlers(Object object) {
        for (Method method : object.getClass().getMethods()) {
            if (method.getAnnotation(InputHandler.class) != null) {
                GameEngine.getLogger().atInfo().log("Input Handler was been found in class " + object.getClass().getSimpleName());

                InputHandler inputHandler = method.getAnnotation(InputHandler.class);
                registerInputHandler(inputHandler, inputHandler.name(), method, object);

                GameEngine.getLogger().atInfo().log("Input Handler " + inputHandler.name() + " for key ID " + inputHandler.input() + " was been successfully initialized.");
            }
        }
    }

    /**
     * With this method we can register specific input handler.
     */
    public void registerInputHandler(InputHandler inputHandler, String label, Method method, Object object) {
        updateMap.put(this.getGameEngine().getWindow().getTitle() + ':' + label.toLowerCase(), new AbstractMap.SimpleEntry<Method, Object>(method, object));
    }
}
