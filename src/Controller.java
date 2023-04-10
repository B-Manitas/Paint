import Model.*;
import java.io.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;
import javax.imageio.ImageIO;

public class Controller {
    // Déclare les attributs de l'app
    public Model model = new Model();
    private Coord posStart, posCurrent = new Coord();
    private String selectedStyle = "-fx-background-color: #D3E8EE";

    // Déclare les attributs de l'outil
    public IShape selectedShape = new ShapeLine(posStart, model.getSize(), model.getColor());
    public ITools selectedTools = new ToolsShape();

    // Déclare les attributs de l'interface
    private Image previousImage;
    public static boolean isPressedCtrlS, isPressedCtrlN, isPressedCtrlO, isPressedCtrlW = false;

    // Déclare les attributs de la vue
    @FXML
    private Canvas canvas;
    @FXML
    private Label lblLog, lblZoom;
    @FXML
    private TextField inputFilename;
    @FXML
    private MenuItem btnNew, btnOpen, btnSave, btnClose;
    @FXML
    private MenuButton btnShape;
    @FXML
    private CustomMenuItem btnRect, btnCircle, btnTriangle;
    @FXML
    private TextField inputSize;
    @FXML
    private Button btnLine, btnEraser, btnSizeUp, btnSizeDown, btnSelection, btnFront, btnBack, btnText;
    @FXML
    private ToggleGroup groupSize;
    @FXML
    private ColorPicker cPicker;
    @FXML
    private DialogPane helpPane;

    @FXML
    private void onEditFilename(ActionEvent event) {
        /**
         * Editer le nom du fichier
         * 
         */
        model.setfileTitle(inputFilename.getText());
    }

    @FXML
    private void onNew(ActionEvent event) {
        /**
         * Créer un nouveau fichier
         */

        // Si le canvas n'est pas vide propose de sauvegarder
        if (!model.isCanvasEmpty(canvas)) {
            boolean result = model.showSaveAlert();

            if (result == true)
                onSave(event);

        }

        // Vider le canvas
        model.clearCanvas(canvas);
        model.reset();
        cancelStyle();

        lblLog.setText("Création d'un nouveau fichier");
    }

    @FXML
    private void onOpen(ActionEvent event) {
        /**
         * Ouvrir un fichier
         */
        // Si le canvas n'est pas vide et propose de sauvegarder
        if (!model.isCanvasEmpty(canvas)) {
            boolean result = model.showSaveAlert();

            if (result)
                onSave(event);
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un fichier PNG");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PNG", "*.png"));
        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());

        if (file != null) {
            // Charger l'image depuis le fichier
            Image image = new Image(file.toURI().toString());
            model.setfileTitle(file.getName().replace(".png", ""));
            inputFilename.setText(model.getfileTitle());

            // Redimensionner l'image à la taille du canva
            double targetWidth = canvas.getWidth();
            double targetHeight = canvas.getHeight();
            double scaleFactor = Math.min(targetWidth / image.getWidth(), targetHeight / image.getHeight());
            double scaledWidth = image.getWidth() * scaleFactor;
            double scaledHeight = image.getHeight() * scaleFactor;

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(scaledWidth);
            imageView.setFitHeight(scaledHeight);

            // Dessiner l'image redimensionnée sur le canvas
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.drawImage(imageView.snapshot(null, null), 0, 0);
            cancelStyle();
        }
    }

    @FXML
    private void onSave(ActionEvent event) {
        /**
         * Sauvegarder le fichier
         */
        // Récupérer la racine de la scène
        Node node = canvas;
        Stage stage = (Stage) node.getScene().getWindow();

        // Capturer l'image du canvas
        WritableImage image = new WritableImage(
                (int) canvas.getWidth(),
                (int) canvas.getHeight());
        node.snapshot(null, image);

        // Créer un objet File pour enregistrer l'image en PNG
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer en tant qu'image PNG");
        fileChooser.setInitialFileName(model.getfileTitle() + ".png");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PNG", "*.png"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                lblLog.setText("Fichier enregistré sous " + file.getName());
            } catch (IOException ex) {
                lblLog.setText("Erreur lors de l'enregistrement du fichier.");
                ex.printStackTrace();
            }
        }

        lblLog.setText("Fichier enregistré");
    }

    @FXML
    private void onExit(ActionEvent event) {
        /**
         * Fermer l'application
         */
        // Vérifier si le canvas n'est pas vide, et propose de sauvegarder
        if (!model.isCanvasEmpty(canvas)) {
            boolean result = model.showSaveAlert();

            if (result == true)
                onSave(event);

        }

        lblLog.setText("Fermeture de l'application");
        Platform.exit();
    }

    @FXML
    private void onHelp(ActionEvent event) {
        /**
         * Afficher l'aide.
         */
        helpPane.setVisible(!helpPane.isVisible());
    }

    @FXML
    private void onZoomIn(ActionEvent event) {
        /**
         * Zoomer en avant sur l'image.
         */
        model.zoomIn();
        lblZoom.setText(model.getZoomRatio());
    }

    @FXML
    private void onZoomOut(ActionEvent event) {
        /**
         * Zoomer en arrière sur l'image.
         */
        model.zoomOut();
        lblZoom.setText(model.getZoomRatio());
    }

    @FXML
    private void onChangeColor(ActionEvent event) {
        /**
         * Sélectionner une couleur.
         */
        model.setColor(cPicker);
        lblLog.setText("Couleur modifiée");
    }

    @FXML
    private void onChangeSize() {
        /**
         * Modifier la taille de l'outil.
         */
        String text = inputSize.getText();
        try {
            int newSize = Integer.parseInt(text);

            if (newSize < 1 || newSize > 20)
                throw new NumberFormatException();

            model.setSize(newSize);
            lblLog.setText("Taille de l'outil modifiée.");
            inputSize.setText(model.getSizeStr());
        }

        catch (NumberFormatException ex) {
            inputSize.setText(model.getSizeStr());
            lblLog.setText("Veuillez entrer un nombre valide.");
        }
    }

    @FXML
    private void onIncreaseSize(ActionEvent event) {
        /**
         * Augmenter la taille de l'outil.
         */
        if (model.getSize() != 20) {
            model.increaseSize();
            inputSize.setText(Integer.toString(model.getSize()));
            lblLog.setText("La taille de l'outil a été augmentée.");
        }
    }

    @FXML
    private void onDecreaseSize(ActionEvent event) {
        /**
         * Diminuer la taille de l'outil.
         */
        if (model.getSize() != 1) {
            model.decreaseSize();
            inputSize.setText(model.getSizeStr());
            lblLog.setText("La taille de l'outil a été diminuée.");
        }
    }

    @FXML
    private void onEraser(ActionEvent event) {
        /**
         * Sélectionner la gomme.
         */
        cancelStyle();
        btnEraser.setStyle(selectedStyle);
        lblLog.setText("Cliquer sur une forme pour la supprimer.");

        model.shapeUnselect();
        selectedTools = new ToolsEraser();
    }

    @FXML
    private void onAddText() {
        /**
         * Sélectionner le texte.
         */
        cancelStyle();
        btnText.setStyle(selectedStyle);
        lblLog.setText("Cliquer pour ajouter du texte");

        model.shapeUnselect();
        selectedShape = new ShapeText(posCurrent, model.getSize(), model.getColor());
        selectedTools = new ToolsShape();
    }

    @FXML
    private void onAddLine(ActionEvent event) {
        /**
         * Sélectionner la forme ligne.
         */
        cancelStyle();
        btnLine.setStyle(selectedStyle);
        lblLog.setText("Cliquer et déplacer pour dessiner une ligne.");

        model.shapeUnselect();
        model.setColor(cPicker);
        selectedShape = new ShapeLine(posStart, model.getSize(), model.getColor());
        selectedTools = new ToolsShape();
    }

    @FXML
    private void onAddRect(ActionEvent event) {
        /**
         * Sélectionner la forme rectangle.
         */
        cancelStyle();
        btnRect.setStyle(selectedStyle);
        btnShape.setStyle(selectedStyle);
        lblLog.setText("Cliquer et déplacer pour dessiner un rectangle.");

        model.shapeUnselect();
        model.setColor(cPicker);
        selectedShape = new ShapeRect(posStart, model.getSize());
        selectedTools = new ToolsShape();
    }

    @FXML
    private void onAddCircle(ActionEvent event) {
        /**
         * Sélectionner la forme cercle.
         */
        cancelStyle();
        btnCircle.setStyle(selectedStyle);
        btnShape.setStyle(selectedStyle);
        lblLog.setText("Cliquer et déplacer pour dessiner un cercle.");

        model.shapeUnselect();
        model.setColor(cPicker);
        selectedShape = new ShapeCircle(posStart, model.getSize());
        selectedTools = new ToolsShape();
    }

    @FXML
    private void onAddTrig(ActionEvent event) {
        /**
         * Sélectionner la forme triangle.
         */
        cancelStyle();
        btnTriangle.setStyle(selectedStyle);
        btnShape.setStyle(selectedStyle);
        lblLog.setText("Cliquer et déplacer pour dessiner un triangle.");

        model.shapeUnselect();
        model.setColor(cPicker);
        selectedShape = new ShapeTriangle(posStart, model.getSize());
        selectedTools = new ToolsShape();
    }

    @FXML
    private void onToFront() {
        /**
         * Mettre la forme sélectionnée au premier plan.
         */
        model.toFront();
    }

    @FXML
    private void onToBack() {
        /**
         * Mettre la forme sélectionnée au dernier plan.
         */
        model.toBack();
    }

    @FXML
    private void onSelection(ActionEvent event) {
        /**
         * Sélectionner la forme.
         */
        cancelStyle();
        lblLog.setText("Cliquer sur une forme pour la sélectionner.");
        btnSelection.setStyle(selectedStyle);

        selectedTools = new ToolsSelector();
    }

    @FXML
    private void keyEvent(KeyEvent e) {
        /**
         * Gestion des raccourcis clavier
         * 
         * Ctrl + S : Sauvegarder
         * Ctrl + N : Nouveau fichier
         * Ctrl + O : Ouvrir un fichier
         * Ctrl + W : Fermer l'application
         * 
         */
        // Ctrl + S
        if (e.getCode() == KeyCode.CONTROL)
            isPressedCtrlS = true;

        else if (e.getCode() == KeyCode.S && isPressedCtrlS) {
            isPressedCtrlS = false;
            onSave(null);
        }

        else
            isPressedCtrlS = false;

        // Ctrl + N
        if (e.getCode() == KeyCode.CONTROL)
            isPressedCtrlN = true;

        else if (e.getCode() == KeyCode.N && isPressedCtrlN) {
            isPressedCtrlN = false;
            onNew(null);
        }

        else
            isPressedCtrlN = false;

        // Ctrl + O
        if (e.getCode() == KeyCode.CONTROL)
            isPressedCtrlO = true;

        else if (e.getCode() == KeyCode.O && isPressedCtrlO) {
            isPressedCtrlO = false;
            onOpen(null);
        }

        else
            isPressedCtrlO = false;

        // Ctrl + W
        if (e.getCode() == KeyCode.CONTROL)
            isPressedCtrlW = true;

        else if (e.getCode() == KeyCode.W && isPressedCtrlW) {
            isPressedCtrlW = false;
            onExit(null);

        } else
            isPressedCtrlW = false;

    }

    @FXML
    private void initialize() {
        /**
         * Initialisation de l'application.
         */
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Coord.setCenter(canvas.getWidth() / 2, canvas.getHeight() / 2);

        // Initialisation du modèle
        model.setCanvas(canvas);
        model.setWidget(btnBack, btnFront, cPicker, lblZoom, inputSize);

        // Lorque la souris est en mouvement
        canvas.setOnMouseMoved(e -> {
            Coord mouse = Coord.getCoordMouse(e);

            // Outils de forme
            if (selectedTools.isTool(ToolsTypes.SHAPE))
                canvas.setCursor(Cursor.CROSSHAIR);

            // Outils gomme
            else if (selectedTools.isTool(ToolsTypes.ERASER))
                canvas.setCursor(Cursor.HAND);

            // Outils de sélection
            else if (selectedTools.isTool(ToolsTypes.SELECT)) {
                ToolsSelector selector = (ToolsSelector) selectedTools;

                // Si la souris peut redimensionner la forme horizontalement
                if (selector.canResizeH(mouse))
                    canvas.setCursor(Cursor.H_RESIZE);

                // Si la souris peut redimensionner la forme verticalement
                else if (selector.canResizeV(mouse))
                    canvas.setCursor(Cursor.V_RESIZE);

                // Si la souris peut déplacer la forme
                else if (selector.canMove(mouse))
                    canvas.setCursor(Cursor.MOVE);

                // Si la souris est sur une forme
                else if (model.isShapeSelected())
                    canvas.setCursor(Cursor.DEFAULT);

                else
                    canvas.setCursor(Cursor.HAND);
            }

            else
                canvas.setCursor(Cursor.DEFAULT);

        });

        // Lorque la souris est pressée
        canvas.setOnMousePressed(e -> {
            // Réinitialiser les coordonnées
            posStart = Coord.getCoordMouse(e, model.getSize());
            posCurrent = posStart;

            // Débuter le dessin
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());

            // Mettre à jour l'état de l'application
            model.updateApp();
            model.setSelectedTools(selectedTools);

            // Sauvegarder l'image précédente
            previousImage = canvas.snapshot(null, null);

            // Outils de forme
            if (selectedTools.isTool(ToolsTypes.SELECT))
                model.shapeSelect(posStart);

            // Outils gomme
            else if (selectedTools.isTool(ToolsTypes.ERASER)) {
                model.shapeUnselect();
                model.shapeRemove(posStart);
            }

            // Outils de forme
            else if (selectedTools.isTool(ToolsTypes.SHAPE)) {
                model.shapeUnselect();
                selectedShape.setColor(model.getColor());
                selectedShape.setBdSize(model.getSize());
                selectedShape.setPStart(posStart);
            }
        });

        // Lorque la souris est déplacée
        canvas.setOnMouseDragged(e -> {
            // Récupère les coordonnées de la souris
            Coord mouse = Coord.getCoordMouse(e, model.getSize());
            posCurrent = mouse;

            // Outils de forme
            if (selectedTools.isTool(ToolsTypes.SHAPE))
                ((ToolsShape) selectedTools).draw(canvas, previousImage, posCurrent, selectedShape);

            // Outils de sélection
            else if (selectedTools.isTool(ToolsTypes.SELECT))
                model.onShapeDrag(posStart, posCurrent);

        });

        // Lorque la souris est relâchée
        canvas.setOnMouseReleased(e -> {
            // Récupère les coordonnées de la souris
            Coord mouse = Coord.getCoordMouse(e, model.getSize());
            posCurrent = mouse;

            // Outils de forme
            if (selectedTools.isTool(ToolsTypes.SHAPE)) {
                ((ToolsShape) selectedTools).draw(canvas, previousImage, posCurrent, selectedShape);
                selectedShape.finish();
                model.shapeAdd(selectedShape.copy());
                model.redraw();
            }

            // Outils de sélection
            else if (selectedTools.isTool(ToolsTypes.SELECT))
                if (model.isShapeSelected()) {
                    Coord[] coords = model.getSelectedShape().getArea();
                    ((ToolsSelector) selectedTools).setSelection(coords[0], coords[1]);
                }
        });
    }

    @FXML
    private void cancelStyle() {
        /**
         * Réinitialise le style des boutons.
         */
        String defaultStyle = "-fx-background-color: transparent";
        btnEraser.setStyle(defaultStyle);
        btnShape.setStyle(defaultStyle);
        btnText.setStyle(defaultStyle);
        btnSelection.setStyle(defaultStyle);
        btnLine.setStyle(defaultStyle);
    }

}
