package Model;
import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelReader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Model {

  private Color toolColor = Color.BLACK;
  private int toolSize = 2;
  private String fileTitle = "Untitled";

  public String getfileTitle() {
    return this.fileTitle;
  }

  public void setfileTitle(String title) {
    this.fileTitle = title;
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

  public boolean isCanvasEmpty(Canvas canvas) {
    /**
     * Vérifier si le canvas est vide.
     *
     * @param canvas Le canvas
     * @return true si le canvas est vide, false sinon
     */
    boolean isEmpty = true;
    WritableImage image = canvas.snapshot(null, null);

    // Obtenir une instance de PixelReader
    PixelReader pixelReader = image.getPixelReader();
    int width = (int) canvas.getWidth();
    int height = (int) canvas.getHeight();

    // Vérifier si tous les pixels sont blancs
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            if (!pixelReader.getColor(x, y).equals(Color.WHITE)) {
                isEmpty = false;
                break;
            }
        }
        if (!isEmpty) {
            break;
        }
    }
    return isEmpty;
  }

  public void clearCanvas(Canvas canvas){
    /**
     * Effacer le canvas.
     *
     * @param canvas Le canvas
     */
    int width = (int) canvas.getWidth();
    int height = (int) canvas.getHeight();

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        canvas.getGraphicsContext2D().getPixelWriter().setColor(x, y, Color.WHITE);
      }
    }
  }

  public void resetAttributes() {
    /**
     * Réinitialiser les attributs.
     */
    this.toolColor = Color.BLACK;
    this.toolSize = 2;
    this.fileTitle = "Untitled";
  }

  public boolean showSaveAlert(){

    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Sauvegarder le dessin");
    alert.setHeaderText("Voulez-vous sauvegarder le dessin actuel ?");

    ButtonType saveButton = new ButtonType("Sauvegarder");
    ButtonType dontSaveButton = new ButtonType("Ne pas sauvegarder");
    alert.getButtonTypes().setAll(saveButton, dontSaveButton);

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == saveButton){
      return true;
    } else if (result.get() == dontSaveButton) {
      return false;
    } else {
      return false;
    }
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
    gc.setStroke(this.toolColor);
    gc.setLineWidth(this.toolSize);
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
    gc.setStroke(this.toolColor);
    gc.setLineWidth(this.toolSize);

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
    gc.setStroke(this.toolColor);
    gc.setLineWidth(this.toolSize);

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
    gc.setStroke(this.toolColor);
    gc.setLineWidth(this.toolSize);

    double[] xPoints = new double[] { x1.x, x2.x, x3.x };
    double[] yPoints = new double[] { x1.y, x2.y, x3.y };

    gc.strokePolygon(xPoints, yPoints, 3);
  }
}
