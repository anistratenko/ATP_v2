package pl.edu.agh.fis.anistratenko_team_project.Application;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import pl.edu.agh.fis.anistratenko_team_project.Gravity.GDS;
import pl.edu.agh.fis.anistratenko_team_project.Gravity.GravityGuiController;
import pl.edu.agh.fis.anistratenko_team_project.Pendulum.PDS;
import pl.edu.agh.fis.anistratenko_team_project.Pendulum.PendulumGuiController;
import pl.edu.agh.fis.anistratenko_team_project.Structure.SDS;
import pl.edu.agh.fis.anistratenko_team_project.Structure.StructureGuiController;

import java.util.Locale;
import java.util.ResourceBundle;


public class Controller {
    boolean darkMode = false;
    private PDS pDS = new PDS();
    private GDS gDS = new GDS();
    private SDS sDS = new SDS();

    private boolean initEvent = false;
    private double xOffset = 0;
    private double yOffset = 0; // for dragging


    @FXML
    private Button buttonCloseHelp;
    @FXML
    private GridPane innerWrapperGrid;

    @FXML
    private Pane PanePendulum;

    @FXML
    private Pane PaneGravity;

    @FXML
	private Pane PaneStructure;

    @FXML
    private Pane ContentPane = new Pane(); //nullpointer exception after process starts(appears once)

    @FXML
    private Tab TabGravity;

    @FXML
    private Tab TabPendulum;

	@FXML
	private Tab TabStructure;


    @FXML
    private MenuBar menuBar;

    @FXML
    private GravityGuiController gravityController;

    @FXML
    private PendulumGuiController pendulumController;

	@FXML
	private StructureGuiController structureController;

    @FXML
    private TabPane tabPane;

    @FXML
    private Pane tabPaneWrapper;

    @FXML
    private Text help;

    //
    @FXML
    public void initialize() {

        buttonCloseHelp.setVisible(false);
        help.setVisible(false);


        menuBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        menuBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Main.primaryStage.setX(event.getScreenX() - xOffset);
                Main.primaryStage.setY(event.getScreenY() - yOffset);
            }
        });

        if (PanePendulum != null) {
            pendulumController.initialize(PanePendulum, pDS);
            System.out.println("Initialized");
        } else {
            System.out.println("Not initialized");
        }

        PanePendulum.widthProperty().addListener((obs, oldVal, newVal) -> {
            pendulumController.setPaneSize(PanePendulum.getWidth(), PanePendulum.getHeight(), pDS.xreal, pDS.yreal);
        });
        PanePendulum.heightProperty().addListener((obs, oldVal, newVal) -> {
            pendulumController.setPaneSize(PanePendulum.getWidth(), PanePendulum.getHeight(), pDS.xreal, pDS.yreal);
        });

        if (PaneGravity != null) {
            gravityController.initialize(PaneGravity, gDS);
            System.out.println("Initialized Gravity");
        }

		if (PaneStructure != null) {
			structureController.initialize(PaneStructure, sDS);
			System.out.println("Initialized Structure");
		}
		PaneStructure.widthProperty().addListener((obs, oldVal, newVal) -> {
			structureController.setPaneSize(PaneStructure.getWidth(), PaneStructure.getHeight(), sDS.xreal, sDS.yreal);
		});
		PaneStructure.heightProperty().addListener((obs, oldVal, newVal) -> {
			structureController.setPaneSize(PaneStructure.getWidth(), PaneStructure.getHeight(), sDS.xreal, sDS.yreal);
		});

        tabPane.setFocusTraversable(false);

        assert PaneGravity != null;
//        PaneGravity.widthProperty().addListener((obs, oldVal, newVal) -> {
//            gravityController.setPaneSize(newVal.doubleValue(), PaneGravity.getHeight(), gDS.xreal, gDS.yreal);
//        });
//        PaneGravity.heightProperty().addListener((obs, oldVal, newVal) -> {
//            gravityController.setPaneSize(PaneGravity.getWidth(), newVal.doubleValue(), gDS.xreal, gDS.yreal);
//        });
    }


    @FXML
    private void ChangeGui(Event event) throws Exception {
        if (initEvent) {
            if (TabPendulum.isSelected()) {
                gravityController.stopAnimation();
                gravityController.guiPane.setVisible(false);
                gravityController.guiPane.setManaged(false);
				structureController.stopAnimation();
				structureController.guiPane.setVisible(false);
				structureController.guiPane.setManaged(false);
                pendulumController.GUIPane.setVisible(true);
                pendulumController.GUIPane.setManaged(true);


//                pendulumController.startAnimation();
            }
            else if (TabGravity.isSelected())
            {
                pendulumController.stopAnimation();
                pendulumController.GUIPane.setVisible(false);
                pendulumController.GUIPane.setManaged(false);
				structureController.stopAnimation();
				structureController.guiPane.setVisible(false);
				structureController.guiPane.setManaged(false);
                gravityController.guiPane.setVisible(true);
                gravityController.guiPane.setManaged(true);

//                gravityController.startAnimation();
            }
			else if (TabStructure.isSelected())
			{
				pendulumController.stopAnimation();
				pendulumController.GUIPane.setVisible(false);
				pendulumController.GUIPane.setManaged(false);
				gravityController.stopAnimation();
				gravityController.guiPane.setVisible(false);
				gravityController.guiPane.setManaged(false);
				structureController.guiPane.setVisible(true);
				structureController.guiPane.setManaged(true);

//                structureController.startAnimation();
			}
        } else initEvent = true;
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
////                gravityController.gravityView = new GravityView(gDS.numOfBodies);
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
    private MenuItem closeButton;

    @FXML
    private MenuItem menuHelp;

    @FXML
    private void changeLanguageEN(Event event) throws Exception {
        Locale currLocale = new Locale("en");
        changeLocale(currLocale);
    }

    @FXML
    private void changeLanguagePL(Event event) throws Exception {
        Locale currLocale = new Locale("pl");
        changeLocale(currLocale);
    }


    @FXML
    private void changeModeDark(Event event) throws Exception {
        Main.root.getStylesheets().clear();
        Main.root.getStylesheets().add("/stylesheets/nightStyle.css");
        darkMode = false;
    }

    @FXML
    private void changeModeLight(Event event) throws Exception {
        Main.root.getStylesheets().clear();
        Main.root.getStylesheets().add("/stylesheets/dayStyle.css");
        darkMode = true;
    }

    @FXML
    private void exit(Event event) {

        gravityController.stopAnimation();
        pendulumController.stopAnimation();

        Main.primaryStage.close();

    }

    private void changeLocale(Locale locale) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("language.Locale", locale);

        Main.controller.gravityController.stopAnimation();
        Main.controller.pendulumController.stopAnimation();

        Main.fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/application.fxml"));
        Main.fxmlLoader.setResources(bundle);

        GridPane newNode = Main.fxmlLoader.load();
        Main.controller = Main.fxmlLoader.getController();
        Main.primaryStage.setTitle(Main.fxmlLoader.getResources().getString("window_title"));
        Main.root.getChildren().setAll(newNode.getChildren());
    }

    @FXML
    private void showHelp(){
        tabPane.setVisible(false);
        help.setVisible(true);
        buttonCloseHelp.setVisible(true);
    }

    @FXML
    private void closeHelp(){
        help.setVisible(false);
        buttonCloseHelp.setVisible(false);
        tabPane.setVisible(true);
    }
}

