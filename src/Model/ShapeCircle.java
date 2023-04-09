package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeCircle implements IShape {

  private ShapeTypes type = ShapeTypes.CIRCLE;
  private Coord center, end;
  private int toolSize;
  private Color toolColor;
  private double zoomFactor = 1.0;

  public ShapeCircle(Coord c, int toolSize) {
    this.toolSize = toolSize;
    center = c;
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

  private ShapeCircle(Coord center, Coord end, int toolSize, Color toolColor) {
    this.center = center;
    this.end = end;
    this.toolSize = toolSize;
    this.toolColor = toolColor;
  }

  public Coord getStartCoord() {
    return center;
  }

  public Coord getEndCoord() {
    return end;
  }

  public void setEndCoord(Coord end) {
    this.end = end;
  }

  public boolean isShape(ShapeTypes type) {
    return this.type == type;
  }

  public void initializeCoord(Coord c) {
    center = c;
  }

  public boolean isIn(Coord c) {
    return center.distance(c) <= 1.5 * getRadius();
  }

  public double getRadius() {
    return center.distance(end);
  }

  public IShape copy() {
    return new ShapeCircle(center, end, toolSize, toolColor);
  }

  public Coord[] getSelectedCoords() {
    Coord[] coords = new Coord[2];

    coords[0] = new Coord(center.x - getRadius(), center.y - getRadius());
    coords[1] = new Coord(center.x + getRadius(), center.y + getRadius());

    return coords;
  }

  public void moveTo(Coord mouse) {
    this.center = mouse;
  }

  public void draw(GraphicsContext gc) {
    gc.setLineWidth(toolSize * zoomFactor);
    gc.setStroke(toolColor);
    gc.setFill(toolColor);
    gc.fillOval(center.x - getRadius(), center.y - getRadius(), 2 * getRadius(), 2 * getRadius());
  }

  public void finishShape() {
    // Nothing to do here
  }

  public void zoomIn() {
    this.zoomFactor = Math.max(.1, this.zoomFactor - .1);
    zoom();
  }

  public void zoomOut() {
    this.zoomFactor = Math.min(2.1, this.zoomFactor + .1);
    zoom();
  }

  public void zoom(){
    center.centerZoom(this.zoomFactor);
    end.centerZoom(this.zoomFactor);    
  }
}
