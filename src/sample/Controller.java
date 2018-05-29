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

        if (PaneGravity != null) {gravityController.initialize(PaneGravity);  System.out.println("Initialized Gravity"); }

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
        if (initEvent) {
            if (TabPendulum.isSelected()) {
				gravityController.stopAnimation();
				gravityController.GUIPane.setVisible(false);
				gravityController.GUIPane.setManaged(false);
                pendulumController.GUIPane.setVisible(true);
                pendulumController.GUIPane.setManaged(true);


//                pendulumController.startAnimation();
            } else if (TabGravity.isSelected()) {
				pendulumController.stopAnimation();
                pendulumController.GUIPane.setVisible(false);
                pendulumController.GUIPane.setManaged(false);
                gravityController.GUIPane.setVisible(true);
                gravityController.GUIPane.setManaged(true);

//                gravityController.startAnimation();
            }
        }
        else initEvent = true;
    }
//        if (initEvent) {
//        	gravityController.checkAnimation();
//        	pendulumController.checkAnimation();
//            if (TabPendulum.isSelected()) {
//                gravityController.stopAnimation();
//                FXMLLoader pendulumLoader = new FXMLLoader(getClass().getResource("pendulumGui.fxml"));
//                ContentPane.getChildren().clear();
//                ContentPane.getChildren().add(pendulumLoader.load());
//                pendulumController = pendulumLoader.getController();
//                PanePendulum.getChildren().clear();
//                pendulumController.initialize(PanePendulum);
////                pendulumController.startAnimation();
//            } else if (TabGravity.isSelected()) {
//                pendulumController.stopAnimation();
//                FXMLLoader gravityLoader = new FXMLLoader(getClass().getResource("gravityGui.fxml"));
//                ContentPane.getChildren().clear();
//                ContentPane.getChildren().add(gravityLoader.load());
//				gravityController = gravityLoader.getController();
//				PaneGravity.getChildren().clear();
//				gravityController.initialize(PaneGravity);
////                //To nie miejsce na takie rzeczy, od tego jest przycisk reset, wewnątrz GUIcontroller
////                gravityController.gravityView = new GravityView(GDS.numOfBodies);
////                for (Node i : gravityController.gravityView.getNodes())
////                    PaneGravity.getChildren().add(i);
////				//Od tego przycisk start, ja dałem takie coś wyżej(w pendulum)?
////                gravityController.startAnimation();
//            }
//        }
//        else initEvent = true;
//    }
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

