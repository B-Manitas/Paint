package Model;

import javafx.scene.shape.Shape;

public class ShapeSelector extends Shape implements IShape {

  private ShapeTypes type = ShapeTypes.SELECT;
  private IShape selectedShape;
  private Model model;

  public ShapeSelector(Model model) {
    super();
    this.model = model;
  }

  public boolean isShape(ShapeTypes type) {
    /**
     * Retourne vrai si la forme est de type `type`.
     *
     * @param type Le type de forme à comparer
     */
    return this.type == type;
  }

  public void addCoord(Coord c) {
    // TODO Auto-generated method stub
  }

  public void initializeCoord(Coord c) {
    // TODO Auto-generated method stub
  }

  public void printShape() {
    // TODO Auto-generated method stub
  }

  public boolean isIn(Coord c) {
    // TODO Auto-generated method stub
    return false;
  }

  public IShape copy() {
    return this;
  }

  public Coord[] getSelectedCoords() {
    // TODO Auto-generated method stub
    return null;
  }

  public void moveTo(Coord mouse){
    // TODO Auto-generated method stub
  }
}
