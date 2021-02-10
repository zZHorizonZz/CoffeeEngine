package com.horizon.engine.hud.hud;

import com.horizon.engine.component.component.hud.text.TextFont;
import com.horizon.engine.data.ApplicationData;
import com.horizon.engine.graphics.hud.Canvas;
import com.horizon.engine.graphics.hud.HudObject;
import com.horizon.engine.graphics.hud.objects.BoxView;
import com.horizon.engine.graphics.hud.orientations.AlignmentData;
import com.horizon.engine.graphics.hud.orientations.AlignmentDistance;
import com.horizon.engine.graphics.hud.orientations.AlignmentSize;
import com.horizon.engine.graphics.hud.other.DisplayAnchor;
import com.horizon.engine.graphics.hud.text.TextView;
import com.horizon.engine.hud.other.MenuSort;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

public abstract class MenuObject {

    @Getter private Canvas canvas;

    @Getter private String name;
    @Getter private float xSize;
    @Getter private float ySize;
    @Getter private DisplayAnchor displayAnchor;
    @Getter private AlignmentData[] alignmentData;

    @Getter protected BoxView menuContainer;
    @Getter protected BoxView topMenuContainer;

    @Getter private TextView menuHeader;

    @Getter private boolean showMenu = true;
    @Getter @Setter private MenuSort menuSort;

    private final List<HudObject> menuItem = new LinkedList<>();

    public MenuObject(Canvas canvas, String name, float xSize, float ySize, DisplayAnchor displayAnchor){
        this.canvas = canvas;

        this.name = name;
        this.xSize = xSize;
        this.ySize = ySize;
        this.displayAnchor = displayAnchor;

        initialize();
    }

    public void initialize(){
        menuContainer = getCanvas().createBoxView(name + " Container", ApplicationData.getMainBackground(), getDisplayAnchor(), (int) getXSize(), (int) getYSize());
        topMenuContainer = getCanvas().createBoxView(name + " Top Container", ApplicationData.getSubBackground(), DisplayAnchor.TOP, (int) getXSize(), 40);
        topMenuContainer.setParent(menuContainer);

        topMenuContainer.addAlignmentData(new AlignmentSize(topMenuContainer, 100, 0));

        menuHeader = getCanvas().createTextView(name + " Header", name, TextFont.MODERN_SANS, DisplayAnchor.LEFT);
        menuHeader.setSize(25);
        menuHeader.setBold(true);
        menuHeader.addAlignmentData(new AlignmentDistance(menuHeader, menuHeader.getWidth() / 2 + 25, 0));
        menuHeader.setParent(topMenuContainer);
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;

        if(!showMenu) {
            getCanvas().getCanvasObjects().remove(name + " Container");
            getCanvas().getCanvasObjects().remove(name + " Top Container");
            getCanvas().getCanvasObjects().remove(name + " Header");

            menuItem.forEach(item -> getCanvas().getCanvasObjects().remove(item.getGameObjectName()));
        } else {
            getCanvas().initializeObject(menuContainer);
            getCanvas().initializeObject(topMenuContainer);
            getCanvas().initializeObject(menuHeader);

            menuItem.forEach(item -> getCanvas().initializeObject(item));
        }
    }

    public void addAlignmentData(AlignmentData alignmentData) {
        getMenuContainer().addAlignmentData(alignmentData);
    }

    public void addMenuItem(HudObject hudObject) {
        if(menuSort == null && menuItem.size() != 0)
            hudObject.setParent(menuItem.get(menuItem.size() -1));
        else if(menuItem.size() == 0) {
            hudObject.setParent(menuContainer);
        }

        menuItem.add(hudObject);
    }
}
