package sample;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


public class Controller {



    private boolean initEvent = false;

//    AnimationTimer GravityAnimation;
//    static GravityView gravityView;
//
    @FXML
    private Pane PanePendulum;

    @FXML
    private Pane PaneGravity;

    @FXML
    private Pane ContentPane = new Pane(); //nullpointer exception after process starts(appears once)

    @FXML
    private Tab TabGravity;

    @FXML
    private Tab TabPendulum;

    @FXML
    private GravityGuiController gravityController;

    @FXML
    private PendulumGuiController pendulumController;
//
    @FXML
    public void initialize()
    {
        if (PanePendulum != null) {pendulumController.initialize(PanePendulum);  System.out.println("Initialized");}
        else {System.out.println("Not initialized");}

        PanePendulum.widthProperty().addListener((obs, oldVal, newVal) -> {
            pendulumController.setPaneSize(PanePendulum.getWidth(),PanePendulum.getHeight(), PDS.xreal, PDS.yreal);
        });
        PanePendulum.heightProperty().addListener((obs, oldVal, newVal) -> {
            pendulumController.setPaneSize(PanePendulum.getWidth(),PanePendulum.getHeight(), PDS.xreal, PDS.yreal);

        });

        if (PaneGravity != null) {gravityController.initialize(PaneGravity);  System.out.println("Initialized Gravity");}

        assert PaneGravity != null;
        PaneGravity.widthProperty().addListener((obs, oldVal, newVal) -> {
            gravityController.setPaneSize(newVal.doubleValue(),PaneGravity.getHeight(), GDS.xreal, GDS.yreal);
        });
        PaneGravity.heightProperty().addListener((obs, oldVal, newVal) -> {
            gravityController.setPaneSize(PaneGravity.getWidth(),newVal.doubleValue(), GDS.xreal, GDS.yreal);
        });
    }



    @FXML
    private void ChangeGui(Event event) throws Exception
    {
    /*    if (initEvent) {
            if (TabPendulum.isSelected()) {
                pendulumController.GUIPane.setVisible(true);
                pendulumController.GUIPane.setManaged(true);
                gravityController.setVisible(false);
				gravityController.stopAnimation();
                pendulumController.setVisible(true);
                pendulumController.startAnimation();
            } else if (TabGravity.isSelected()) {
                pendulumController.GUIPane.setVisible(false);
                pendulumController.GUIPane.setManaged(false);
                pendulumController.setVisible(false);
                gravityController.setVisible(true);
				pendulumController.stopAnimation();
                gravityController.startAnimation();
            }
        }
        else initEvent = true;
    }*/
        if (initEvent) {
            if (TabPendulum.isSelected()) {
                FXMLLoader pendulumLoader = new FXMLLoader(getClass().getResource("pendulumGui.fxml"));
                ContentPane.getChildren().clear();
                ContentPane.getChildren().add(pendulumLoader.load());
                pendulumController.startAnimation();
            } else if (TabGravity.isSelected()) {
                FXMLLoader gravityLoader = new FXMLLoader(getClass().getResource("gravityGui.fxml"));
                ContentPane.getChildren().clear();
                ContentPane.getChildren().add(gravityLoader.load());
                gravityController.gravityView = new GravityView(GDS.num_of_bodies);
                for (Node i : gravityController.gravityView.getNodes())
                    PaneGravity.getChildren().add(i);

                gravityController.startAnimation();
            }
        }
        else initEvent = true;
    }
//
//
    @FXML
    private MenuItem langSelectEN;

    @FXML
    private MenuItem langSelectPL;

    @FXML
    private void changeLanguageEN(Event event) throws Exception{
        Locale currLocale = new Locale("en");
        changeLocale(currLocale);
    }

    @FXML
    private void changeLanguagePL(Event event) throws Exception{
        Locale currLocale = new Locale("pl");
        changeLocale(currLocale);
    }

    private void changeLocale(Locale locale) throws Exception{
        Main.fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Main.fxmlLoader.setResources(ResourceBundle.getBundle("sample.Locale", locale));

        GridPane newNode = Main.fxmlLoader.load();
        Main.controller = Main.fxmlLoader.getController();
        Main.primaryStage.setTitle(Main.fxmlLoader.getResources().getString("window_title"));
        Main.root.getChildren().setAll(newNode.getChildren());
    }


}

