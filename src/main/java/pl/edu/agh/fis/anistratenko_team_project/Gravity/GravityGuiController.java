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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.regex.Pattern;

/**
 * Class for controlling gravity simulation
 */
public class GravityGuiController {
    GDS gDS;
    private AnimationTimer gravityAnimation;
    private GravityView gravityView;
    private boolean placeBlackHole = false;
    private String regex = "[0-9]+";
    private Pattern pattern = Pattern.compile(regex);
    private int loadedValue;

    @FXML
    private TextFlow warning;

    @FXML
    private Slider speed;

    @FXML
    private Button defaultButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button reset;

    @FXML
    private Button start;

    @FXML
    private Button load;

    @FXML
    private Button blackHoleButton;

	@FXML
	public Pane guiPane;

    @FXML
    private TextField numOfBodiesInput;

    private Pane drawPane;

    /**
     * Constructor that does nothing
     */
    public GravityGuiController() {
    }

    /**
     * Should be called before starting the simulation
     * It is not constructor for framework's internal reasons
     * Initializes Animation, adds event listeners
     * @param p - pane in which gravity will be drawed
     * @param gds - GDS instance shared with Controller
     */
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

        speed.valueProperty().addListener((observableValue, old_val, new_val) -> gDS.FrameTime = new_val.doubleValue() / 60.);

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

        drawPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            setPaneSize(newVal.doubleValue(), drawPane.getHeight(), gDS.xreal, gDS.yreal);
        });
        drawPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            setPaneSize(drawPane.getWidth(), newVal.doubleValue(), gDS.xreal, gDS.yreal);
        });

        drawPane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (placeBlackHole){
                    gravityView.addBlackHoleView(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                    placeBlackHole = false;
                }
            }
        });
    }

    /**
     * When Load button is clicked, checks the input field and loads correct input to gDS
     * @param event
     * @throws Exception
     */
    @FXML
    private void onClickLoad(Event event) throws Exception {
        warning.getChildren().clear();

        int numOfBodies = parseInput(numOfBodiesInput);
        if (numOfBodies < 0){
            Text text = new Text("Provide number of bodies less than 25");

            warning.getChildren().add(text);
            setTextFieldColor(numOfBodiesInput, "red", "white");
            if (numOfBodies == -2){
                System.out.println("EMPTy");
                setTextFieldColor(numOfBodiesInput, "red", "RGB(0,0,0,0.1)");}
            return;
        }

        setTextFieldColor(numOfBodiesInput, "white", "RGB(255,255,255,0.6),RGB(0,255,0,0.3)");
        loadedValue = numOfBodies;

    }

    /**
     * When the Reset button is clicked, reloads simulation with latest loaded data
     * @param event - event passed to handler by framework to ... handle
     * @throws Exception
     */
    @FXML
    private void onClickReset(Event event) throws Exception {
        gravityView.resetGravityView(loadedValue);
//        setTextFieldColor(numOfBodiesInput, "white", "white");
//        numOfBodiesInput.clear();
    }

    /**
     * When the Defaut button is clicked, resets gravity's gui and simulation to default state(20 init bodies)
     * @param event - event passed to handler by framework to ... handle
     * @throws Exception
     */
    @FXML
    private void onClickDefault(Event event) throws Exception {
            numOfBodiesInput.clear();
            gravityView.resetGravityView(20);

            setTextFieldColor(numOfBodiesInput, "white", "white");
            loadedValue = 20;
    }


    /**
     * Set colors of borders and background for given text field
     * @param field - text field to color
     * @param colorBorder - new border color
     * @param colorBackground - new background color
     * @throws Exception
     */
    private void setTextFieldColor(TextField field, String colorBorder, String colorBackground){
        field.setStyle("-fx-border-color:" + colorBorder  + "; " + "-fx-background-color:" + colorBackground  + ";");
    }



    /**
     * Parses input data from the input text field
     * @param inputText - input field
     * @return - parsed or default value
     * @throws NumberFormatException
     * @throws NullPointerException
     */
    private int parseInput(TextField inputText) throws NumberFormatException, NullPointerException {

        if(inputText.getText().trim().isEmpty()){
            return -2; // empty input (yes, that's awkward. <shrug> )
        }

        if (!inputText.getText().trim().isEmpty() && pattern.matcher(inputText.getText()).matches()){
            int result = Integer.parseInt(inputText.getText());
            if (result > 0 && result < 25)
                return result;
        }

        return -1;
    }

    /**
     * When Start button is clicked, starts or stops the animation
     * @param event - event passed by the framework to handler to ... handle
     * @throws Exception
     */
    @FXML
    private void onClickStart(Event event) throws Exception {
        gDS.running = !gDS.running;
        if (gDS.running) {
            start.setText("Stop");
            startAnimation();
        } else {
            start.setText("start");
            stopAnimation();
        }
    }

    /**
     * When Create Black Hole button is clicked, creates a black hole in cursor's position
     * @param event - event passed by the framework to handler to ... handle
     * @throws Exception
     */
    @FXML
    private void createBlackHole(Event event) throws Exception{
        System.out.println("BLAAAACK HOOOOOOLE WOWOWOWO ");
        double y = drawPane.getHeight() / 7;
        double x = drawPane.getWidth() / 7;
        placeBlackHole = true;
//        gravityView.addBlackHoleView((int)x, (int)y);
    }


    public void startAnimation() {
        if (gravityAnimation != null) gravityAnimation.start();
    }

    public void stopAnimation() {
        if (gravityAnimation != null) gravityAnimation.stop();
    }

    /**
     * Sets pane's size in which the simulation will be visualised in pixels and in physical units
     * @param x1 - width of pane in pixels
     * @param y1 - heigth of pane in pixels
     * @param x2 - width of pane [m]
     * @param y2 - heigth of pane [m]
     */
    public void setPaneSize(double x1, double y1, double x2, double y2) {
        gravityView.setPaneSize(x1, y1, x2, y2);
    }

    /**
     * Sets pane in which pendulum will be drawed
     * @param p - pane to dra on
     */
    private void setPane(Pane p) {
        drawPane = p;
        for (Node i : gravityView.getNodes())
            drawPane.getChildren().add(i);
    }

}
