package com.horizon.engine.hud;

import com.horizon.engine.AbstractManager;
import com.horizon.engine.GameEngine;
import com.horizon.engine.data.ApplicationData;
import com.horizon.engine.graphics.hud.orientations.AlignmentData;
import com.horizon.engine.graphics.hud.other.DisplayAnchor;
import com.horizon.engine.hud.hud.DeveloperMenu;
import com.horizon.engine.hud.hud.MenuObject;
import com.horizon.engine.tool.FPSCounter;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

public class HudManager extends AbstractManager {

    private Map<String, MenuObject> menuMap = new LinkedHashMap<>();

    @Getter private FPSCounter fpsCounter;

    public HudManager(GameEngine engine) {
        super(engine, "HUD Manager");

        if(ApplicationData.isShowFPS())
            fpsCounter = new FPSCounter(getGameEngine());
    }

    public MenuObject createMenu(MenuObject menuObject) {
        menuMap.put(menuObject.getName(), menuObject);
        return menuObject;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void initialize() {

    }
}
