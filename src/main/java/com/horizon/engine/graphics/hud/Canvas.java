package com.horizon.engine.graphics.hud;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.Color;
import com.horizon.engine.component.component.hud.text.TextFont;
import com.horizon.engine.data.ApplicationSetting;
import com.horizon.engine.event.data.EventHandler;
import com.horizon.engine.event.event.ScreenResizeEvent;
import com.horizon.engine.graphics.object.hud.HudObject;
import com.horizon.engine.graphics.object.hud.objects.BoxView;
import com.horizon.engine.graphics.object.hud.orientations.AlignmentData;
import com.horizon.engine.graphics.object.hud.orientations.AlignmentDistance;
import com.horizon.engine.graphics.object.hud.orientations.AlignmentPercentage;
import com.horizon.engine.graphics.object.hud.other.DisplayAnchor;
import com.horizon.engine.graphics.object.hud.text.TextView;
import com.horizon.engine.hud.hud.MenuPrefab;
import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Canvas {

    @Getter private final GameEngine gameEngine;

    @Getter private final Map<String, HudObject> canvasObjects = new LinkedHashMap<>();

    public Canvas(GameEngine gameEngine){
        this.gameEngine = gameEngine;

        MenuPrefab menuPrefab = new MenuPrefab(this, "Debug Menu", 800, 650, DisplayAnchor.CENTER);

        menuPrefab.initialize();

        gameEngine.getEventManager().registerEventHandlers(this);
    }

    public void initializeObject(HudObject object){
        canvasObjects.put(object.getGameObjectName(), object);
    }

    public TextView createTextView(String name, String text, DisplayAnchor anchor, AlignmentData... alignmentData){
        return createTextView(name, text, TextFont.IMPACT, anchor, alignmentData);
    }

    public TextView createTextView(String name, String text, DisplayAnchor anchor){
       return createTextView(name, text, TextFont.IMPACT, anchor);
    }

    public TextView createTextView(String name, String text, TextFont font, DisplayAnchor anchor, AlignmentData... alignmentData){
        TextView textView = new TextView(getGameEngine(), text, font, name);

        textView.setDisplayAnchor(anchor);
        if(alignmentData != null)
            textView.getAlignmentDataList().addAll(Arrays.asList(alignmentData));

        textView.updateObject();

        initializeObject(textView);

        return textView;
    }

    public BoxView createBoxView(String name, Color color, DisplayAnchor displayAnchor, int xSize, int ySize){
        BoxView boxView = new BoxView(getGameEngine(), name, xSize, ySize);
        boxView.setDisplayAnchor(displayAnchor);
        boxView.setColor(color);

        initializeObject(boxView);

        return boxView;
    }

    @EventHandler
    public void onScreenResizeEvent(ScreenResizeEvent event){
        for(HudObject hudObject : canvasObjects.values()){
            hudObject.updateObject();
        }

        //((TextView) getCanvasObjects().get("Test Text")).setText("Data Text");
    }
}
