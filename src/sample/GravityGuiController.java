package sample;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;


/**
 * Created by yevhenii on 5/8/18.
 */
public class GravityGuiController {
    public AnimationTimer gravityAnimation;
    public GravityView gravityView = new GravityView(GDS.num_of_bodies);

    @FXML
    private Button gravityButton;

    @FXML
    private Button Start;

    public Pane drawPane;

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
        if (GDS.running) gravityAnimation.start();
    }

    @FXML
    private void onClickGravity(Event event) throws Exception{
        System.out.println("CLICK GRAVITY");
    }

    @FXML
    private void onClickStart(Event event) throws Exception
    {
        GDS.running = !GDS.running;
        if ( GDS.running )Start.setText("Stop");
        else Start.setText("Start");
    }

    public void setPane(Pane p)
    {
        drawPane = p;
        for (Node i : gravityView.getNodes())
            drawPane.getChildren().add(i);
    }

    public void setVisible(boolean p)
    {
        gravityButton.setVisible(p);
        Start.setVisible(p);
        gravityButton.setManaged(p);
        Start.setManaged(p);
    }

    public void startAnimation()
    {
        gravityAnimation.start();
    }

    public void stopAnimation() {
        gravityAnimation.stop();
    }

    public void setPaneSize(double x1, double y1, double x2, double y2)
    {
        gravityView.setPaneSize(x1, y1, x2, y2);

    }
}
