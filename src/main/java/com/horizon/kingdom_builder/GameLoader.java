package com.horizon.kingdom_builder;

import com.horizon.engine.AbstractGameLogic;
import com.horizon.engine.GameEngine;
import com.horizon.engine.data.ApplicationData;

public class GameLoader {

    public static void main(String[] args) {
        try {
            AbstractGameLogic gameLogic = new KingdomBuilder();

            GameEngine gameEngine = new GameEngine(ApplicationData.getWindowTitle(),
                    (int) ApplicationData.getWindowSize().x(),
                    (int) ApplicationData.getWindowSize().y(),
                    ApplicationData.isVSync(),
                    gameLogic);

            gameEngine.run();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
    }
}
