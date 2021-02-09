package com.horizon.engine.tool;

import com.horizon.engine.GameEngine;
import com.horizon.engine.component.component.hud.text.TextFont;
import com.horizon.engine.graphics.object.hud.orientations.AlignmentDistance;
import com.horizon.engine.graphics.object.hud.orientations.AlignmentPercentage;
import com.horizon.engine.graphics.object.hud.other.DisplayAnchor;
import com.horizon.engine.graphics.object.hud.text.TextView;
import com.horizon.game.DummyGame;
import lombok.Getter;

public class FPSCounter {

    @Getter private final GameEngine gameEngine;
    @Getter private final TextView fpsCounterText;

    public FPSCounter(GameEngine gameEngine){
        this.gameEngine = gameEngine;

        fpsCounterText = getGameEngine().getGameLogic().getCanvas().createTextView("FPS Counter", " FPS", TextFont.IMPACT, DisplayAnchor.TOP);
        fpsCounterText.setSize(16);
        fpsCounterText.addAlignmentData(new AlignmentPercentage(fpsCounterText, -95, 0));
        fpsCounterText.addAlignmentData(new AlignmentDistance(fpsCounterText, 0, 25));
    }

    public void update(){
        String currentText = getGameEngine().getLastFramesPerSecond() + " FPS";

        if(getFpsCounterText().getTextComponent().getText().equalsIgnoreCase(currentText))
            return;

        getFpsCounterText().setText(currentText);
    }
}
