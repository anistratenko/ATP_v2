package sample;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * Created by yevhenii on 5/9/18.
 */
public class PendulumGuiController {

    private AnimationTimer PendulumAnimation;
    private PendulumView pendulumView = new PendulumView(0.15,0.2,0.2,0.1);;

    @FXML
    private Button Start;
    @FXML
    private Button Type;
	@FXML
	private Button Load;

	@FXML
	private TextField L1_input;
	@FXML
	private TextField PHI_input;
	@FXML
	private TextField M1_input;
	@FXML
	private TextField L2_input;


    private Pane drawPane;
    public PendulumGuiController(){};

    public void initialize(Pane p)
    {
        setPane(p);
        drawPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (!PDS.running)pendulumView.refresh();
        });

        drawPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (!PDS.running)pendulumView.refresh();
        });

        PendulumAnimation = new AnimationTimer() {
            long lastUpdate = 0;
            public void handle(long now) {
                if (now - lastUpdate >= 16_666_666) {
                    if (PDS.running) {
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
        if (PDS.running) PendulumAnimation.start();
    }

    @FXML
    private void onClickStart(Event event) throws Exception
    {
        PDS.running = !PDS.running;
        if ( PDS.running )
        {
            Start.setText("Stop");
            startAnimation();
        }
        else {
            Start.setText("Start");
            stopAnimation();
        }
    }

    @FXML
    private void onClickType(Event event) throws Exception
    {
        PDS.doublependulum = !PDS.doublependulum;
        if ( PDS.doublependulum ) Type.setText("Double");
        else Type.setText("Single");

    }

    @FXML
	private void onClickLoad(Event event) throws Exception
	{


	}

    private void setPane(Pane p)
    {
        drawPane = p;
        for (Node i : pendulumView.getNodes())
            drawPane.getChildren().add(i);
    }

    public void setVisible(boolean p)
    {
        Type.setVisible(p);
        Start.setVisible(p);
        Type.setManaged(p);
        Start.setManaged(p);
    }

    public void startAnimation()
    {
        PendulumAnimation.start();
    }

    public void stopAnimation() {
        PendulumAnimation.stop();
    }

    public void setPaneSize(double x1, double y1, double x2, double y2)
    {
        pendulumView.setPaneSize(x1, y1, x2, y2);
    }
}
