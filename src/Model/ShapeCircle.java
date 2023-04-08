package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeCircle implements IShape {

  private ShapeTypes type = ShapeTypes.CIRCLE;
  private Coord center;
  private double radius;
  private int toolSize;
  private Color toolColor;

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

  private ShapeCircle(Coord center, double radius, int toolSize, Color toolColor) {
    this.center = center;
    this.radius = radius;
    this.toolSize = toolSize;
    this.toolColor = toolColor;
  }

  public Coord getStartCoord() {
    return center;
  }

  public Coord getEndCoord() {
    return new Coord(center.x + radius, center.y + radius);
  }

  public void setEndCoord(Coord end){
    this.radius = center.distance(end);
  }

  public boolean isShape(ShapeTypes type) {
    return this.type == type;
  }

  public void addCoord(Coord c) {
    c.printCoord();
    this.radius = center.distance(c);
  }

  public void initializeCoord(Coord c) {
    center = c;
  }

  public boolean isIn(Coord c) {
    return center.distance(c) <= radius + toolSize;
  }

  public IShape copy() {
    return new ShapeCircle(center, radius, toolSize, toolColor);
  }

  public Coord[] getSelectedCoords() {
    Coord[] coords = new Coord[2];

    coords[0] = new Coord(center.x - radius, center.y - radius);
    coords[1] = new Coord(center.x + radius, center.y + radius);

    coords[0].moveTo(toolSize);
    coords[1].moveTo(toolSize);

    return coords;
  }

  public void moveTo(Coord mouse) {
    this.center = mouse;
  }

  public void draw(GraphicsContext gc){
    double radius = center.distance(getEndCoord());
    
    gc.setLineWidth(toolSize);
    gc.setStroke(toolColor);
    gc.strokeOval(center.x - radius, center.y - radius, radius * 2, radius * 2); 
  }
}
