package com.horizon.engine;

import lombok.Getter;

public abstract class AbstractManager {

    @Getter
    private final String managerName;

    @Getter
    private final GameEngine gameEngine;

    public AbstractManager(GameEngine engine, String managerName){
        this.gameEngine = engine;
        this.managerName = managerName;

        onEnable();
    }

    public void registerSelf(){
        getGameEngine().getInputManager().registerInputHandlers(this);
    }

    public void logAtInfo(String info){
        GameEngine.getLogger().atInfo().log(info);
    }

    public abstract void onEnable();

    public abstract void initialize();
}
