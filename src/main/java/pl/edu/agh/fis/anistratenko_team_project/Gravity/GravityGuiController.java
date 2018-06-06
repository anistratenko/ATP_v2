package pl.edu.agh.fis.anistratenko_team_project.Gravity;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class GravityGuiController {
    GDS gDS;
    private AnimationTimer gravityAnimation;
    private GravityView gravityView;
    private boolean placeBlackHole = false;


    @FXML
    private Slider Speed;
    @FXML
    private Button gravityButton;

    @FXML
    private Button Start;

    @FXML
    private Button blackHoleButton;

	@FXML
	public Pane GUIPane;

    @FXML
    private TextField numOfBodiesInput;

    @FXML
    private TextField blackHoleXInput;

    @FXML
    private TextField blackHoleYInput;

    private Pane drawPane;

    public GravityGuiController() {
    }

    public void initialize(Pane p, GDS gds) {
        gDS = gds;
        gravityView = new GravityView(gDS.numOfBodies, gDS);
        setPane(p);
        drawPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (!gDS.running) gravityView.refresh();
        });

        drawPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (!gDS.running) gravityView.refresh();

        });

        Speed.valueProperty().addListener((observableValue, old_val, new_val) -> gDS.FrameTime = new_val.doubleValue() / 60.);

        gravityAnimation = new AnimationTimer() {
            long lastUpdate = 0;
            public void handle(long now) {
                if (now - lastUpdate >= 16_666_666) {
                    if (gDS.running) {
                        if (gravityView.performSimulationStep()) {
                            drawPane.getChildren().clear();
                            for (Node i : gravityView.getNodes())
                                drawPane.getChildren().add(i);
                        }
                    }
                    lastUpdate = now;
                }
            }
        };

        drawPane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (placeBlackHole){
                    gravityView.addBlackHoleView((int)mouseEvent.getSceneX(), (int)mouseEvent.getSceneY());
                    placeBlackHole = false;
                }
                System.out.println("EVT: " + (int)mouseEvent.getSceneX() + " " + (int)mouseEvent.getSceneY());

            }
        });
        checkAnimation();
    }



    @FXML
    private void onClickGravity(Event event) throws Exception {
        System.out.println("CLICK ");
        int numOfBodies = parseInput(numOfBodiesInput, 20);
        gravityView.resetGravityView(numOfBodies);
        checkAnimation();
    }

    @FXML
    private void onClickStart(Event event) throws Exception {
        gDS.running = !gDS.running;
        if (gDS.running) {
            Start.setText("Stop");
            startAnimation();
        } else {
            Start.setText("Start");
            stopAnimation();
        }
    }

    @FXML
    private void createBlackHole(Event event) throws Exception{
        System.out.println("BLAAAACK HOOOOOOLE WOWOWOWO ");
        double y = drawPane.getHeight() / 7;
        double x = drawPane.getWidth() / 7;
        placeBlackHole = true;
//        gravityView.addBlackHoleView((int)x, (int)y);
    }

    private void checkAnimation() {
        System.out.println("Gravity: " + this);
        if (gravityAnimation != null) System.out.println("All OK");
        else System.out.println("Not too good");
    }

    private void setPane(Pane p) {
        drawPane = p;
        for (Node i : gravityView.getNodes())
            drawPane.getChildren().add(i);
    }

    public void setVisible(boolean p) {
        gravityButton.setVisible(p);
        Start.setVisible(p);
        gravityButton.setManaged(p);
        Start.setManaged(p);
    }

    public void startAnimation() {
        if (gravityAnimation != null) gravityAnimation.start();
    }

    public void stopAnimation() {
        if (gravityAnimation != null) gravityAnimation.stop();
    }


    public void setPaneSize(double x1, double y1, double x2, double y2) {
        gravityView.setPaneSize(x1, y1, x2, y2);

    }

    private int parseInput(TextField inputText, int defaultValue) throws NumberFormatException, NullPointerException {
        if (!inputText.getText().trim().isEmpty() && Integer.parseInt(inputText.getText()) < 25){
            return Integer.parseInt(inputText.getText());
        }
        else {
                System.out.println("Provide number of bodies less than 25");
                return defaultValue;
            }
    }

}
