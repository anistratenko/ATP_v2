package pl.edu.agh.fis.anistratenko_team_project.Structure;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

public class StructureGuiController {
	private SDS sDS;
	private AnimationTimer structureAnimation;
	private StructureView structureView;

	@FXML
	private Button start;
	@FXML
	private void onClickStart(Event event) throws Exception {
		sDS.running = !sDS.running;
		if (sDS.running) {
			start.setText("Stop");
			startAnimation();
		} else {
			start.setText("start");
			stopAnimation();
		}
	}
	@FXML
	private Button wave;
	@FXML
	private void onClickWave(Event event) throws Exception {
		structureView.callWave();
	}

	@FXML
	private Button random;
	@FXML
	private void onClickRandom(Event event) throws Exception {
		structureView.callRandomize();
	}

	@FXML
	private Slider Speed;


	@FXML
	public Pane guiPane;
	private Pane drawPane;

	public StructureGuiController() {}

	public void initialize(Pane p, SDS sds) {
		sDS = sds;
		structureView = new StructureView(sDS);
		setPane(p);
		drawPane.widthProperty().addListener((obs, oldVal, newVal) -> {
			if (!sDS.running) structureView.refresh();
		});

		drawPane.heightProperty().addListener((obs, oldVal, newVal) -> {
			if (!sDS.running) structureView.refresh();

		});

		Speed.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number old_val, Number new_val) {
				sDS.FrameTime = new_val.doubleValue() / 60.;
			}
		});

		structureAnimation = new AnimationTimer() {
			long lastUpdate = 0;
			public void handle(long now) {
				if (now - lastUpdate >= 16_666_666) {
					if (sDS.running) {
						structureView.performSimulationStep();
					}
					lastUpdate = now;
				}
			}
		};

		drawPane.widthProperty().addListener((obs, oldVal, newVal) -> {
			setPaneSize(newVal.doubleValue(), drawPane.getHeight(), sDS.xreal, sDS.yreal);
		});
		drawPane.heightProperty().addListener((obs, oldVal, newVal) -> {
			setPaneSize(drawPane.getWidth(), newVal.doubleValue(), sDS.xreal, sDS.yreal);
		});
	}

	private void setPane(Pane p) {
		drawPane = p;
		for (Node i : structureView.getNodes())
			drawPane.getChildren().add(i);
	}

	public void startAnimation() {
		if (structureAnimation != null) structureAnimation.start();
	}

	public void stopAnimation() {
		if (structureAnimation != null) structureAnimation.stop();
	}

	public void setPaneSize(double x1, double y1, double x2, double y2) {
		structureView.setPaneSize(x1, y1, x2, y2);
	}


}
