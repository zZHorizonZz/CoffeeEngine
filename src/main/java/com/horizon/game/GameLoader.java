package com.horizon.game;

import com.horizon.engine.GameEngine;
import com.horizon.engine.IGameLogic;

public class GameLoader {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEngine = new GameEngine("GAME", 720, 480, vSync, gameLogic);
            gameEngine.run();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
