package Model;

public class ShapeCircle implements IShape {

  private ShapeTypes type = ShapeTypes.CIRCLE;
  private Coord center;
  private double radius;
  private int toolSize;

  public ShapeCircle(Coord c, int toolSize) {
    this.toolSize = toolSize;
    center = c;
  }

  public int getToolSize() {
    return this.toolSize;
  }

  private ShapeCircle(Coord center, double radius) {
    this.center = center;
    this.radius = radius;
  }

  public Coord getCenter() {
    return center;
  }

  public Coord getRadiusCoord() {
    return new Coord(center.x + radius, center.y + radius);
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
    System.out.println(radius);
    return center.distance(c) <= radius + toolSize;
  }

  public IShape copy() {
    return new ShapeCircle(center, radius);
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
}
