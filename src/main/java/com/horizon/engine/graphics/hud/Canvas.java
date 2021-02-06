package com.horizon.engine.graphics.hud;

import com.horizon.engine.GameEngine;
import com.horizon.engine.common.Font;
import com.horizon.engine.component.component.hud.text.TextFont;
import com.horizon.engine.event.data.EventHandler;
import com.horizon.engine.event.event.ScreenResizeEvent;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.object.hud.HudObject;
import com.horizon.engine.graphics.object.hud.objects.BoxView;
import com.horizon.engine.graphics.object.hud.orientations.DisplayData;
import com.horizon.engine.graphics.object.hud.orientations.DisplayPercentage;
import com.horizon.engine.graphics.object.hud.other.DisplayAnchor;
import com.horizon.engine.graphics.object.hud.text.TextView;
import javafx.util.Pair;
import lombok.Data;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public @Data class Canvas {

    private final GameEngine gameEngine;

    private Map<String, HudObject> canvasObjects = new LinkedHashMap<>();

    public Canvas(GameEngine gameEngine){
        this.gameEngine = gameEngine;

        TextView textView = createTextView("Test Text", "Test Text", DisplayAnchor.CENTER, new DisplayPercentage(0, -25));
        textView.setSize(40);

        createBoxView();

        gameEngine.getEventManager().registerEventHandlers(this);
    }

    public void initializeObject(HudObject object){
        canvasObjects.put(object.getGameObjectName(), object);
    }

    public TextView createTextView(String name, String text, DisplayAnchor anchor, DisplayData... displayData){
        return createTextView(name, text, TextFont.AERIAL, anchor, displayData);
    }

    public TextView createTextView(String name, String text, DisplayAnchor anchor){
       return createTextView(name, text, TextFont.AERIAL, anchor);
    }

    public TextView createTextView(String name, String text, TextFont font, DisplayAnchor anchor, DisplayData... displayData){
        TextView textView = new TextView(getGameEngine(), text, font, name);

        textView.setDisplayAnchor(anchor);
        if(displayData != null)
            textView.getDisplayDataList().addAll(Arrays.asList(displayData));

        textView.updateObject();

        canvasObjects.put(name, textView);

        return textView;
    }

    public BoxView createBoxView(){
        BoxView boxView = new BoxView(getGameEngine(), "Test Box", 5, 5);

        canvasObjects.put("Test Box", boxView);

        return boxView;
    }

    @EventHandler
    public void onScreenResizeEvent(ScreenResizeEvent event){
        for(HudObject hudObject : canvasObjects.values()){
            hudObject.updateObject();
        }

        ((TextView) getCanvasObjects().get("Test Text")).setText("Data Text");
    }
}
