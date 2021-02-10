package com.horizon.engine.hud.hud;

import com.horizon.engine.component.component.hud.text.TextFont;
import com.horizon.engine.data.ApplicationData;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.hud.orientations.AlignmentDistance;
import com.horizon.engine.graphics.hud.other.DisplayAnchor;
import com.horizon.engine.graphics.hud.text.TextView;

public class DeveloperMenu extends MenuObject {

    private TextView dataLabel;

    public DeveloperMenu(Canvas canvas) {
        super(canvas, "Developer Menu", 600, 800, DisplayAnchor.LEFT);

        addAlignmentData(new AlignmentDistance(getMenuContainer(), 300.0f, 0.0f));

        initializeLabel();
    }

    public void initializeLabel() {
        dataLabel = getCanvas().createTextView("Application Data", getDeveloperData(), TextFont.MODERN_SANS, DisplayAnchor.LEFT);

        dataLabel.getTextComponent().setSpaceBetweenLines(1.0f);
        dataLabel.setBold(false);
        dataLabel.addAlignmentData(new AlignmentDistance(dataLabel, dataLabel.getWidth() / 2 + 10,  0.0f));
        addMenuItem(dataLabel);
    }

    public void updateData() {
        dataLabel.setText(getDeveloperData());
    }

    public String getDeveloperData() {
        String label = "Window Data:" +
                "/n  Window Title: " + ApplicationData.getWindowTitle() +
                "/n  VSync: " + ApplicationData.isVSync() +
                "/n  Window Size: Width: " + ApplicationData.getWindowSize().x() + " Height: " + ApplicationData.getWindowSize().y() +
                "/n  " +
                "/nSystem Data:" +
                "/n  Random: " + ApplicationData.getRandom() +
                "/n  Show FPS: " + ApplicationData.isShowFPS() +
                "/n  Developer Menu: " + ApplicationData.isDeveloperMenu() +
                "/n  " +
                "/nGame Data:" +
                "/n  Current Map Square: ";

        return label;
    }

    public void initializeSystemData() {

    }

    public void initializeColorPalette() {

    }
}
