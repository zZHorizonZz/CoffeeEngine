package com.horizon.engine.event;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.event.data.Event;
import com.horizon.engine.event.data.EventHandler;
import com.horizon.engine.input.other.InputHandler;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class EventManager extends AbstractManager {

    @Getter private final Map<String, Map.Entry<Method, Object>> eventMap = new HashMap<String, Map.Entry<Method, Object>>();

    public EventManager(GameEngine gameEngine){
        super(gameEngine, "Event Manager");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void initialize() {

    }

    public void callEvent(Event event) {
        try{
            for(String handler : getEventMap().keySet()) {
                Method method = getEventMap().get(handler).getKey();
                Object methodObject = getEventMap().get(handler).getValue();

                method.invoke(methodObject, event);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public void registerEventHandlers(Object object) {
        for (Method method : object.getClass().getMethods()) {
            if (method.getAnnotation(EventHandler.class) != null) {
                GameEngine.getLogger().atInfo().log("Input Handler was been found in class " + object.getClass().getSimpleName());

                EventHandler eventHandler = method.getAnnotation(EventHandler.class);
                registerEventHandler(eventHandler, eventHandler.getClass().getSimpleName(), method, object);

                GameEngine.getLogger().atInfo().log("Input Handler " + eventHandler.getClass().getSimpleName());
            }
        }
    }

    public void registerEventHandler(EventHandler eventHandler, String label, Method method, Object object) {
        getEventMap().put(this.getGameEngine().getWindow().getTitle() + ':' + label.toLowerCase(), new AbstractMap.SimpleEntry<Method, Object>(method, object));
    }
}