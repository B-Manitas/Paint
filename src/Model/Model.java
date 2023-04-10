package Model;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import java.util.Optional;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Model {
    // Déclaration des attributs
    private Color toolColor = Constant.DEFAULT_COLOR;
    private int toolSize = Constant.DEFAULT_TOOL_SIZE;
    private String fileTitle = Constant.DEFAULT_FILENAME;
    private double zoomFactor = Constant.DEFAULT_ZOOM;

    private ArrayList<IShape> shapes = new ArrayList<IShape>();
    private IShape selectedShape;
    private ITools selectedTools;

    // Déclaration des widgets
    private GraphicsContext gc;
    private Canvas canvas;
    private Button btnBack, btnFront;
    private Label lblZoom;
    private ColorPicker cPicker;
    private TextField inputSize;

    public void setWidget(Button btnBack, Button btnFront, ColorPicker cPicker, Label lblZoom, TextField inputSize) {
        /**
         * Définit les widgets de la vue
         */
        this.btnBack = btnBack;
        this.btnFront = btnFront;
        this.cPicker = cPicker;
        this.lblZoom = lblZoom;
        this.inputSize = inputSize;
    }

    public void setCanvas(Canvas canvas) {
        /**
         * Définit le canvas
         */
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void setSelectedTools(ITools tool) {
        /**
         * Définit l'outil sélectionné
         * 
         * @param tool Outil sélectionné
         */
        this.selectedTools = tool;
    }

    public void setfileTitle(String title) {
        /**
         * Définit le titre du fichier
         * 
         * @param title Titre du fichier
         */
        this.fileTitle = title;
    }

    public void setColor(ColorPicker cPicker) {
        /**
         * Définit la couleur de l'outil
         * 
         * @param cPicker ColorPicker de la vue
         */
        this.toolColor = cPicker.getValue();

        if (selectedShape != null) {
            selectedShape.setColor(toolColor);
            redraw();
        }
    }

    public void setSize(int size) {
        /**
         * Définit la taille de l'outil
         * 
         * @param size Taille de l'outil
         */
        this.toolSize = size;

        if (selectedShape != null) {
            selectedShape.setBdSize(size);
            redraw();
        }
    }

    public String getZoomRatio() {
        /**
         * Retourne le zoom sous forme de chaîne de caractères
         * 
         * @return Zoom sous forme de chaîne de caractères
         */
        return (int) Math.round(zoomFactor * 100) + "%";
    }

    public String getfileTitle() {
        /**
         * Retourne le titre du fichier
         * 
         * @return Titre du fichier
         */
        return this.fileTitle;
    }

    public IShape getSelectedShape() {
        /**
         * Retourne la forme sélectionnée
         * 
         * @return Forme sélectionnée
         */
        return this.selectedShape;
    }

    public Color getColor() {
        /**
         * Retourne la couleur de l'outil
         * 
         * @return Couleur de l'outil
         */
        return this.toolColor;
    }

    public String getSizeStr() {
        /**
         * Retourne la taille de l'outil sous forme de chaîne de caractères
         * 
         * @return Taille de l'outil sous forme de chaîne de caractères
         */
        return Integer.toString(this.toolSize);
    }

    public int getSize() {
        /**
         * Retourne la taille de l'outil
         * 
         * @return Taille de l'outil
         */
        return this.toolSize;
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
            for (int x = 0; x < width; x++)
                if (!pixelReader.getColor(x, y).equals(Color.WHITE)) {
                    isEmpty = false;
                    break;
                }

            if (!isEmpty)
                break;
        }

        return isEmpty;
    }

    public boolean isShapeSelected() {
        /**
         * Retourne si une forme est sélectionnée
         * 
         * @return Vrai si une forme est sélectionnée, faux sinon
         */
        return this.selectedShape != null;
    }

    public void shapeSelect(Coord mouse) {
        /**
         * Sélectionner une forme.
         * 
         * @param mouse Coordonnées de la souris
         */
        ToolsSelector selector = (ToolsSelector) selectedTools;

        if (selector.canMove(mouse) || selector.canResize(mouse))
            return;

        selectedShape = shapeFind(mouse);

        if (selectedShape == null)
            selector.reset();

        shapeHighlight(selectedShape);
        redraw();

    }

    public IShape shapeFind(Coord mouse) {
        /**
         * Trouver une forme.
         * 
         * @param mouse Coordonnées de la souris
         * @return La forme trouvée
         */
        for (int i = shapes.size() - 1; i >= 0; i--) {
            IShape iShape = shapes.get(i);

            if (iShape.isInArea(mouse))
                return iShape;
        }

        return null;
    }

    public void shapeAdd(IShape shape) {
        /**
         * Ajoute une forme à la liste des formes
         * 
         * @param shape Forme à ajouter
         */

        if (!shape.isShape(ShapeTypes.SELECT))
            shapes.add(shape);
    }

    public void shapeUnselect() {
        /**
         * Désélectionner une forme.
         */
        selectedShape = null;
        updateApp();
        redraw();
    }

    private void shapeHighlight(IShape shape) {
        /**
         * Mettre en surbrillance une forme.
         * 
         * @param shape La forme
         */
        if (shape == null)
            return;

        Coord[] coordHighlight = shape.getArea();
        ((ToolsSelector) selectedTools).setSelection(coordHighlight[0], coordHighlight[1]);
        drawHighlight(coordHighlight);

        updateApp();

        if (shapes.indexOf(shape) > 0)
            btnBack.setDisable(false);

        if (shapes.indexOf(shape) < shapes.size() - 1)
            btnFront.setDisable(false);
    }

    public void shapeMove(Coord mouse) {
        /**
         * Déplacer une forme.
         * 
         * @param mouse Coordonnées de la souris
         */
        if (selectedShape != null) {
            selectedShape.moveTo(mouse);
            redraw();
            drawHighlight(selectedShape.getArea());
        }
    }

    public void shapeRemove(Coord mouse) {
        /**
         * Supprimer une forme.
         * 
         * @param mouse Coordonnées de la souris
         */
        selectedShape = shapeFind(mouse);
        shapes.remove(selectedShape);
        updateApp();
        shapeUnselect();
    }

    private void shapeResize(Coord mouse) {
        /**
         * Redimensionner une forme.
         * 
         * @param mouse Coordonnées de la souris
         */
        if (selectedShape != null) {
            selectedShape.setPEnd(mouse);
            redraw();
            drawHighlight(selectedShape.getArea());
        }
    }

    public void onShapeDrag(Coord posStart, Coord posCurrent) {
        /**
         * Sélectionner une forme avec la souris.
         * 
         * @param posStart   Coordonnées de départ
         * @param posCurrent Coordonnées actuelles
         */
        ToolsSelector selector = ((ToolsSelector) selectedTools);

        if (selector.canResize(posStart))
            shapeResize(posCurrent);

        else if (selector.canMove(posStart))
            shapeMove(posCurrent);
    }

    public void increaseSize() {
        /**
         * Augmente la taille de l'outil
         */
        this.toolSize++;
    }

    public void decreaseSize() {
        /**
         * Diminue la taille de l'outil
         */
        this.toolSize--;
    }

    public void toFront() {
        /**
         * Déplace la forme sélectionnée au premier plan
         */
        IShape shape = this.selectedShape;
        int i = shapes.indexOf(selectedShape);

        if (i < shapes.size() - 1) {
            shapes.remove(i);
            shapes.add(shape);

            shapeHighlight(shape);
            redraw();
        }
    }

    public void toBack() {
        /**
         * Déplace la forme sélectionnée au dernier plan
         */
        IShape shape = this.selectedShape;
        int i = shapes.indexOf(selectedShape);

        if (i > 0) {
            shapes.remove(i);
            shapes.add(i - 1, shape);

            shapeHighlight(shape);
            redraw();
        }
    }

    public void drawHighlight(Coord[] coords) {
        /**
         * Dessine un rectangle de sélection
         * 
         * @param coords Coordonnées du rectangle
         */
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

    public void clearCanvas(Canvas canvas) {
        /**
         * Effacer le canvas.
         *
         * @param canvas Le canvas
         */
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                canvas.getGraphicsContext2D().getPixelWriter().setColor(x, y, Color.WHITE);
    }

    public boolean showSaveAlert() {
        /**
         * Afficher une boîte de dialogue de sauvegarde.
         *
         * @return true si l'utilisateur a cliqué sur le bouton "Sauvegarder", false
         *         sinon
         */

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
        /**
         * Dessiner une ligne avec l'outil crayon.
         * 
         * @param posStart   Coordonnées de départ
         * @param posCurrent Coordonnées actuelles
         */
        double centerX = (posStart.x + posCurrent.x) / 2.0;
        double centerY = (posStart.y + posCurrent.y) / 2.0;

        gc.quadraticCurveTo(posStart.x, posStart.y, centerX, centerY);
        gc.stroke();
        gc.beginPath();
        gc.moveTo(centerX, centerY);
    }

    public void updateApp() {
        /**
         * Mettre à jour l'état de l'application.
         */
        btnFront.setDisable(true);
        btnBack.setDisable(true);
        lblZoom.setText(getZoomRatio());
        canvas.requestFocus();

        if (selectedShape == null) {
            cPicker.setValue(toolColor);
            inputSize.setText(toolSize + "");
        } else {
            cPicker.setValue(selectedShape.getColor());
            inputSize.setText(selectedShape.getBdSize() + "");
        }
    }

    public void redraw() {
        /**
         * Redessiner le canvas.
         */
        this.gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (IShape iShape : shapes) {
            if (!iShape.isShape(ShapeTypes.PEN))
                iShape.draw(gc);
        }

        if (selectedShape != null)
            drawHighlight(selectedShape.getArea());
    }

    public void reset() {
        /**
         * Réinitialiser les attributs.
         */
        this.toolColor = Color.BLACK;
        this.toolSize = 2;
        this.zoomFactor = 1;
        this.fileTitle = "Untitled";
        shapes.clear();
        updateApp();
        shapeUnselect();
        redraw();
    }

    public void zoomIn() {
        /**
         * Dézoomer sur le canvas.
         */
        if (zoomFactor > Constant.MIN_ZOOM) {
            zoomFactor = Math.max(zoomFactor - .1, Constant.MIN_ZOOM);
            shapeUnselect();

            for (IShape iShape : shapes)
                iShape.zoomIn();

            redraw();
        }
    }

    public void zoomOut() {
        /**
         * Zoomer sur le canvas.
         */
        if (zoomFactor < Constant.MAX_ZOOM) {
            zoomFactor = Math.min(zoomFactor + .1, Constant.MAX_ZOOM);

            shapeUnselect();

            for (IShape iShape : shapes)
                iShape.zoomOut();

            redraw();
        }
    }
}
