package sample;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;


public class Controller {
    private boolean initEvent = false;

    @FXML
    public void initialize()
    {
        pendulumView = new PendulumView(1,1,1,1);
        gravityView = new GravityView(20);
        PanePendulum.widthProperty().addListener((obs, oldVal, newVal) -> {
            pendulumView.setPaneSize(PanePendulum.getWidth(),PanePendulum.getHeight(), PDS.xreal, PDS.yreal);
        });
        PanePendulum.heightProperty().addListener((obs, oldVal, newVal) -> {
            pendulumView.setPaneSize(PanePendulum.getWidth(),PanePendulum.getHeight(), PDS.xreal, PDS.yreal);
        });
        PaneGravity.widthProperty().addListener((obs, oldVal, newVal) -> {
            gravityView.setPaneSize(PaneGravity.getWidth(),PaneGravity.getHeight(), GDS.xreal, GDS.yreal);
        });
        PaneGravity.heightProperty().addListener((obs, oldVal, newVal) -> {
            gravityView.setPaneSize(PaneGravity.getWidth(),PaneGravity.getHeight(), GDS.xreal, GDS.yreal);
        });

        PendulumAnimation = new AnimationTimer() {
            long lastUpdate = 0;
            public void handle(long now) {
                if (now - lastUpdate >= 16_666_666) {
                    if (PDS.running) {
                        if (pendulumView.performSimulationStep()) {
                            PanePendulum.getChildren().clear();
                            for (Node i : pendulumView.getNodes())
                                PanePendulum.getChildren().add(i);
                        }
                    }
                    lastUpdate = now;
                }
            }
        };

        GravityAnimation = new AnimationTimer() {
            long lastUpdate = 0;
            public void handle(long now) {
                if (now - lastUpdate >= 16_666_666) {
                    if (GDS.running) {
                    if (gravityView.performSimulationStep()){
                            PaneGravity.getChildren().clear();
                            for (Node i : gravityView.getNodes())
                                PaneGravity.getChildren().add(i);
                        }
                    }
                    lastUpdate = now;
                }
            }
        };
    }

    @FXML
    private Pane PanePendulum;
    AnimationTimer PendulumAnimation;
    PendulumView pendulumView;


    @FXML
    private Pane PaneGravity;
    AnimationTimer GravityAnimation;
    GravityView gravityView;

    @FXML
    private Pane ContentPane;

    @FXML
    private Tab TabGravity;

    @FXML
    private Tab TabPendulum;

    @FXML
    private void ChangeGui(Event event) throws Exception{
        if (initEvent) {
            System.out.println("ACTION");
            GravityAnimation.stop();
            PendulumAnimation.stop();
            if (PanePendulum != null) PanePendulum.getChildren().clear();
            if (PaneGravity != null) PaneGravity.getChildren().clear();
            if (TabPendulum.isSelected()) {
                System.out.println("ACTION Pendulum " + ContentPane);
                FXMLLoader pendulumLoader = new FXMLLoader(getClass().getResource("pendulumGui.fxml"));
                if (ContentPane != null) ContentPane.getChildren().clear();
                if (ContentPane != null) ContentPane.getChildren().add(pendulumLoader.load());
                if (PDS.doublependulum) pendulumView = new PendulumView(PDS.l1*1., PDS.m1*1., PDS.l2*1, PDS.m2*1);
                else pendulumView = new PendulumView(PDS.l1*1., PDS.m1*1);
                pendulumView.setPaneSize(PanePendulum.getWidth(), PanePendulum.getHeight(), PDS.xreal, PDS.yreal);
                for (Node i : pendulumView.getNodes())
                    PanePendulum.getChildren().add(i);
                PendulumAnimation.start();
            } else if (TabGravity.isSelected()) {
                System.out.println("ACTION GRAVITY");
                FXMLLoader gravityLoader = new FXMLLoader(getClass().getResource("gravityGui.fxml"));
                if (ContentPane != null) ContentPane.getChildren().clear();
                if (ContentPane != null) ContentPane.getChildren().add(gravityLoader.load());
                gravityView = new GravityView(GDS.num_of_bodies);
                for (Node i : gravityView.getNodes())
                    PaneGravity.getChildren().add(i);
                gravityView.setPaneSize(500, 500, GDS.xreal, GDS.yreal);
                GravityAnimation.start();
            }
        }
        else initEvent = true;
    }
}

