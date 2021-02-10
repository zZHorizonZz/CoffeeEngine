package com.horizon.engine.component.component.hud.text;

import com.horizon.engine.common.Font;
import com.horizon.engine.common.UtilResource;
import com.horizon.engine.component.Component;
import com.horizon.engine.component.ComponentType;
import com.horizon.engine.component.component.Mesh;
import com.horizon.engine.graphics.data.Material;
import com.horizon.engine.graphics.object.GameObject;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class TextComponent extends Component {

    @Getter private String text;

    @Getter @Setter private TextFont font;

    @Getter private int size;
    @Getter private float textWidth;
    @Getter private float textHeight;

    @Getter private boolean bold = false;
    @Getter private float spaceBetweenLines = 0.5f;

    private final float ZPOS = 0.0f;
    private final int VERTICES_PER_QUAD = 4;

    protected GameObject gameObject;

    public TextComponent(GameObject gameObject) {
        super(ComponentType.TEXT);

        this.gameObject = gameObject;
    }

    public void initializeText(){
        gameObject.addComponent(buildMesh(getFont().getFont()));
    }

    public void setSize(int size) {
        this.size = size;

        if(gameObject.getMesh() == null)
            return;

        gameObject.getMesh().cleanUp();

        if(isBold())
            getFont().getFont().setFont(getFont().getFont().getFont().deriveFont(java.awt.Font.BOLD, getSize()));
        else
            getFont().getFont().setFont(getFont().getFont().getFont().deriveFont(java.awt.Font.PLAIN, getSize()));

        gameObject.getComponents().replace(ComponentType.MESH, buildMesh(getFont().getFont()));
    }

    public void setBold(boolean bold) {
        this.bold = bold;

        if(gameObject.getMesh() == null)
            return;

        gameObject.getMesh().cleanUp();
        gameObject.getComponents().replace(ComponentType.MESH, buildMesh(getFont().getFont()));
    }

    public void setSpaceBetweenLines(float spaceBetweenLines) {
        this.spaceBetweenLines = spaceBetweenLines;

        if(gameObject.getMesh() == null)
            return;

        gameObject.getMesh().cleanUp();
        gameObject.getComponents().replace(ComponentType.MESH, buildMesh(getFont().getFont()));
    }

    private Mesh buildMesh(Font font) {
        List<Float> positions = new ArrayList<>();
        List<Float> textureCoordinates = new ArrayList<>();
        List<Integer> indices   = new ArrayList<>();

        if(isBold())
            font.setFont(font.getFont().deriveFont(java.awt.Font.BOLD, getSize()));

        float[] normals = new float[0];
        char[] characters = text.toCharArray();
        int numChars = characters.length;

        float startx = 0.0f;
        float starty = 0.0f;

        float textWidth = 0.0f;

        int line = 1;
        int superIndex = 0;

        for(int i = 0; i < numChars; i++) {
            if(i - 1 >= 0 && characters[i - 1] == '/' && characters[i] == 'n')
                continue;

            if(i != characters.length - 1 && characters[i] == '/' && characters[i + 1] == 'n') {
                startx = 0.0f;
                starty += font.getHeight() + spaceBetweenLines;

                line++;
                continue;
            }

            Font.CharInfo charInfo = font.getCharInfo(characters[i]);

            // Left Top vertex
            positions.add(startx); // x
            positions.add(starty); //y
            positions.add(ZPOS); //z
            textureCoordinates.add( (float) charInfo.getStartX() / (float) font.getWidth());
            textureCoordinates.add(0.0f);
            indices.add(superIndex * VERTICES_PER_QUAD);

            // Left Bottom vertex
            positions.add(startx); // x
            positions.add(starty + (float) font.getHeight()); //y
            positions.add(ZPOS); //z
            textureCoordinates.add((float) charInfo.getStartX() / (float) font.getWidth());
            textureCoordinates.add(1.0f);
            indices.add(superIndex * VERTICES_PER_QUAD + 1);

            // Right Bottom vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add(starty + (float) font.getHeight()); //y
            positions.add(ZPOS); //z
            textureCoordinates.add((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) font.getWidth());
            textureCoordinates.add(1.0f);
            indices.add(superIndex * VERTICES_PER_QUAD + 2);

            // Right Top vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add(starty); //y
            positions.add(ZPOS); //z
            textureCoordinates.add((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) font.getWidth());
            textureCoordinates.add(0.0f);
            indices.add(superIndex * VERTICES_PER_QUAD + 3);

            // Add indices por left top and bottom right vertices
            indices.add(superIndex * VERTICES_PER_QUAD);
            indices.add(superIndex * VERTICES_PER_QUAD + 2);

            startx += charInfo.getWidth();
            superIndex++;

            if(startx > textWidth)
                textWidth = startx;
        }

        textHeight = ((font.getHeight() + spaceBetweenLines) * line) - spaceBetweenLines;
        this.textWidth = textWidth;

        size = font.getFont().getSize();

        float[] positionsArray = UtilResource.listToArray(positions);
        float[] textCoordinatesArray = UtilResource.listToArray(textureCoordinates);

        int[] indicesArray = indices.stream().mapToInt(i->i).toArray();
        Mesh mesh = new Mesh(positionsArray, textCoordinatesArray, normals, indicesArray);

        // Set material for mesh.
        mesh.setMaterial(new Material(font.getTexture()));
        return mesh;
    }

    public TextComponent setText(String text) {
        this.text = text;

        if(gameObject.getMesh() == null)
            return this;

        gameObject.getMesh().cleanUp();
        gameObject.getComponents().replace(ComponentType.MESH, buildMesh(getFont().getFont()));

        return this;
    }

    @Override
    public void update() {

    }
}
