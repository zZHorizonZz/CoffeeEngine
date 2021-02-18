package com.horizon.engine.debug;

import com.google.common.flogger.FluentLogger;
import com.horizon.engine.GameEngine;
import lombok.Getter;

import java.util.logging.Level;

public class Debugger {

    @Getter private final GameEngine gameEngine;
    private static FluentLogger fluentLogger;

    public Debugger(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        fluentLogger = GameEngine.getLogger();
    }

    public static void log(String logMessage) {
        log("", logMessage);
    }

    public static void logWarning(String logMessage) {
        logWarning("", logMessage);
    }

    public static void logError(String logMessage) {
        logError("", logMessage);
    }

    public static void logConfig(String logMessage) {
        logConfig("", logMessage);
    }

    public static void log(String prefix, String logMessage) {
        fluentLogger.at(Level.INFO).log(prefix == null ? "" : prefix + " -> " + logMessage);
    }

    public static void logWarning(String prefix, String logMessage) {
        fluentLogger.at(Level.WARNING).log(prefix == null ? "" : prefix + " -> " + logMessage);
    }

    public static void logError(String prefix, String logMessage) {
        fluentLogger.at(Level.SEVERE).log(prefix == null ? "" : prefix + " -> " + logMessage);
    }

    public static void logConfig(String prefix, String logMessage) {
        fluentLogger.at(Level.CONFIG).log(prefix == null ? "" : prefix + " -> " + logMessage);
    }
}
