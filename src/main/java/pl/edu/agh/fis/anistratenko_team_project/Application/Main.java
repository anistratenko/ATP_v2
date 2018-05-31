package pl.edu.agh.fis.anistratenko_team_project.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    static Stage primaryStage;
    static FXMLLoader fxmlLoader;
    static Scene scene;
    static GridPane root;
    static Controller controller;


    @Override
    public void start(Stage primaryStage_arg) throws Exception{
        primaryStage = primaryStage_arg;

        Locale currLocale = new Locale("pl");

//        root = fxmlLoader.load();
        root = loadFXML(currLocale);
        controller = fxmlLoader.getController();
        scene = new Scene(root);


//        this.primaryStage.setTitle(fxmlLoader.getResources().getString("window_title"));


        primaryStage.setScene(scene);
        primaryStage.setWidth(800.);
        primaryStage.setMinWidth(600.);
        primaryStage.setHeight(600.);
        primaryStage.setMinHeight(500.);
        primaryStage.show();
    }


    public  GridPane loadFXML(Locale locale) {
        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/application.fxml"));

//        fxmlLoader.setLocation();
        fxmlLoader.setResources(ResourceBundle.getBundle("language.Locale", locale));

        GridPane node = null;

        try {
            node = fxmlLoader.load();

            controller = fxmlLoader.getController();
            primaryStage.setTitle(fxmlLoader.getResources().getString("window_title"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return node;
    }




    public static void main(String[] args) {
        launch(args);
    }
}
