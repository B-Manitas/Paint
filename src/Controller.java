import java.security.Principal;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class Controller {

  @FXML
  private Canvas canvas;

  @FXML
  private MenuItem btnNew, btnOpen, btnSave, btnClose;
  
  @FXML
  private ToggleGroup penSize;
  
  @FXML
  private RadioButton radioPenSize1, radioPenSize2, radioPenSize3, radioPenSize4, radioPenSize5;

  private int valuePenSize = 2;

  @FXML
  void onNew(ActionEvent event) {
    System.out.println("New");
  }

  @FXML
  void onOpen(ActionEvent event) {
    System.out.println("Open");
  }

  @FXML
  void onSave(ActionEvent event) {
    System.out.println("Save");
  }

  @FXML
  void onExit(ActionEvent event) {
    System.out.println("Exit");
    Platform.exit();
  }

  @FXML
  void selectPen(ActionEvent event) {
    System.out.println("Pen");
    RadioButton selectedSize = (RadioButton) penSize.getSelectedToggle();
    String strSize = selectedSize.getText().replace("px", "");
    int size = Integer.parseInt(strSize);
    valuePenSize = size;
    System.out.println(size);
  }

  @FXML
  public void initialize() {
    GraphicsContext g = canvas.getGraphicsContext2D();

    canvas.setOnMouseDragged(e -> {
      double posX = e.getX() - valuePenSize / 2;
      double posY = e.getY() - valuePenSize / 2;

      g.fillRect(posX, posY, valuePenSize, valuePenSize);
    });
  }
}
