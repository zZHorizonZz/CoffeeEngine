package com.horizon.game;

import com.horizon.engine.GameEngine;
import com.horizon.engine.IGameLogic;
import com.horizon.engine.data.ApplicationSetting;

public class GameLoader {

    public static void main(String[] args) {
        try {
            IGameLogic gameLogic = new DummyGame();

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
