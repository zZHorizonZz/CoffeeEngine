package com.horizon.engine.hud.hud;

import com.horizon.engine.component.component.hud.text.TextFont;
import com.horizon.engine.data.ApplicationSetting;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.object.hud.objects.BoxView;
import com.horizon.engine.graphics.object.hud.orientations.AlignmentData;
import com.horizon.engine.graphics.object.hud.orientations.AlignmentDistance;
import com.horizon.engine.graphics.object.hud.orientations.AlignmentSize;
import com.horizon.engine.graphics.object.hud.other.DisplayAnchor;
import com.horizon.engine.graphics.object.hud.text.TextView;
import lombok.Getter;

public class MenuPrefab {

    @Getter private Canvas canvas;

    @Getter private String name;
    @Getter private float xSize;
    @Getter private float ySize;
    @Getter private DisplayAnchor displayAnchor;
    @Getter private AlignmentData[] alignmentData;

    protected BoxView menuContainer;
    protected BoxView topMenuContainer;

    private TextView menuHeader;

    public MenuPrefab(Canvas canvas, String name, float xSize, float ySize, DisplayAnchor displayAnchor, AlignmentData... alignmentData){
        this.canvas = canvas;

        this.name = name;
        this.xSize = xSize;
        this.ySize = ySize;
        this.displayAnchor = displayAnchor;
        this.alignmentData = alignmentData;
    }

    public void initialize(){
        menuContainer = getCanvas().createBoxView(name + " Container", ApplicationSetting.getMainBackground(), DisplayAnchor.CENTER, 800, 750);
        topMenuContainer = getCanvas().createBoxView(name + " Top Container", ApplicationSetting.getSubBackground(), DisplayAnchor.TOP, 500, 40);
        topMenuContainer.setParent(menuContainer);

        topMenuContainer.addAlignmentData(new AlignmentSize(topMenuContainer, 100, 0));

        menuHeader = getCanvas().createTextView(name + " Header", name, TextFont.MODERN_SANS, DisplayAnchor.LEFT);
        menuHeader.setSize(25);
        menuHeader.setBold(true);
        menuHeader.addAlignmentData(new AlignmentDistance(menuHeader, menuHeader.getWidth() / 2 + 25, 0));
        menuHeader.setParent(topMenuContainer);
    }
}
