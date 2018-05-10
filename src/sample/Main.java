package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Controller controller = new Controller();
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.setWidth(800.);
        primaryStage.setMinWidth(600.);
        primaryStage.setHeight(600.);
        primaryStage.setMinHeight(500.);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
