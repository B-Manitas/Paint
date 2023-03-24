package Model;

import javafx.scene.input.MouseEvent;

public class Coord {

  public double x;
  public double y;

  Coord(double posX, double posY){
    x = posX;
    y = posY;
  }

  public static Coord getCoordMouse(MouseEvent e){
    double posX = e.getX();
    double posY = e.getY();

    return new Coord(posX, posY);
  }

  public static Coord getCoordMouse(MouseEvent e, int penSize){
    double posX = e.getX() - penSize / 2;
    double posY = e.getY() - penSize / 2;

    return new Coord(posX, posY);
  }

  public void printCoord(){
    System.out.println(this.x);
    System.out.println(this.y);
  }
}
