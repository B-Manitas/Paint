package Model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ToolsShape implements ITools {

  private static ToolsTypes type = ToolsTypes.SHAPE;

  public boolean isTool(ToolsTypes type) {
    /**
     * Retourne vrai si la forme est de type `type`.
     *
     * @param type Le type de forme à comparer
     */
    return ToolsShape.type == type;
  }

  public void draw(Canvas canvas, Image previousImage, Coord posCurrent, IShape shapeToolsSelected) {
    /**
     * Dessine la forme.
     *
     * @param gc Le contexte graphique
     */
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    gc.drawImage(previousImage, 0, 0);

    shapeToolsSelected.setEndCoord(posCurrent);
    shapeToolsSelected.draw(gc);
  }
}
