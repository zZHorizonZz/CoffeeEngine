package com.horizon.engine;

import com.google.common.flogger.FluentLogger;
import com.horizon.engine.input.InputManager;
import com.horizon.engine.input.other.MouseInput;
import com.horizon.engine.model.ModelManager;
import com.horizon.engine.tool.ToolManager;
import com.horizon.game.DummyGame;
import lombok.Getter;

public class GameEngine implements Runnable {

    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;

    @Getter private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Getter private final Window window;
    @Getter private final Timer timer;
    @Getter private final IGameLogic gameLogic;

    //Managers
    @Getter private final MouseInput mouseInput;

    @Getter private InputManager inputManager;
    @Getter private ModelManager modelManager;
    @Getter private ToolManager toolManager;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) throws Exception {
        window = new Window(this, windowTitle, width, height, vSync);
        mouseInput = new MouseInput();
        this.gameLogic = gameLogic;
        timer = new Timer();
    }

    @Override
    public void run() {
        try {
            initialize();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void initialize() throws Exception {
        //Initializations
        window.init();
        timer.init();
        mouseInput.init(window);

        //Managers
        inputManager = new InputManager(this);
        modelManager = new ModelManager(this);
        toolManager = new ToolManager(this);

        //Final initialization
        ((DummyGame)gameLogic).assignGameEngine(this); //Todo create abstract class for game itself.
        gameLogic.initialize();
    }

    protected void gameLoop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
    }

    protected void input() {
        mouseInput.input(window);
        gameLogic.onInput(window, mouseInput);
    }

    protected void update(float interval) {
        gameLogic.onUpdate(interval, mouseInput);
    }

    protected void render() {
        gameLogic.onRender(window);
        window.update();
    }

    protected void cleanup() {
        gameLogic.cleanup();
    }
}
