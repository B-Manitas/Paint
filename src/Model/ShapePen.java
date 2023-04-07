package Model;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapePen implements IShape {

  private ShapeTypes type = ShapeTypes.PEN;
  private ArrayList<Coord> shapeCoord;
  private int toolSize;

  public ShapePen(Color color, int toolSize) {
    super();
    this.toolSize = toolSize;
    initializeCoord(new Coord());
  }

  private ShapePen(ArrayList<Coord> coords, int toolSize) {
    this.shapeCoord = coords;
    this.toolSize = toolSize;
  }

  public ShapePen(Coord C, Color color, int toolSize) {
    super();
    this.toolSize = toolSize;
    initializeCoord(C);
  }

  public ArrayList<Coord> getShapeCoord() {
    /**
     * Retourne la liste de coordonnées de la forme.
     */
    return shapeCoord;
  }

  public void initializeCoord(Coord C) {
    /**
     * Initialise la liste de coordonnées de la forme.
     */
    shapeCoord = new ArrayList<Coord>();
    shapeCoord.add(C);

    for (int i = -toolSize; i <= toolSize; i++) {
      for (int j = -toolSize; j <= toolSize; j++) {
        if (i == 0 && j == 0) {
          continue; // La coordonnée principale a déjà été ajoutée
        }
        shapeCoord.add(new Coord(C.x + i, C.y + j));
      }
    }
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
    /**
     * Ajoute une coordonnée à la liste de coordonnées de la forme.
     *
     * @param c La coordonnée à ajouter
     */
    shapeCoord.add(c);

    for (int i = -toolSize; i <= toolSize; i++) {
      for (int j = -toolSize; j <= toolSize; j++) {
        if (i == 0 && j == 0) {
          continue; // La coordonnée principale a déjà été ajoutée
        }
        shapeCoord.add(new Coord(c.x + i, c.y + j));
      }
    }
  }

  public void deleteShape(GraphicsContext gc) {
    /**
     * Efface la forme.
     *
     * @param g Le contexte graphique
     */
    gc.setStroke(Color.WHITE);

    for (int i = 0; i < this.shapeCoord.size(); i++) {
      if (i + 1 < this.shapeCoord.size()) {
        Coord pos1 = this.getShapeCoord().get(i);
        Coord pos2 = this.getShapeCoord().get(i + 1);
        double cpx = (pos1.x + pos2.x) / 2.0;
        double cpy = (pos1.y + pos2.y) / 2.0;

        gc.beginPath();
        gc.quadraticCurveTo(pos1.x, pos1.y, cpx, cpy);
        gc.stroke();
        gc.closePath();
        gc.moveTo(cpx, cpy);
      }
    }
  }

  public Coord[] getSelectedCoords() {
    Coord[] extrema = new Coord[2];

    extrema[0] = shapeCoord.get(10);
    extrema[1] = shapeCoord.get(10);

    for (Coord coord : shapeCoord) {
      if (coord.x < extrema[0].x && coord.y < extrema[0].y) extrema[0] = coord;
      if (coord.x > extrema[1].x && coord.y > extrema[1].y) extrema[1] = coord;
    }

    return extrema;
  }

  public void deleteCoord(Coord c) {
    /**
     * Supprime une coordonnée de la liste de coordonnées de la forme.
     *
     * @param c La coordonnée à supprimer
     */
    if (isIn(c)) {
      shapeCoord.remove(c);
    }
  }

  public void printCoord() {
    /**
     * Affiche les coordonnées de la forme.
     */
    for (int i = 0; i < shapeCoord.size(); i++) {
      System.out.println(shapeCoord.get(i).x + " " + shapeCoord.get(i).y);
    }
  }

  public boolean isIn(Coord c) {
    return shapeCoord.contains(c);
  }

  public IShape copy() {
    return new ShapePen(shapeCoord, toolSize);
  }

  public void moveTo(Coord mouse) {
    // TODO Auto-generated method stub
  }
}
