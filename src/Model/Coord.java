package Model;

import javafx.scene.input.MouseEvent;

public class Coord {

  public double x;
  public double y;

  public Coord(double posX, double posY) {
    x = posX;
    y = posY;
  }

  public Coord() {
    x = 0;
    y = 0;
  }

  public static Coord getCoordMouse(MouseEvent e) {
    double posX = e.getX();
    double posY = e.getY();

    return new Coord(posX, posY);
  }

  public Coord copy() {
    return new Coord(x, y);
  }

  public static Coord getCoordMouse(MouseEvent e, int penSize) {
    double posX = e.getX() - penSize / 2;
    double posY = e.getY() - penSize / 2;

    return new Coord(posX, posY);
  }

  public void printCoord() {
    System.out.println("(" + this.x + ", " + this.y + ")");
  }

  public double distance(Coord c) {
    return Math.sqrt(Math.pow(this.x - c.x, 2) + Math.pow(this.y - c.y, 2));
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Coord) return (
      ((Coord) o).x == this.x && ((Coord) o).y == this.y
    ); else return false;
  }

  public void moveTo(Coord c) {
    this.x += c.x;
    this.y += c.y;
  }

  public void moveTo(int translate) {
    this.x += translate;
    this.y += translate;
  }

  public void moveTo(double translateX, double translateY) {
    this.x += translateX;
    this.y += translateY;
  }

  public Coord translate(int translate) {
    return new Coord(x + translate, y + translate);
  }
}
