package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeLine implements IShape {

  private ShapeTypes type = ShapeTypes.LINE;
  private int toolSize;
  private Coord start, end;
  private Color toolColor;

  public ShapeLine(int toolSize, Color toolColor) {
    this.toolSize = toolSize;
    this.toolColor = toolColor;
    this.start = new Coord();
  }

  public ShapeLine(Coord c, int toolSize, Color toolColor) {
    this.toolSize = toolSize;
    this.toolColor = toolColor;
    start = c;
  }

  private ShapeLine(
    Coord posStart,
    Coord posEnd,
    int toolSize,
    Color toolColor
  ) {
    this.toolSize = toolSize;
    this.toolColor = toolColor;
    this.start = posStart;
    this.end = posEnd;
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

  public IShape copy() {
    return new ShapeLine(start.copy(), end.copy(), toolSize, toolColor);
  }

  public Coord getStartCoord() {
    return this.start;
  }

  public Coord getEndCoord() {
    return this.end;
  }

  public void setEndCoord(Coord end) {
    this.end = end;
  }

  public int getToolSize() {
    return this.toolSize;
  }

  public boolean isShape(ShapeTypes type) {
    return this.type == type;
  }

  public void addCoord(Coord c) {
    this.end = c;
  }

  public void initializeCoord(Coord c) {
    this.start = c;
  }

  public boolean isIn(Coord c) {
    double d1 = start.distance(c) + end.distance(c);
    double d2 = start.distance(end);

    return Math.abs(d1 - d2) <= 2 * this.toolSize;
  }

  public Coord[] getSelectedCoords() {
    Coord[] selectedCoords = new Coord[2];

    selectedCoords[0] = start.translate(-toolSize / 2);
    selectedCoords[1] = end.translate(toolSize / 2);

    return selectedCoords;
  }

  public void moveTo(Coord mouse) {
    double dx = mouse.x - ((start.x + end.x) / 2.0);
    double dy = mouse.y - ((start.y + end.y) / 2.0);

    start.moveTo(dx, dy);
    end.moveTo(dx, dy);
  }

  public void draw(GraphicsContext gc) {
    gc.setLineWidth(toolSize);
    gc.setStroke(toolColor);
    gc.strokeLine(start.x, start.y, end.x, end.y);
  }
}
