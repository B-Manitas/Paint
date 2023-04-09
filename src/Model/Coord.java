package Model;

import javafx.scene.input.MouseEvent;

public class Coord {

  public double x, y;
  private double xInit, yInit;
  public static double centerX, centerY = 0;

  public Coord(double posX, double posY) {
    x = posX;
    y = posY;
    xInit = x;
    yInit = y;
  }

  public Coord() {
    x = 0;
    y = 0;
    xInit = 0;
    yInit = 0;
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
    if (o instanceof Coord)
      return (((Coord) o).x == this.x && ((Coord) o).y == this.y);
    else
      return false;
  }

  public void moveTo(double translateX, double translateY) {
    this.x += translateX;
    this.y += translateY;

    this.xInit += translateX;
    this.yInit += translateY;
  }

  public Coord translate(int translate) {
    return new Coord(x + translate, y + translate);
  }

  public Coord translate(double translate) {
    return new Coord(x + translate, y + translate);
  }

  public Coord opposite(Coord c) {
    return new Coord(c.x - distance(c), this.y);
  }

  public void centerZoom(double factor) {
    double deltaCenterX = (this.xInit - centerX) * (1 - factor);
    double deltaCenterY = (this.yInit - centerY) * (1 - factor);

    this.x = this.xInit - deltaCenterX;
    this.y = this.yInit - deltaCenterY;
  }

  public void setInit(Coord c) {
    this.xInit = c.x;
    this.yInit = c.y;
  }

  public static void setCenter(double centerX, double centerY) {
    Coord.centerX = centerX;
    Coord.centerY = centerY;
  }
}
