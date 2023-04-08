package Model;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class Model {

  private Color toolColor = Color.BLACK;
  private int toolSize = 2;
  private ArrayList<IShape> shapes = new ArrayList<IShape>();
  private GraphicsContext gc;
  private Canvas canvas;
  private IShape shapeSelected;
  private Button btnBack, btnFront;
  private ColorPicker cPicker;
  private TextField inputSize;

  public void addShape(IShape shape) {
    if (!shape.isShape(ShapeTypes.SELECT)) {
      shapes.add(shape);
      System.out.println("New object added");
    }
  }

  public void setBtns(Button btnBack, Button btnFront) {
    this.btnBack = btnBack;
    this.btnFront = btnFront;
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

    if (shapeSelected != null) {
      shapeSelected.setToolColor(toolColor);
      redraw();
    }
  }

  public void setCPicker(ColorPicker cPicker) {
    this.cPicker = cPicker;
  }

  public void setInputSize(TextField inputSize) {
    this.inputSize = inputSize;
  }

  public void setToolSize(int size) {
    this.toolSize = size;

    if (shapeSelected != null) {
      shapeSelected.setToolSize(size);
      redraw();
    }
  }

  public void setToolSize(String size) {
    this.toolSize = Integer.parseInt(size);
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

  public void toFront() {
    IShape shape = this.shapeSelected;
    int i = shapes.indexOf(shapeSelected);

    if (i < shapes.size() - 1) {
      shapes.remove(i);
      shapes.add(shape);

      highlightShape(shape);
      redraw();
    }
  }

  public void toBack() {
    IShape shape = this.shapeSelected;
    int i = shapes.indexOf(shapeSelected);

    if (i > 0) {
      shapes.remove(i);
      shapes.add(i - 1, shape);

      highlightShape(shape);
      redraw();
    }
  }

  public void drawPen(ArrayList<Coord> coords) {
    for (int i = 1; i < coords.size(); i++) {
      drawPen(coords.get(i - 1), coords.get(i));
    }
  }

  public void drawHighlight(Coord[] coords) {
    double offset = 0;

    double width = Math.abs(coords[0].x - coords[1].x) - offset;
    double height = Math.abs(coords[0].y - coords[1].y) - offset;
    double x = Math.min(coords[0].x, coords[1].x) - offset;
    double y = Math.min(coords[0].y, coords[1].y) - offset;

    gc.setStroke(Color.BLUE);
    gc.setLineDashes(5);
    gc.setLineWidth(2);

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

  public void selectShape(Coord mouse) {
    shapeSelected = findShape(mouse);
    highlightShape(shapeSelected);
    redraw();
  }

  public IShape findShape(Coord mouse) {
    for (int i = shapes.size() - 1; i >= 0; i--) {
      IShape iShape = shapes.get(i);

      if (iShape.isIn(mouse)) return iShape;
    }

    return null;
  }

  public void updateAppState() {
    btnFront.setDisable(true);
    btnBack.setDisable(true);

    if (shapeSelected == null) {
      cPicker.setValue(toolColor);
      inputSize.setText(toolSize + "");
    } else {
      cPicker.setValue(shapeSelected.getToolColor());
      inputSize.setText(shapeSelected.getToolSize() + "");
    }
  }

  public void unselectShape() {
    shapeSelected = null;
    updateAppState();
    redraw();
  }

  private void highlightShape(IShape shape) {
    if (shape == null) return;

    drawHighlight(shape.getSelectedCoords());
    updateAppState();

    if (shapes.indexOf(shape) > 0) btnBack.setDisable(false);
    if (shapes.indexOf(shape) < shapes.size() - 1) btnFront.setDisable(false);
  }

  public void moveShape(Coord mouse) {
    if (shapeSelected != null) {
      shapeSelected.moveTo(mouse);
      redraw();
      drawHighlight(shapeSelected.getSelectedCoords());
    }
  }

  public void removeShape(Coord mouse) {
    shapeSelected = findShape(mouse);
    shapes.remove(shapeSelected);
    updateAppState();
    unselectShape();
    System.out.println("Remove object " + shapes.size());
  }

  public void redraw() {
    this.gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    for (IShape iShape : shapes) {
      if (!iShape.isShape(ShapeTypes.PEN)) iShape.draw(gc);
    }

    if (shapeSelected != null) drawHighlight(shapeSelected.getSelectedCoords());
  }
}
