package pl.edu.agh.fis.anistratenko_team_project.Pendulum;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * Created by yevhenii on 5/9/18.
 */
public class PendulumGuiController {

    private AnimationTimer pendulumAnimation;
    private PendulumView pendulumView = new PendulumView(0.15, 0.2, 0.2, 0.1);
    ;

    @FXML
    private Button Start;
    @FXML
    private Button Type;
    @FXML
    private Button Load;

    @FXML
    public Pane GUIPane;

    @FXML
    private TextField L1_input;
    @FXML
    private TextField PHI_input;
    @FXML
    private TextField M1_input;
    @FXML
    private TextField L2_input;
    @FXML
    private TextField THETA_input;
    @FXML
    private TextField M2_input;
    @FXML
    private Slider Speed;


    private Pane drawPane;

    public PendulumGuiController() {
    }

    public void initialize(Pane p) {
        setPane(p);
        drawPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (!PDS.running) pendulumView.refresh();
        });

        drawPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (!PDS.running) pendulumView.refresh();
        });

        Speed.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number old_val, Number new_val) {
                PDS.FrameTime = new_val.doubleValue() / 60.;
            }
        });

        pendulumAnimation = new AnimationTimer() {
            long lastUpdate = 0;

            public void handle(long now) {
                if (now - lastUpdate >= 16_666_666) {
                    if (PDS.running) {
                        fitPendulum(1., 1.);
                        if (pendulumView.performSimulationStep()) {
                            drawPane.getChildren().clear();
                            for (Node i : pendulumView.getNodes())
                                drawPane.getChildren().add(i);
                        }
                    }
                    lastUpdate = now;
                }
            }
        };
        if (PDS.running) pendulumAnimation.start();
    }

    private void fitPendulum(Double xmin, Double ymin) {

        Double temp = (PDS.l1 + PDS.l2) * 2.;
        if (temp > PDS.xreal || temp > PDS.yreal) {
            PDS.xreal = temp * 2;
            PDS.yreal = temp * 2;

        } else if (temp < PDS.xreal / 2. || temp < PDS.yreal / 2.) {
            PDS.xreal = temp < xmin ? xmin : temp;
            PDS.yreal = temp < xmin ? xmin : temp;
        }
    }

    @FXML
    private void onClickStart(Event event) throws Exception {
        checkAnimation();
        PDS.running = !PDS.running;
        if (PDS.running) {
            Start.setText("Stop");
            startAnimation();
        } else {
            Start.setText("Start");
            stopAnimation();
        }
    }

    public void checkAnimation() {
        System.out.println("Pendulum: " + this);
        if (pendulumAnimation != null) System.out.println("All OK");
        else System.out.println("Not too good");
    }

    @FXML
    private void onClickType(Event event) throws Exception {
        PDS.doublependulum = !PDS.doublependulum;
        if (PDS.doublependulum) Type.setText("Double");
        else Type.setText("Single");

    }

    @FXML
    private void onClickLoad(Event event) throws Exception {
        Double tempL1, tempL2, tempM1, tempM2, tempPHI, tempTHETA;
        try {
            tempL1 = parseInput(L1_input, PDS.l1);
            tempL2 = parseInput(L2_input, PDS.l2);
            tempM1 = parseInput(M1_input, PDS.m1);
            tempM2 = parseInput(M2_input, PDS.m2);
            tempPHI = parseInput(PHI_input, PDS.phi);
            tempTHETA = parseInput(THETA_input, PDS.theta);

            if (tempL1 <= 0 || tempL2 <= 0 || tempM1 <= 0 || tempM2 <= 0)
                throw new IllegalArgumentException("Lengths and masses have to be greater than 0");


            PDS.l1 = tempL1;
            PDS.l2 = tempL2;
            PDS.m1 = tempM1;
            PDS.m2 = tempM2;
            PDS.phi = tempPHI;
            PDS.theta = tempTHETA;
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        } catch (IllegalArgumentException e) {
        }
    }

    private Double parseInput(TextField inputText, Double defaultInput) throws NumberFormatException, NullPointerException {
        if (!inputText.getText().trim().isEmpty()) return Double.parseDouble(inputText.getText());
        else return defaultInput;
    }

    private void setPane(Pane p) {
        drawPane = p;
        for (Node i : pendulumView.getNodes())
            drawPane.getChildren().add(i);
    }

    public void setVisible(boolean p) {
        Type.setVisible(p);
        Start.setVisible(p);
        Type.setManaged(p);
        Start.setManaged(p);
    }

    public void startAnimation() {
        pendulumAnimation.start();
    }

    public void stopAnimation() {
        pendulumAnimation.stop();
    }

    public void setPaneSize(double x1, double y1, double x2, double y2) {
        pendulumView.setPaneSize(x1, y1, x2, y2);
    }
}
