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
  public IShape shapeSelected = new ShapePen(
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
  private Button btnEraser, btnPen, upSize, downSize, btnSelect;

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
      lblLog.setText("Taille modifiée");
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
      lblLog.setText("Taille augtmentée");
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
      lblLog.setText("Taille diminuée");
    }
  }

  @FXML
  void selectPen(ActionEvent event) {
    /**
     * Sélectionner le pinceau.
     */
    shapeSelected =
      new ShapePen(posStart, model.getColor(), model.getToolSize());
    cancelStyle();
    model.setColor(cPicker);

    lblLog.setText("Pinceau séléctionné");
    btnPen.setStyle(selectedStyle);
  }

  @FXML
  void selectEraser(ActionEvent event) {
    /**
     * Sélectionner la gomme.
     */
    shapeSelected = new ShapeEraser(model.getToolSize());
    cancelStyle();

    model.setColor(Color.WHITE);
    lblLog.setText("Gomme séléctionnée");
    btnEraser.setStyle(selectedStyle);
  }

  @FXML
  void selectShape(ActionEvent event) {
    /**
     * Sélectionner une forme.
     */
    cancelStyle();
    btnShape.setStyle(selectedStyle);
    lblLog.setText("Forme séléctionnée");
    btnShape.setStyle(selectedStyle);
  }

  @FXML
  void selectLine(ActionEvent event) {
    /**
     * Sélectionner la forme ligne.
     */
    cancelStyle();
    model.setColor(cPicker);
    lblLog.setText("Ligne sélectionnée");
    btnLine.setStyle(selectedStyle);
    shapeSelected = new ShapeLine(posStart, model.getToolSize());
    btnShape.setStyle(selectedStyle);
  }

  @FXML
  void selectRectangle(ActionEvent event) {
    /**
     * Sélectionner la forme rectangle.
     */
    cancelStyle();
    model.setColor(cPicker);

    lblLog.setText("Rectangle sélectionné");
    btnRect.setStyle(selectedStyle);
    btnShape.setStyle(selectedStyle);
    // shape = new Rectangle();
  }

  @FXML
  void selectCircle(ActionEvent event) {
    /**
     * Sélectionner la forme cercle.
     */
    cancelStyle();
    btnShape.setStyle(selectedStyle);
    model.setColor(cPicker);
    lblLog.setText("Cercle sélectionné");
    btnCircle.setStyle(selectedStyle);
    // shape = new Circle();
  }

  @FXML
  void selectTriangle(ActionEvent event) {
    /**
     * Sélectionner la forme triangle.
     */
    cancelStyle();
    model.setColor(cPicker);
    lblLog.setText("Cercle sélectionné");
    btnTriangle.setStyle(selectedStyle);
    btnShape.setStyle(selectedStyle);
    // shape = new Polygon();
  }

  @FXML
  void selectObject(ActionEvent event) {
    cancelStyle();
    shapeSelected = new ShapeSelector(model);
    lblLog.setText("Outils de sélections");
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
  public void initialize() {
    GraphicsContext gc = canvas.getGraphicsContext2D();
    model.setGraphicsContext(gc);
    model.setCanvas(canvas);

    canvas.setOnMousePressed(e -> {
      // Récupère les coordonnées de la souris
      posStart = Coord.getCoordMouse(e, model.getToolSize());
      posCurrent = posStart;

      gc.setStroke(model.getColor());
      gc.setLineWidth(model.getToolSize());
      gc.beginPath();
      gc.moveTo(e.getX(), e.getY());

      shapeSelected.initializeCoord(posStart);

      // Sauvegarder l'image précédente
      previousImage = canvas.snapshot(null, null);

      if (shapeSelected.isShape(ShapeTypes.SELECT)) {
        model.setSelector((ShapeSelector) shapeSelected);
        model.printSelectedShape(posStart);
      } else model.setSelector(null);
    });

    canvas.setOnMouseDragged(e -> {
      // Récupère les coordonnées de la souris
      Coord mouse = Coord.getCoordMouse(e, model.getToolSize());
      posCurrent = mouse;

      if (shapeSelected.isShape(ShapeTypes.LINE)) {
        //  Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        model.drawLine(posStart, posCurrent);
      } else if (shapeSelected.isShape(ShapeTypes.RECTANGLE)) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        // Dessiner le rectangle
        model.drawRectangle(gc, posStart, posCurrent);
      } else if (shapeSelected.isShape(ShapeTypes.CIRCLE)) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        // Dessiner le cercle
        model.drawCircle(gc, posStart, posCurrent);
      } else if (shapeSelected.isShape(ShapeTypes.TRIANGLE)) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        posOpposite.x = posCurrent.x - posCurrent.getDistance(posStart);
        posOpposite.y = posCurrent.y;

        model.drawTriangle(gc, posStart, posCurrent, posOpposite);
      } else if (shapeSelected.isShape(ShapeTypes.PEN)) {
        model.drawPen(gc, posStart, posCurrent);

        posStart = posCurrent;
        shapeSelected.addCoord(posCurrent);

        // Enregistrer la nouvelle image
        previousImage = canvas.snapshot(null, null);
      } else if (shapeSelected.isShape(ShapeTypes.SELECT)) {
        model.moveShape(posCurrent);
      }
    });

    canvas.setOnMouseReleased(e -> {
      // Récupère les coordonnées de la souris
      Coord mouse = Coord.getCoordMouse(e, model.getToolSize());
      posCurrent = mouse;

      if (shapeSelected.isShape(ShapeTypes.LINE)) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        // Dessiner la ligne
        model.drawLine(posStart, posCurrent);
        shapeSelected.addCoord(posCurrent);
        // Definie les coordonnées de la ligne
      } else if (shapeSelected.isShape(ShapeTypes.RECTANGLE)) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        // Dessiner le rectangle
        model.drawRectangle(gc, posStart, posCurrent);
      } else if (shapeSelected.isShape(ShapeTypes.CIRCLE)) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        // Dessiner le cercle
        model.drawCircle(gc, posStart, posCurrent);
      } else if (shapeSelected.isShape(ShapeTypes.TRIANGLE)) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        // Dessiner le triangle
        model.drawTriangle(gc, posStart, posCurrent, posOpposite);
      } else {
        shapeSelected.addCoord(posCurrent);
      }

      model.addShape(shapeSelected.copy());
    });
  }
}
