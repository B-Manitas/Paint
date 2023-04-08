package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeTriangle implements IShape {

  public ShapeTypes type = ShapeTypes.TRIANGLE;
  private Coord start, end, opposite;
  private int toolSize;
  private Color toolColor;

  public ShapeTriangle(Coord c, int toolSize) {
    this.start = c;
    this.toolSize = toolSize;
  }

  private ShapeTriangle(Coord start, Coord end, int toolSize, Color toolColor) {
    this.start = start;
    this.end = end;
    this.toolSize = toolSize;
    this.opposite = start.opposite(end);
    this.toolColor = toolColor;
  }

  public Coord getStartCoord() {
    return this.start;
  }

  public Coord getEndCoord() {
    return this.end;
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

  public boolean isShape(ShapeTypes type) {
    return this.type == type;
  }

  public void addCoord(Coord c) {
    this.end = c;
    this.opposite = this.start.opposite(this.end);
  }

  public void initializeCoord(Coord c) {
    this.start = c;
  }

  public boolean isIn(Coord c) {
    double alpha =
      (
        (opposite.y - end.y) *
        (c.x - end.x) +
        (end.x - opposite.x) *
        (c.y - end.y)
      ) /
      (
        (opposite.y - end.y) *
        (start.x - end.x) +
        (end.x - opposite.x) *
        (start.y - end.y)
      );

    double beta =
      ((end.y - start.y) * (c.x - end.x) + (start.x - end.x) * (c.y - end.y)) /
      (
        (opposite.y - end.y) *
        (start.x - end.x) +
        (end.x - opposite.x) *
        (start.y - end.y)
      );

    return alpha >= 0 && beta >= 0 && alpha + beta <= 1;
  }

  public IShape copy() {
    return new ShapeTriangle(start, end, toolSize, toolColor);
  }

  public Coord[] getSelectedCoords() {
    return new Coord[] {this.start, this.end};
  }

  public void moveTo(Coord mouse) {
    double dx = mouse.x - ((start.x + end.x) / 2.0);
    double dy = mouse.y - ((start.y + end.y) / 2.0);

    start.moveTo(dx, dy);
    end.moveTo(dx, dy);
  }

  public void draw(GraphicsContext gc){
    gc.setStroke(this.toolColor);
    gc.setLineWidth(this.toolSize);

    Coord opp = start.opposite(end);

    double[] xPoints = new double[] { start.x, end.x, opp.x };
    double[] yPoints = new double[] { start.y, end.y, opp.y };

    gc.strokePolygon(xPoints, yPoints, 3);
  }

  public void setEndCoord(Coord end) {
    this.end = end;
  }
}
