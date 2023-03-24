import Model.Coord;
import Model.Model;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

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
  private MenuItem btnNew, btnOpen, btnSave, btnClose;

  @FXML
  private MenuButton btnSize, btnShape;

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
    cancelStyle();
    appColor = cPicker.getValue();

    lblLog.setText("Pinceau séléctionné");
    btnPen.setStyle(selectedStyle);
  }

  @FXML
  void selectEraser(ActionEvent event) {
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
  public void initialize() {
    GraphicsContext g = canvas.getGraphicsContext2D();

    canvas.setOnMousePressed(e -> {
      Coord mouse = Coord.getCoordMouse(e, appSize);

      g.setStroke(appColor);
      g.setLineWidth(appSize);
      g.beginPath();
      g.moveTo(mouse.x, mouse.y);
    });

    canvas.setOnMouseDragged(e -> {
      Coord mouse = Coord.getCoordMouse(e, appSize);

      g.setStroke(appColor);
      g.setLineWidth(appSize);
      g.lineTo(mouse.x, mouse.y);
      g.stroke();
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
