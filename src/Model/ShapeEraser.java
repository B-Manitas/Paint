package Model;

import javafx.scene.paint.Color;

public class ShapeEraser extends ShapePen {
    public ShapeTypes type = ShapeTypes.PEN;
    public static Color color = Color.WHITE;

    public ShapeEraser(int toolSize) {
        super(ShapeEraser.color, toolSize);
    }
}