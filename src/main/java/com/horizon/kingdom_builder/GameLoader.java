package com.horizon.kingdom_builder;

import com.horizon.engine.AbstractGameLogic;
import com.horizon.engine.GameEngine;
import com.horizon.engine.data.ApplicationSetting;

public class GameLoader {

    public static void main(String[] args) {
        try {
            AbstractGameLogic gameLogic = new KingdomBuilder();

            GameEngine gameEngine = new GameEngine(ApplicationSetting.getWindowTitle(),
                    (int) ApplicationSetting.getWindowSize().x(),
                    (int) ApplicationSetting.getWindowSize().y(),
                    ApplicationSetting.isVSync(),
                    gameLogic);

            gameEngine.run();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
    }
}
