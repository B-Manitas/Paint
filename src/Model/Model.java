package Model;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class Model {

  private Color toolColor = Color.BLACK;
  private int toolSize = 2;

  public void setColor(Color color) {
    toolColor = color;
  }

  public void setColor(ColorPicker cPicker) {
    toolColor = cPicker.getValue();
  }

  public void setToolSize(int size) {
    toolSize = size;
  }

  public void setToolSize(String size) {
    toolSize = Integer.parseInt(size.replace("px", ""));
  }

  public Color getColor() {
    return toolColor;
  }

  public int getToolSize() {
    return toolSize;
  }

  public ArrayList<Coord> getNeighbors(Coord c) {
    ArrayList<Coord> neighbors = new ArrayList<>();
    neighbors.add(Coord.createCoord(c.x - 1, c.y));
    neighbors.add(Coord.createCoord(c.x + 1, c.y));
    neighbors.add(Coord.createCoord(c.x, c.y - 1));
    neighbors.add(Coord.createCoord(c.x, c.y + 1));
    System.out.println(neighbors);
    return neighbors;
  }

  public void drawLine(GraphicsContext gc, Coord start, Coord end) {
    /**
     * Dessiner une ligne.
     *
     * @param gc   Le contexte graphique
     * @param start Les coordonnées de départ
     * @param end Les coordonnées d'arrivée
     */
    gc.setStroke(toolColor);
    gc.setLineWidth(toolSize);
    gc.strokeLine(start.x, start.y, end.x, end.y);
  }

  public void drawRectangle(GraphicsContext gc, Coord start, Coord end) {
    /**
     * Dessiner un rectangle.
     *
     * @param gc   Le contexte graphique
     * @param start Les coordonnées de départ
     * @param end Les coordonnées d'arrivée
     */
    gc.setStroke(toolColor);
    gc.setLineWidth(toolSize);

    double width = Math.abs(start.x - end.x);
    double height = Math.abs(start.y - end.y);
    double x = Math.min(start.x, end.x);
    double y = Math.min(start.y, end.y);

    gc.strokeRect(x, y, width, height);
  }

  public void drawCircle(GraphicsContext gc, Coord start, Coord end) {
    /**
     * Dessiner un cercle.
     *
     * @param gc   Le contexte graphique
     * @param start Les coordonnées de départ
     * @param end Les coordonnées d'arrivée
     */
    gc.setStroke(toolColor);
    gc.setLineWidth(toolSize);

    double radius = start.getDistance(end);
    double centerX = start.x;
    double centerY = start.y;

    gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
  }

  public void drawTriangle(GraphicsContext gc, Coord x1, Coord x2, Coord x3) {
    /**
     * Dessiner un triangle.
     *
     * @param gc Le contexte graphique
     * @param x1 Les coordonnées du premier point
     * @param x2 Les coordonnées du deuxième point
     * @param x3 Les coordonnées du troisième point
     */
    gc.setStroke(toolColor);
    gc.setLineWidth(toolSize);

    double[] xPoints = new double[] { x1.x, x2.x, x3.x };
    double[] yPoints = new double[] { x1.y, x2.y, x3.y };

    gc.strokePolygon(xPoints, yPoints, 3);
  }
}
