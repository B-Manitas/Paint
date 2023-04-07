package Model;

public class ShapeCircle implements IShape {

  public ShapeTypes type = ShapeTypes.LINE;

  public boolean isShape(ShapeTypes type) {
    return this.type == type;
  }

  public void addCoord(Coord c) {
    // TODO Auto-generated method stub
  }

  public void initializeCoord(Coord c) {
    // TODO Auto-generated method stub
  }

  public boolean isIn(Coord c) {
    // TODO Auto-generated method stub
    return false;
  }

  public IShape copy(){
    // TODO Auto-generated method stub
    return this;
  }

  public Coord[] getSelectedCoords(){
    // TODO Auto-generated method stub
    return null;
  }
  
  public void moveTo(Coord mouse){
    // TODO Auto-generated method stub
  }
}
