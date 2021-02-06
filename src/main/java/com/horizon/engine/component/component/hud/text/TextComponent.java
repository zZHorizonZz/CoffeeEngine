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

    private static final float ZPOS = 0.0f;
    private static final int VERTICES_PER_QUAD = 4;

    protected GameObject gameObject;

    public TextComponent(GameObject gameObject) {
        super(ComponentType.TEXT);

        this.gameObject = gameObject;
    }

    public void initializeText(){
        gameObject.addComponent(buildMesh(getFont().getFont()));
    }

    public void setSize(int size){
        this.size = size;

        if(gameObject.getMesh() == null)
            return;

        gameObject.getMesh().cleanUp();
        //gameObject.getMesh().cleanUpTexture();
        gameObject.getComponents().replace(ComponentType.MESH, buildMesh(getFont().setSize(size)));
    }

    private Mesh buildMesh(Font font) {
        List<Float> positions = new ArrayList<>();
        List<Float> textureCoordinates = new ArrayList<>();
        List<Integer> indices   = new ArrayList<>();

        float[] normals   = new float[0];
        char[] characters = text.toCharArray();
        int numChars = characters.length;

        float startx = 0;
        for(int i=0; i<numChars; i++) {
            Font.CharInfo charInfo = font.getCharInfo(characters[i]);

            // Left Top vertex
            positions.add(startx); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textureCoordinates.add( (float)charInfo.getStartX() / (float)font.getWidth());
            textureCoordinates.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD);

            // Left Bottom vertex
            positions.add(startx); // x
            positions.add((float)font.getHeight()); //y
            positions.add(ZPOS); //z
            textureCoordinates.add((float)charInfo.getStartX() / (float)font.getWidth());
            textureCoordinates.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 1);

            // Right Bottom vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add((float)font.getHeight()); //y
            positions.add(ZPOS); //z
            textureCoordinates.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)font.getWidth());
            textureCoordinates.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 2);

            // Right Top vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textureCoordinates.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)font.getWidth());
            textureCoordinates.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD + 3);

            // Add indices por left top and bottom right vertices
            indices.add(i*VERTICES_PER_QUAD);
            indices.add(i*VERTICES_PER_QUAD + 2);

            startx += charInfo.getWidth();
        }

        textHeight = font.getHeight();
        textWidth = startx;
        size = font.getFont().getSize();

        float[] positionsArray = UtilResource.listToArray(positions);
        float[] textCoordinatesArray = UtilResource.listToArray(textureCoordinates);

        int[] indicesArray = indices.stream().mapToInt(i->i).toArray();
        Mesh mesh = new Mesh(positionsArray, textCoordinatesArray, normals, indicesArray);

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
