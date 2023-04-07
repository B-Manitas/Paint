package Model;

import javafx.scene.shape.Shape;

public class ShapeLine extends Shape implements IShape {

  private ShapeTypes type = ShapeTypes.LINE;
  private int toolSize;
  private Coord start, end;

  public ShapeLine(Coord c, int toolSize) {
    super();
    this.toolSize = toolSize;
    start = c;
  }

  private ShapeLine(Coord posStart, Coord posEnd, int toolSize) {
    super();
    this.toolSize = toolSize;
    this.start = posStart;
    this.end = posEnd;
  }

  public IShape copy() {
    return new ShapeLine(start.copy(), end.copy(), toolSize);
  }

  public Coord getStart() {
    return this.start;
  }

  public Coord getEnd() {
    return this.end;
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
}
