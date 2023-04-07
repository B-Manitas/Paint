import Model.Coord;
import Model.Model;
import Model.ShapePen;
import java.io.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
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
import javafx.scene.shape.*;
import javafx.stage.*;
import javax.imageio.ImageIO;

public class Controller {

  private Model model = new Model();

  // Déclare les attricuts de l'états de l'application
  private String selectedStyle = "-fx-background-color: #D3E8EE";

  private Shape shape;

  public Coord posStart = Coord.createCoord(0, 0);
  public Coord posCurrent = Coord.createCoord(0, 0);
  public Coord posOpposite = Coord.createCoord(0, 0);
  public ShapePen shapePen;
  public ArrayList<Shape> Shapes = new ArrayList<Shape>();
  private Image previousImage;
  public static boolean halfCtrlSPressed=false;
  public static boolean halfCtrlNPressed=false;
  public static boolean halfCtrlOPressed=false;
  public static boolean halfCtrlWPressed=false;

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
  private Button btnEraser, btnPen, upSize, downSize, btnHelp, btnCloseHelp;

  @FXML
  private ToggleGroup groupSize;

  @FXML
  private ColorPicker cPicker;

  @FXML
  private DialogPane helpPane;

  @FXML
  public void handle(KeyEvent ke) {
    /**
     * Raccourcis clavier
     */
    // Ctrl + S
    if (ke.getCode() == KeyCode.CONTROL) {halfCtrlSPressed = true;} 
    else if (ke.getCode() == KeyCode.S && halfCtrlSPressed) {halfCtrlSPressed = false; onSave(null);}
    else {halfCtrlSPressed = false;}
    // Ctrl + N
    if (ke.getCode() == KeyCode.CONTROL) {halfCtrlNPressed = true;}
    else if (ke.getCode() == KeyCode.N && halfCtrlNPressed) {halfCtrlNPressed = false; onNew(null);}
    else {halfCtrlNPressed = false;}
    // Ctrl + O
    if (ke.getCode() == KeyCode.CONTROL) {halfCtrlOPressed = true;}
    else if (ke.getCode() == KeyCode.O && halfCtrlOPressed) {halfCtrlOPressed = false; onOpen(null);}
    else {halfCtrlOPressed = false;}
    //Ctrl + W
    if (ke.getCode() == KeyCode.CONTROL) {halfCtrlWPressed = true;}
    else if (ke.getCode() == KeyCode.W && halfCtrlWPressed) {halfCtrlWPressed = false; onExit(null);}
    else {halfCtrlWPressed = false;}
}

  @FXML
  void setfileTitle(ActionEvent event) {
    model.setfileTitle(fileTitle.getText());
  }

  @FXML
  void onNew(ActionEvent event) {
    /**
     * Nouveau fichier
     */
    // Vérifier si le canvas n'est pas vide, et propose de sauvegarder
    if (!model.isCanvasEmpty(canvas)) {
      boolean result = model.showSaveAlert();
      if (result==true){onSave(event);}
    }
    // Vider le canvas
    model.clearCanvas(canvas);
    // Réinitialiser les attributs
    model.resetAttributes();
    cancelStyle();
    btnPen.setStyle(selectedStyle);
    lblLog.setText("Nouveau fichier");
  }

  @FXML
  void onOpen(ActionEvent event) {
    /**
     * Ouvrir un fichier
     */
    // Vérifier si le canvas n'est pas vide, et propose de sauvegarder
    if (!model.isCanvasEmpty(canvas)) {
      boolean result = model.showSaveAlert();
      if (result){onSave(event);}
    }
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Ouvrir un fichier PNG");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PNG", "*.png"));
    File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());

    if (file != null) {
        // Charger l'image depuis le fichier
        Image image = new Image(file.toURI().toString());
        model.setfileTitle(file.getName().replace(".png", ""));
        fileTitle.setText(model.getfileTitle());

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
        btnPen.setStyle(selectedStyle);
    }
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
  void onExit(ActionEvent event) {
    /**
     * Fermer l'application.
     */
    // Vérifier si le canvas n'est pas vide, et propose de sauvegarder
    if (!model.isCanvasEmpty(canvas)) {
      boolean result = model.showSaveAlert();
      if (result==true){onSave(event);}
    }
    lblLog.setText("Fermeture de l'application");
    Platform.exit();
  }

  @FXML 
  void onHelp(ActionEvent event) {
    /**
     * Afficher l'aide.
     */
    if (helpPane.isVisible() == true) {helpPane.setVisible(false);}
    else {helpPane.setVisible(true);}
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
  
  @FXML
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
    shape = null;
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
    shape = null;
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
    shape = new Line();
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
    shape = new Rectangle();
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
    shape = new Circle();
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
    shape = new Polygon();
  }

  @FXML
  public void cancelStyle() {
    String defaultStyle = "-fx-background-color: transparent";
    btnEraser.setStyle(defaultStyle);
    btnPen.setStyle(defaultStyle);
    btnShape.setStyle(defaultStyle);
    btnShape.setStyle(defaultStyle);
  }

  @FXML
  public void initialize() {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    canvas.setOnMousePressed(e -> {
      // Récupère les coordonnées de la souris
      posStart = Coord.getCoordMouse(e, model.getToolSize());
      posCurrent = posStart;

      // Créer une nouvelle forme pinceau
      shapePen = new ShapePen(posStart, model.getColor(), model.getToolSize());

      gc.setStroke(model.getColor());
      gc.setLineWidth(model.getToolSize());
      gc.beginPath();
      gc.moveTo(e.getX(), e.getY());

      // Sauvegarder l'image précédente
      previousImage = canvas.snapshot(null, null);
    });

    canvas.setOnMouseDragged(e -> {
      // Récupère les coordonnées de la souris
      Coord mouse = Coord.getCoordMouse(e, model.getToolSize());
      posCurrent = mouse;

      if (shape instanceof Line) {
        //  Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0); // Restaurer l'image précédente

        model.drawLine(gc, posStart, posCurrent);

        ((Line) shape).setEndX(posCurrent.x);
        ((Line) shape).setEndY(posCurrent.y);
      } else if (shape instanceof Rectangle) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        // Dessiner le rectangle
        model.drawRectangle(gc, posStart, posCurrent);
      } else if (shape instanceof Circle) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        // Dessiner le cercle
        model.drawCircle(gc, posStart, posCurrent);
      } else if (shape instanceof Polygon) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        posOpposite.x = posCurrent.x - posCurrent.getDistance(posStart);
        posOpposite.y = posCurrent.y;

        model.drawTriangle(gc, posStart, posCurrent, posOpposite);
      } else {
        double centerX = (posStart.x + posCurrent.x) / 2.0;
        double centerY = (posStart.y + posCurrent.y) / 2.0;

        gc.quadraticCurveTo(posStart.x, posStart.y, centerX, centerY);
        gc.stroke();
        gc.beginPath();
        gc.moveTo(centerX, centerY);

        posStart = posCurrent;
        shapePen.addCoord(posCurrent);
        previousImage = canvas.snapshot(null, null); // enregistrer la nouvelle image
      }
    });

    canvas.setOnMouseReleased(e -> {
      // Récupère les coordonnées de la souris
      Coord mouse = Coord.getCoordMouse(e, model.getToolSize());
      posCurrent = mouse;

      if (shape instanceof Line) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        // Dessiner la ligne
        model.drawLine(gc, posStart, posCurrent);

        // Definie les coordonnées de la ligne
        ((Line) shape).setEndX(posCurrent.x);
        ((Line) shape).setEndY(posCurrent.y);
      } else if (shape instanceof Rectangle) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        // Dessiner le rectangle
        model.drawRectangle(gc, posStart, posCurrent);
      } else if (shape instanceof Circle) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        // Dessiner le cercle
        model.drawCircle(gc, posStart, posCurrent);
      } else if (shape instanceof Circle) {
        // Restaurer l'image précédente
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(previousImage, 0, 0);

        // Dessiner le triangle
        model.drawTriangle(gc, posStart, posCurrent, posOpposite);
      } else {
        shapePen.addCoord(posCurrent);
        Shapes.add(shapePen);
      }
    });
  }
}
