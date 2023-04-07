package Model;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class PenShape extends Shape {

  private ArrayList<Coord> shapeCoord;
  private int toolSize;

  public PenShape(Coord C, Color color, int appSize) {
    super();
    this.toolSize = appSize;
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

  public boolean isInList(Coord c) {
    /**
     *
     * @param c La coordonnée à vérifier
     *
     * @return true si la coordonnée est dans la liste, false sinon
     */
    for (int i = 0; i < shapeCoord.size(); i++) {
      if (shapeCoord.get(i).x == c.x && shapeCoord.get(i).y == c.y) {
        return (true);
      }
    }
    return (false);
  }

  public void deleteCoord(Coord c) {
    /**
     * Supprime une coordonnée de la liste de coordonnées de la forme.
     *
     * @param c La coordonnée à supprimer
     */
    if (isInList(c)) {
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
}
