package Model;

/**
 * ShapeInterface
 */
public interface IShape {
  public void addCoord(Coord c);

  public boolean isShape(ShapeTypes type);

  public void initializeCoord(Coord c);

  public boolean isIn(Coord c);

  public IShape copy();

  public Coord[] getSelectedCoords();

  public void moveTo(Coord mouse);
}
