package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class ShapeRect extends Shape implements IShape {

  private ShapeTypes type = ShapeTypes.RECTANGLE;
  private Coord start, end;
  private int toolSize;
  private Color toolColor;

  public ShapeRect(Coord c, int toolSize) {
    /**
     * Crée une forme de type rectangle.
     *
     * @param c Coordonnées du point de départ
     * @param toolSize Taille de l'outil
     */
    this.start = c;
    this.toolSize = toolSize;
  }

  private ShapeRect(Coord start, Coord end, int toolSize, Color toolColor) {
    /**
     * Crée une forme de type rectangle.
     *
     * @param start Coordonnées du point de départ
     * @param end Coordonnées du point d'arrivée
     */
    this.start = start;
    this.end = end;
    this.toolSize = toolSize;
    this.toolColor = toolColor;
  }

  public Coord getStartCoord() {
    return this.start;
  }

  public Coord getEndCoord() {
    return this.end;
  }

  public boolean isShape(ShapeTypes type) {
    /**
     * Retourne vrai si la forme est de type `type`.
     *
     * @param type Le type de forme à comparer
     */
    return this.type == type;
  }

  public int getToolSize() {
    return this.toolSize;
  }

  public Color getToolColor() {
    return this.toolColor;
  }

  public void setToolColor(Color color) {
    this.toolColor = color;
  }

  public void setToolSize(int size) {
    this.toolSize = size;
  }

  public void setEndCoord(Coord end) {
    this.end = end;
  }

  public void addCoord(Coord c) {
    this.end = c;
  }

  public void initializeCoord(Coord c) {
    this.start = c;
  }

  public boolean isIn(Coord c) {
    double xMin = Math.min(start.x, end.x);
    double xMax = Math.max(start.x, end.x);
    double yMin = Math.min(start.y, end.y);
    double yMax = Math.max(start.y, end.y);

    return c.x >= xMin && c.x <= xMax && c.y >= yMin && c.y <= yMax;
  }

  public IShape copy() {
    return new ShapeRect(start, end, toolSize, toolColor);
  }

  public Coord[] getSelectedCoords() {
    Coord[] coords = new Coord[2];

    coords[0] = start.translate(-toolSize);
    coords[1] = end.translate(toolSize);

    return coords;
  }

  public void moveTo(Coord mouse) {
    double dx = mouse.x - ((start.x + end.x) / 2.0);
    double dy = mouse.y - ((start.y + end.y) / 2.0);

    start.moveTo(dx, dy);
    end.moveTo(dx, dy);
  }

  public void draw(GraphicsContext gc){
    gc.setStroke(toolColor);
    gc.setLineWidth(toolSize);

    double width = Math.abs(start.x - end.x);
    double height = Math.abs(start.y - end.y);
    double x = Math.min(start.x, end.x);
    double y = Math.min(start.y, end.y);

    gc.setFill(toolColor);
    gc.fillRect(x, y, width, height);
  }
}
