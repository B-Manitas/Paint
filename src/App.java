import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("View/Home.fxml")));
        // scene.getStylesheets().add(getClass().getResource("style.css").toString());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Drawing App");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
