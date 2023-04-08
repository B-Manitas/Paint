package Model;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import java.util.Optional;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

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
    private ITools toolSelected;

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

    public void setToolSelected(ITools tool) {
        this.toolSelected = tool;
    }

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

    public void clearCanvas(Canvas canvas) {
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

    public boolean showSaveAlert() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Sauvegarder le dessin");
        alert.setHeaderText("Voulez-vous sauvegarder le dessin actuel ?");

        ButtonType saveButton = new ButtonType("Sauvegarder");
        ButtonType dontSaveButton = new ButtonType("Ne pas sauvegarder");
        alert.getButtonTypes().setAll(saveButton, dontSaveButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == saveButton) {
            return true;
        } else if (result.get() == dontSaveButton) {
            return false;
        } else {
            return false;
        }
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

            if (iShape.isIn(mouse))
                return iShape;
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
        if (shape == null)
            return;

        Coord[] coordHighlight = shape.getSelectedCoords();
        drawHighlight(coordHighlight);

        ((ToolsSelector) toolSelected).setStart(coordHighlight[0]);
        ((ToolsSelector) toolSelected).setEnd(coordHighlight[1]);

        updateAppState();

        if (shapes.indexOf(shape) > 0)
            btnBack.setDisable(false);
        if (shapes.indexOf(shape) < shapes.size() - 1)
            btnFront.setDisable(false);
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
            if (!iShape.isShape(ShapeTypes.PEN))
                iShape.draw(gc);
        }

        if (shapeSelected != null)
            drawHighlight(shapeSelected.getSelectedCoords());
    }

    public void onDragSelect(Coord posStart, Coord posCurrent) {        
        if (((ToolsSelector) toolSelected).isResized(posStart))
            resizeShape(posCurrent);
        else
            moveShape(posCurrent);
    }

    private void resizeShape(Coord mouse) {
        if (shapeSelected != null) {
            shapeSelected.addCoord(mouse);
            redraw();
            drawHighlight(shapeSelected.getSelectedCoords());
        }
    }
}
