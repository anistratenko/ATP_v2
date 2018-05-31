package pl.edu.agh.fis.anistratenko_team_project.Gravity;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


/**
 * Created by yevhenii on 5/8/18.
 */
public class GravityGuiController {
    private AnimationTimer gravityAnimation;
    private GravityView gravityView = new GravityView(GDS.numOfBodies);

    @FXML
    private Button gravityButton;

    @FXML
    private Button Start;

	@FXML
	public Pane GUIPane;

    @FXML
    private TextField numOfBodiesInput;


    private Pane drawPane;

    public GravityGuiController(){}

    public void initialize(Pane p)
    {
        setPane(p);
        drawPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (!GDS.running) gravityView.refresh();
        });

        drawPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (!GDS.running) gravityView.refresh();

        });

        gravityAnimation = new AnimationTimer() {
            long lastUpdate = 0;
            public void handle(long now) {
                if (now - lastUpdate >= 16_666_666) {
                    if (GDS.running) {
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
        checkAnimation();
    }


    public void reinitialize()
    {
        drawPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (!GDS.running) gravityView.refresh();
        });

        drawPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (!GDS.running) gravityView.refresh();

        });

        for (Node i : gravityView.getNodes())
            drawPane.getChildren().add(i);

//        gravityAnimation = new AnimationTimer() {
//            long lastUpdate = 0;
//            public void handle(long now) {
//                if (now - lastUpdate >= 16_666_666) {
//                    if (GDS.running) {
//                        if (gravityView.performSimulationStep()) {
//                            drawPane.getChildren().clear();
//                            for (Node i : gravityView.getNodes())
//                                drawPane.getChildren().add(i);
//                        }
//                    }
//                    lastUpdate = now;
//                }
//            }
//        };
        checkAnimation();
    }

    @FXML
    private void onClickGravity(Event event) throws Exception{
        System.out.println("CLICK ");
        int numOfBodies =  parseInput(numOfBodiesInput, 20);
        gravityView.resetGravityView(numOfBodies);
        checkAnimation();
    }

    @FXML
    private void onClickStart(Event event) throws Exception
    {
        GDS.running = !GDS.running;
        if ( GDS.running )        {
            Start.setText("Stop");
            startAnimation();
        }
        else {
            Start.setText("Start");
            stopAnimation();
        }
    }

    public void checkAnimation() {
		System.out.println("Gravity: " + this);
		if(gravityAnimation != null ) System.out.println("All OK");
		else System.out.println("Not too good");
	}

    public void setPane(Pane p) {
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
		if(gravityAnimation != null ) gravityAnimation.start();
    }

    public void stopAnimation() {
		if(gravityAnimation != null ) gravityAnimation.stop();
    }



    public void setPaneSize(double x1, double y1, double x2, double y2) {
        gravityView.setPaneSize(x1, y1, x2, y2);

    }

    private int parseInput(TextField inputText,  int defaultValue) throws NumberFormatException, NullPointerException {
        if (!inputText.getText().trim().isEmpty())
            return Integer.parseInt(inputText.getText());
        else
            return defaultValue;
    }

}
