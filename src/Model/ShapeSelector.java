package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class ShapeSelector extends Shape implements IShape {

  private ShapeTypes type = ShapeTypes.SELECT;

  public ShapeSelector(Model model) {
    super();
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

  public void moveTo(Coord mouse) {
    // TODO Auto-generated method stub
  }

  public int getToolSize() {
    // TODO Auto-generated method stub
    return 0;
  }

  public Color getToolColor() {
    // TODO Auto-generated method stub
    return null;
  }

  public Coord getStartCoord() {
    // TODO Auto-generated method stub
    return null;
  }

  public Coord getEndCoord() {
    // TODO Auto-generated method stub
    return null;
  }

  public void setToolColor(Color color) {
    // TODO Auto-generated method stub
  }

  public void setToolSize(int size) {
    // TODO Auto-generated method stub
  }

  public void draw(GraphicsContext gc) {
    // TODO Auto-generated method stub
  }

  public void setEndCoord(Coord end) {
    // TODO Auto-generated method stub
  }
}
