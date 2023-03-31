import Model.Coord;
import Model.Model;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.image.WritableImage;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;



public class Controller {

  private Model model = new Model();
  private Color appColor = Color.BLACK;
  private int appSize = 2;
  private String selectedStyle = "-fx-background-color: #D3E8EE";

  // Récupère les outils de la fenêtre.
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
  private Button btnEraser, btnPen;

  @FXML
  private ToggleGroup groupSize;

  @FXML
  private ColorPicker cPicker;

  private Shape shape;

  @FXML
  void onNew(ActionEvent event) {
    lblLog.setText("Nouveau fichier");
  }

  @FXML
  void onOpen(ActionEvent event) {
    lblLog.setText("Ouverture d'un fichier");
  }

  @FXML
  void onSave(ActionEvent event) {
    // Récupérer la racine de la scène
    Node node = canvas;
    Stage stage = (Stage) node.getScene().getWindow();

    // Capturer l'image du canvas
    WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
    node.snapshot(null, image);

    // Créer un objet File pour enregistrer l'image en PNG
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Enregistrer en tant qu'image PNG");
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
    lblLog.setText("Fermeture de l'application");
    Platform.exit();
  }

  @FXML
  void selectColor(ActionEvent event) {
    appColor = cPicker.getValue();
    lblLog.setText("Couleur modifiée");
  }

  @FXML
  void selectSize(ActionEvent event) {
    RadioButton selectedSize = (RadioButton) groupSize.getSelectedToggle();
    String strSize = selectedSize.getText();

    appSize = Integer.parseInt(strSize.replace("px", ""));
    btnSize.setText(selectedSize.getText());

    lblLog.setText("Taille modifiée");
  }

  @FXML
  void selectPen(ActionEvent event) {
    shape = null;
    cancelStyle();
    appColor = cPicker.getValue();

    lblLog.setText("Pinceau séléctionné");
    btnPen.setStyle(selectedStyle);
  }

  @FXML
  void selectEraser(ActionEvent event) {
    shape = null;
    cancelStyle();

    appColor = Color.WHITE;
    lblLog.setText("Gomme séléctionnée");
    btnEraser.setStyle(selectedStyle);
  }

  @FXML
  void selectShape(ActionEvent event) {
    cancelStyle();
    btnShape.setStyle(selectedStyle);
    lblLog.setText("Forme séléctionnée");
  }

  @FXML
  void selectLine(ActionEvent event) {
    cancelStyle();
    appColor = cPicker.getValue();
    lblLog.setText("Ligne sélectionnée");
    btnLine.setStyle(selectedStyle);
    shape = new javafx.scene.shape.Line();
  }

  @FXML
  void selectRectangle(ActionEvent event) {
    cancelStyle();
    appColor = cPicker.getValue();
    lblLog.setText("Rectangle sélectionné");
    btnLine.setStyle(selectedStyle);
    shape = new javafx.scene.shape.Rectangle();
  }

  // Méthode pour dessiner une ligne
  private void drawLine(GraphicsContext gc, Coord start, Coord end) {
    gc.setStroke(appColor);
    gc.setLineWidth(appSize);
    gc.strokeLine(start.x, start.y, end.x, end.y);
  }

  // Méthode pour dessiner un rectangle
  private void drawRectangle(GraphicsContext gc, Coord start, Coord end) {
    gc.setStroke(appColor);
    gc.setLineWidth(appSize);

    double width = Math.abs(start.x - end.x);
    double height = Math.abs(start.y - end.y);
    double x = Math.min(start.x, end.x);
    double y = Math.min(start.y, end.y);

    gc.strokeRect(x, y, width, height);
  }


  public Coord startpos = Coord.createCoord(0,0);
  public Coord currentpos = Coord.createCoord(0,0);
  private Image previousImage;

  @FXML
  public void initialize() {
    GraphicsContext g = canvas.getGraphicsContext2D();

    canvas.setOnMousePressed(e -> {
        startpos = Coord.getCoordMouse(e, appSize);
        currentpos = startpos;

        g.setStroke(appColor);
        g.setLineWidth(appSize);
        g.beginPath();
        g.moveTo(startpos.x, startpos.y);

        previousImage = canvas.snapshot(null, null); // enregistrer l'image actuelle
    });

    canvas.setOnMouseDragged(e -> {
      Coord mouse = Coord.getCoordMouse(e, appSize);
      currentpos = mouse;
  
      if (shape != null && shape instanceof javafx.scene.shape.Line) {
          g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Effacer le contenu du canvas
          g.drawImage(previousImage, 0, 0); // Restaurer l'image précédente
          drawLine(g, startpos, currentpos);
          ((javafx.scene.shape.Line) shape).setEndX(currentpos.x);
          ((javafx.scene.shape.Line) shape).setEndY(currentpos.y);
      } else if (shape != null && shape instanceof javafx.scene.shape.Rectangle) {
          g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Effacer le contenu du canvas
          g.drawImage(previousImage, 0, 0); // Restaurer l'image précédente
          drawRectangle(g, startpos, currentpos);
          //((javafx.scene.shape.Line) shape).setEndX(currentpos.x);
          //((javafx.scene.shape.Line) shape).setEndY(currentpos.y);
      } else {
          g.setStroke(appColor);
          g.setLineWidth(appSize);
          g.lineTo(mouse.x, mouse.y);
          g.stroke();
          previousImage = canvas.snapshot(null, null); // enregistrer la nouvelle image
      }
    });

    canvas.setOnMouseReleased(e -> {
        Coord mouse = Coord.getCoordMouse(e, appSize);
        currentpos = mouse;

        if (shape != null && shape instanceof javafx.scene.shape.Line) {
            g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Effacer le contenu du canvas
            g.drawImage(previousImage, 0, 0); // Restaurer l'image précédente

            drawLine(g, startpos, currentpos);
            ((javafx.scene.shape.Line) shape).setEndX(currentpos.x);
            ((javafx.scene.shape.Line) shape).setEndY(currentpos.y);
        } else if (shape != null && shape instanceof javafx.scene.shape.Rectangle){
            g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Effacer le contenu du canvas
            g.drawImage(previousImage, 0, 0); // Restaurer l'image précédente

            drawRectangle(g, startpos, currentpos);
            //((javafx.scene.shape.Line) shape).setEndX(currentpos.x);
            //((javafx.scene.shape.Line) shape).setEndY(currentpos.y);
        } else {
            g.setStroke(appColor);
            g.setLineWidth(appSize);
            g.lineTo(currentpos.x, currentpos.y);
            g.stroke();
        }
    });
  }

  @FXML
  public void cancelStyle() {
    String defaultStyle = "-fx-background-color: transparent";
    btnEraser.setStyle(defaultStyle);
    btnPen.setStyle(defaultStyle);
    btnShape.setStyle(defaultStyle);
  }
}
