package com.horizon.engine.graphics.hud;

import com.horizon.engine.GameEngine;
import com.horizon.engine.component.component.mesh.DisplayMesh;
import com.horizon.engine.graphics.object.GameObject;
import com.horizon.engine.graphics.hud.orientations.AlignmentData;
import com.horizon.engine.graphics.hud.orientations.AlignmentPercentage;
import com.horizon.engine.graphics.hud.orientations.AlignmentSize;
import com.horizon.engine.graphics.hud.other.DisplayAnchor;
import com.horizon.engine.graphics.hud.text.TextView;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

import java.util.LinkedList;
import java.util.List;

/**
 * @author - Horizon
 *
 *  This class is main class for all hud objects
 *  like TextView, BoxView, etc. In subclasses we
 *  need to create functions that returns alignment
 *  points of this object.
 *
 *  Also this class supports the parrents so if we
 *  want to set the alignment to other objects we can.
 *  And for calculating location is used anchor that is
 *  calculated from parent locations. If parent is not set
 *  location will be calculated from application view.
 */
public abstract class HudObject extends GameObject {

    @Getter private DisplayAnchor displayAnchor;
    @Getter private final List<AlignmentData> alignmentDataList;

    @Getter @Setter private DisplayMesh displayMesh;

    @Getter private HudObject parentObject = null;

    public HudObject(GameEngine gameEngine, String objectName, HudObject parent) {
        super(gameEngine, objectName);

        this.parentObject = parent;
        alignmentDataList = new LinkedList<>();
    }

    public HudObject(GameEngine gameEngine, String objectName) {
        super(gameEngine, objectName);

        alignmentDataList = new LinkedList<>();
    }

    public Vector2f getTop() {
        return new Vector2f(getX(), getY() - (getHeight() / 2));
    }

    public Vector2f getBottom() {
        return new Vector2f(getX(), getY() + getHeight());
    }

    public Vector2f getLeft() {
        return new Vector2f(getX() - (getWidth() / 2), getY());
    }

    public Vector2f getRight() {
        return new Vector2f(getX() + (getWidth() / 2), getY());
    }

    public Vector2f getCenter() {
        return new Vector2f(getX(), getY());
    }

    public abstract float getWidth();

    public abstract float getHeight();

    public abstract void objectCorrections();

    public abstract void setSize(float xSize, float ySize);

    public void center(){
        setPosition(getX() - (getWidth() / 2), getY() - (getHeight() / 2), 0);
    }

    public void recalculate(){
        if (parentObject == null) {
            setPosition(displayAnchor.getWidth(), displayAnchor.getHeight(), 0);

            if (alignmentDataList.size() > 0) {
                for (AlignmentData alignmentData : alignmentDataList) {
                    if (alignmentData instanceof AlignmentPercentage)
                        setPosition(displayAnchor.getWidth() + alignmentData.getX(), displayAnchor.getHeight() + alignmentData.getY(), 0);
                    else if(alignmentData instanceof AlignmentSize && !(this instanceof TextView)) {
                        setSize(alignmentData.getX(), alignmentData.getY());
                    } else {
                        setPosition(getX() + alignmentData.getX(), getY() + alignmentData.getY(), 0);
                    }
                }
            }
        } else {
            displayAnchor.setParentPosition(this);

            if (alignmentDataList.size() > 0) {
                for (AlignmentData alignmentData : alignmentDataList) {
                    if (alignmentData instanceof AlignmentPercentage)
                        setPosition(displayAnchor.getWidth(getParentObject()) + alignmentData.getX(), displayAnchor.getHeight(getParentObject()) + alignmentData.getY(), 0);
                    else if(alignmentData instanceof AlignmentSize && !(this instanceof TextView)) {
                        setSize(alignmentData.getX(), alignmentData.getY());
                    } else {
                        setPosition(getX() + alignmentData.getX(), getY() + alignmentData.getY(), 0);
                    }
                }
            }
        }

        objectCorrections();
    }

    public void addAlignmentData(AlignmentData alignmentData){
        alignmentDataList.add(alignmentData);
        recalculate();
    }

    public void setDisplayAnchor(DisplayAnchor displayAnchor) {
        this.displayAnchor = displayAnchor;
        recalculate();
    }

    public void setParent(HudObject parent){
        this.parentObject = parent;
        recalculate();
    }
}
