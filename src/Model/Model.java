package Model;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class Model {

  private Color toolColor = Color.BLACK;
  private int toolSize = 12;
  private ArrayList<IShape> shapes = new ArrayList<IShape>();
  private GraphicsContext gc;
  private Canvas canvas;
  private ShapeSelector selector;
  private IShape shapeSelected;

  public void addShape(IShape shape) {
    if (!shape.isShape(ShapeTypes.SELECT)) {
      shapes.add(shape);
      System.out.println("New object added");
    }
  }

  public void setSelector(ShapeSelector selector) {
    this.selector = selector;
  }

  public void setGraphicsContext(GraphicsContext gc) {
    this.gc = gc;
  }

  public void setCanvas(Canvas canvas) {
    this.canvas = canvas;
  }

  public void setColor(Color color) {
    this.toolColor = color;
  }

  public void setColor(ColorPicker cPicker) {
    this.toolColor = cPicker.getValue();
  }

  public void setToolSize(int size) {
    this.toolSize = size;
  }

  public void setToolSize(String size) {
    this.toolSize = Integer.parseInt(size.replace("px", ""));
  }

  public Color getColor() {
    return this.toolColor;
  }

  public String getToolSizeStr() {
    return Integer.toString(this.toolSize);
  }

  public int getToolSize() {
    return this.toolSize;
  }

  public void increaseToolSize() {
    this.toolSize++;
  }

  public void decreaseToolSize() {
    this.toolSize--;
  }

  public ArrayList<Coord> getNeighbors(Coord c) {
    ArrayList<Coord> neighbors = new ArrayList<>();
    neighbors.add(new Coord(c.x - 1, c.y));
    neighbors.add(new Coord(c.x + 1, c.y));
    neighbors.add(new Coord(c.x, c.y - 1));
    neighbors.add(new Coord(c.x, c.y + 1));
    System.out.println(neighbors);
    return neighbors;
  }

  public void drawLine(Coord start, Coord end) {
    /**
     * Dessiner une ligne.
     *
     * @param gc   Le contexte graphique
     * @param start Les coordonnées de départ
     * @param end Les coordonnées d'arrivée
     */

    this.gc.setStroke(this.toolColor);
    this.gc.setLineWidth(this.toolSize);
    this.gc.strokeLine(start.x, start.y, end.x, end.y);
  }

  public void drawRectangle(Coord start, Coord end) {
    /**
     * Dessiner un rectangle.
     *
     * @param gc   Le contexte graphique
     * @param start Les coordonnées de départ
     * @param end Les coordonnées d'arrivée
     */
    this.gc.setStroke(this.toolColor);
    this.gc.setLineWidth(this.toolSize);

    double width = Math.abs(start.x - end.x);
    double height = Math.abs(start.y - end.y);
    double x = Math.min(start.x, end.x);
    double y = Math.min(start.y, end.y);

    this.gc.strokeRect(x, y, width, height);
  }

  public void drawCircle(Coord start, Coord end) {
    /**
     * Dessiner un cercle.
     *
     * @param gc   Le contexte graphique
     * @param start Les coordonnées de départ
     * @param end Les coordonnées d'arrivée
     */
    gc.setStroke(this.toolColor);
    gc.setLineWidth(this.toolSize);

    double radius = start.distance(end);

    gc.strokeOval(start.x - radius, start.y - radius, radius * 2, radius * 2);
  }

  public void drawTriangle(Coord x1, Coord x2, Coord x3) {
    /**
     * Dessiner un triangle.
     *
     * @param gc Le contexte graphique
     * @param x1 Les coordonnées du premier point
     * @param x2 Les coordonnées du deuxième point
     * @param x3 Les coordonnées du troisième point
     */
    gc.setStroke(this.toolColor);
    gc.setLineWidth(this.toolSize);

    double[] xPoints = new double[] { x1.x, x2.x, x3.x };
    double[] yPoints = new double[] { x1.y, x2.y, x3.y };

    gc.strokePolygon(xPoints, yPoints, 3);
  }

  public void drawPen(ArrayList<Coord> coords) {
    for (int i = 1; i < coords.size(); i++) {
      drawPen(coords.get(0), coords.get(i));
    }
  }

  public void drawSelected(Coord[] coords) {
    gc.setStroke(Color.BLUE);
    gc.setLineDashes(5);
    gc.setLineWidth(2);

    double offset = 0;

    double width = Math.abs(coords[0].x - coords[1].x) - offset;
    double height = Math.abs(coords[0].y - coords[1].y) - offset;
    double x = Math.min(coords[0].x, coords[1].x) - offset;
    double y = Math.min(coords[0].y, coords[1].y) - offset;

    gc.strokeRect(x, y, width, height);
  }

  public void drawPen(Coord posStart, Coord posCurrent) {
    double centerX = (posStart.x + posCurrent.x) / 2.0;
    double centerY = (posStart.y + posCurrent.y) / 2.0;

    gc.quadraticCurveTo(posStart.x, posStart.y, centerX, centerY);
    gc.stroke();
    gc.beginPath();
    gc.moveTo(centerX, centerY);
  }

  public void printSelectedShape(Coord mouse) {
    shapeSelected = null;
    redraw();
    
    for (int i = shapes.size() - 1; i >= 0; i--) {
      IShape iShape = shapes.get(i);

      if (iShape.isIn(mouse)) {
        drawSelected(iShape.getSelectedCoords());
        shapeSelected = iShape;

        break;
      }
    }
  }

  public void moveShape(Coord mouse) {
    shapeSelected.moveTo(mouse);
    redraw();
    drawSelected(shapeSelected.getSelectedCoords());
  }

  public void redraw() {
    this.gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    for (IShape iShape : shapes) {
      if (iShape.isShape(ShapeTypes.LINE)) {
        ShapeLine iShapeLine = (ShapeLine) iShape;
        drawLine(iShapeLine.getStart(), iShapeLine.getEnd());
      } else if (iShape.isShape(ShapeTypes.CIRCLE)) {
        ShapeCircle iShapeCircle = (ShapeCircle) iShape;
        drawCircle(iShapeCircle.getCenter(), iShapeCircle.getRadiusCoord());
      }
    }
  }
}
