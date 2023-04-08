import Model.*;
import java.io.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javax.imageio.ImageIO;

public class Controller {

  public Model model = new Model();

  // Déclare les attricuts de l'états de l'application
  private String selectedStyle = "-fx-background-color: #D3E8EE";

  public Coord posStart = new Coord();
  public Coord posCurrent = new Coord();
  public Coord posOpposite = new Coord();
  public IShape shapeToolsSelected;

  public ITools toolSelected = new ToolsPen(
    model.getColor(),
    model.getToolSize()
  );

  private Image previousImage;

  // Déclare les attributs de la vue
  @FXML
  private Canvas canvas;

  @FXML
  private Label lblLog;

  @FXML
  private TextField fileTitle;

  @FXML
  private MenuItem btnNew, btnOpen, btnSave, btnClose;

  @FXML
  private MenuButton btnSize, btnShape;

  @FXML
  private CustomMenuItem btnLine, btnRect, btnCircle, btnTriangle;

  @FXML
  private TextField inputSize;

  @FXML
  private Button btnEraser, btnPen, upSize, downSize, btnSelect, btnFront, btnBack;

  @FXML
  private ToggleGroup groupSize;

  @FXML
  private ColorPicker cPicker;

  @FXML
  void onNew(ActionEvent event) {
    /**
     * Nouveau fichier
     */
    lblLog.setText("Nouveau fichier");
  }

  @FXML
  void onOpen(ActionEvent event) {
    /**
     * Ouvrir un fichier
     */
    lblLog.setText("Ouverture d'un fichier");
  }

  @FXML
  void onSave(ActionEvent event) {
    /**
     * Enregistrer l'image du canvas dans un fichier PNG.
     */

    // Récupérer la racine de la scène
    Node node = canvas;
    Stage stage = (Stage) node.getScene().getWindow();

    // Capturer l'image du canvas
    WritableImage image = new WritableImage(
      (int) canvas.getWidth(),
      (int) canvas.getHeight()
    );
    node.snapshot(null, image);

    // Créer un objet File pour enregistrer l'image en PNG
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Enregistrer en tant qu'image PNG");
    fileChooser
      .getExtensionFilters()
      .add(new FileChooser.ExtensionFilter("Fichiers PNG", "*.png"));
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
  void onExit(ActionEvent event) {
    /**
     * Fermer l'application.
     */
    // TODO: Afficher une boîte de dialogue pour confirmer la fermeture de l'application
    lblLog.setText("Fermeture de l'application");
    Platform.exit();
  }

  @FXML
  void selectColor(ActionEvent event) {
    /**
     * Sélectionner une couleur.
     */
    model.setColor(cPicker);
    lblLog.setText("Couleur modifiée");
  }

  @FXML
  public void selectSize(ActionEvent event) {
    /**
     * Sélectionner la taille de l'outils.
     */
    RadioButton selectedSize = (RadioButton) groupSize.getSelectedToggle();
    String strSize = selectedSize.getText();

    model.setToolSize(strSize);
    btnSize.setText(selectedSize.getText());
  }

  public void listenSize() {
    String text = inputSize.getText();
    try {
      int newSize = Integer.parseInt(text);
      if (newSize < 1 || newSize > 20) {
        throw new NumberFormatException();
      }

      model.setToolSize(newSize);
      lblLog.setText("La taille de l'outil a été modifiée.");
      inputSize.setText(model.getToolSizeStr());
    } catch (NumberFormatException ex) {
      inputSize.setText(model.getToolSizeStr());
      lblLog.setText("Veuillez entrer un nombre valide.");
    }
  }

  @FXML
  public void onPressIncreaseToolSize(ActionEvent event) {
    /**
     * Augmenter la taille de l'outil.
     */
    if (model.getToolSize() != 20) {
      model.increaseToolSize();
      inputSize.setText(Integer.toString(model.getToolSize()));
      lblLog.setText("La taille de l'outil a été augmentée.");
    }
  }

  @FXML
  public void onPressDecreaseToolSize(ActionEvent event) {
    /**
     * Diminuer la taille de l'outil.
     */
    if (model.getToolSize() != 1) {
      model.decreaseToolSize();
      inputSize.setText(model.getToolSizeStr());
      lblLog.setText("La taille de l'outil a été diminuée.");
    }
  }

  @FXML
  void selectPen(ActionEvent event) {
    /**
     * Sélectionner le pinceau.
     */
    toolSelected =
      new ToolsPen(posStart, model.getColor(), model.getToolSize());
    cancelStyle();
    model.setColor(cPicker);

    lblLog.setText("Pinceau prêt à être utilisé.");
    btnPen.setStyle(selectedStyle);
    model.unselectShape();
  }

  @FXML
  void selectEraser(ActionEvent event) {
    /**
     * Sélectionner la gomme.
     */
    toolSelected = new ToolsEraser();
    cancelStyle();

    model.setColor(Color.WHITE);
    lblLog.setText("Cliquer sur une forme pour la supprimer.");
    btnEraser.setStyle(selectedStyle);
    model.unselectShape();
  }

  @FXML
  void selectShape(ActionEvent event) {
    /**
     * Sélectionner une forme.
     */
    cancelStyle();
    btnShape.setStyle(selectedStyle);
    lblLog.setText("Choisir une forme à dessiner.");
    btnShape.setStyle(selectedStyle);
    model.unselectShape();
  }

  @FXML
  void selectLine(ActionEvent event) {
    /**
     * Sélectionner la forme ligne.
     */
    cancelStyle();
    model.setColor(cPicker);
    lblLog.setText("Cliquer et déplacer pour dessiner une ligne.");
    btnLine.setStyle(selectedStyle);
    btnShape.setStyle(selectedStyle);
    shapeToolsSelected =
      new ShapeLine(posStart, model.getToolSize(), model.getColor());
    toolSelected = new ToolsShape();
    model.unselectShape();
  }

  @FXML
  void selectRectangle(ActionEvent event) {
    /**
     * Sélectionner la forme rectangle.
     */
    cancelStyle();
    model.setColor(cPicker);

    lblLog.setText("Cliquer et déplacer pour dessiner un rectangle.");
    btnRect.setStyle(selectedStyle);
    btnShape.setStyle(selectedStyle);
    shapeToolsSelected = new ShapeRect(posStart, model.getToolSize());
    toolSelected = new ToolsShape();
    model.unselectShape();
  }

  @FXML
  void selectCircle(ActionEvent event) {
    /**
     * Sélectionner la forme cercle.
     */
    cancelStyle();
    btnShape.setStyle(selectedStyle);
    model.setColor(cPicker);
    lblLog.setText("Cliquer et déplacer pour dessiner un cercle.");
    btnCircle.setStyle(selectedStyle);
    shapeToolsSelected = new ShapeCircle(posStart, model.getToolSize());
    toolSelected = new ToolsShape();
    model.unselectShape();
  }

  @FXML
  void selectTriangle(ActionEvent event) {
    /**
     * Sélectionner la forme triangle.
     */
    cancelStyle();
    model.setColor(cPicker);
    lblLog.setText("Cliquer et déplacer pour dessiner un triangle.");
    btnTriangle.setStyle(selectedStyle);
    btnShape.setStyle(selectedStyle);
    shapeToolsSelected = new ShapeTriangle(posStart, model.getToolSize());
    toolSelected = new ToolsShape();
    model.unselectShape();
  }

  @FXML
  void selectObject(ActionEvent event) {
    cancelStyle();
    toolSelected = new ToolsSelector();
    lblLog.setText("Cliquer sur une forme pour la sélectionner.");
    btnSelect.setStyle(selectedStyle);
  }

  @FXML
  public void cancelStyle() {
    String defaultStyle = "-fx-background-color: transparent";
    btnEraser.setStyle(defaultStyle);
    btnPen.setStyle(defaultStyle);
    btnShape.setStyle(defaultStyle);
    btnShape.setStyle(defaultStyle);
    btnSelect.setStyle(defaultStyle);
  }

  @FXML
  public void onPressToFront() {
    model.toFront();
  }

  @FXML
  public void onPressToBack() {
    model.toBack();
  }

  @FXML
  public void initialize() {
    GraphicsContext gc = canvas.getGraphicsContext2D();
    model.setGraphicsContext(gc);
    model.setCanvas(canvas);
    model.setBtns(btnBack, btnFront);
    model.setCPicker(cPicker);
    model.setInputSize(inputSize);

    canvas.setOnMousePressed(e -> {
      model.updateAppState();

      // Récupère les coordonnées de la souris
      posStart = Coord.getCoordMouse(e, model.getToolSize());
      posCurrent = posStart;

      gc.beginPath();
      gc.moveTo(e.getX(), e.getY());

      shapeToolsSelected.initializeCoord(posStart);
      shapeToolsSelected.setToolColor(model.getColor());
      shapeToolsSelected.setToolSize(model.getToolSize());
      model.unselectShape();

      // Sauvegarder l'image précédente
      previousImage = canvas.snapshot(null, null);

      if (toolSelected.isTool(ToolsTypes.SELECT)) {
        model.selectShape(posStart);
      } else if (toolSelected.isTool(ToolsTypes.ERASER)) {
        model.removeShape(posStart);
      }
    });

    canvas.setOnMouseDragged(e -> {
      // Récupère les coordonnées de la souris
      Coord mouse = Coord.getCoordMouse(e, model.getToolSize());
      posCurrent = mouse;

      if (toolSelected.isTool(ToolsTypes.PEN)) {
        // TODO: Implémenter le pinceau
      } else if (toolSelected.isTool(ToolsTypes.SHAPE)) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);
        shapeToolsSelected.setEndCoord(posCurrent);
        shapeToolsSelected.draw(gc);
      } else if (toolSelected.isTool(ToolsTypes.SELECT)) {
        model.moveShape(posCurrent);
      }
    });

    canvas.setOnMouseReleased(e -> {
      // Récupère les coordonnées de la souris
      Coord mouse = Coord.getCoordMouse(e, model.getToolSize());
      posCurrent = mouse;

      if (toolSelected.isTool(ToolsTypes.SHAPE)) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        shapeToolsSelected.setEndCoord(posCurrent);
        shapeToolsSelected.draw(gc);
        shapeToolsSelected.addCoord(posCurrent);

        model.addShape(shapeToolsSelected.copy());
      }
    });
  }
}
