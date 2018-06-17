package pl.edu.agh.fis.anistratenko_team_project.Structure;

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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class StructureGuiController {
	private SDS sDS;
	private AnimationTimer structureAnimation;
	private StructureView structureView;

	@FXML
	private TextFlow warning;

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
	private TextField G_INPUT;
	@FXML
	private TextField K_INPUT;

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

	@FXML
	private void onClickLoad(Event event) throws Exception {
		Double tempK, tempG;

		warning.getChildren().clear();
		StringBuilder text = new StringBuilder("");

		String regex_signed_double = "^-?[0-9]*\\.?[0-9]+$";
		String regex_unsigned_double = "^[0-9]*\\.?[0-9]+$";

		Pattern pattern_signed = Pattern.compile(regex_signed_double);
		Pattern pattern_unsigned_not_zero = Pattern.compile(regex_unsigned_double);

		tempG = parseInput(G_INPUT, sDS.g, pattern_signed, text);
		tempK = parseInput(K_INPUT, sDS.k, pattern_unsigned_not_zero, text);

		sDS.g = tempG;
		sDS.k = tempK;
		if (!text.toString().isEmpty())
		{
			ResourceBundle bundle = ResourceBundle.getBundle("language.Locale", new Locale("pl"));

			Text message = new Text( bundle.getString("pendulum_wrong_input_begin")+ text.toString() +bundle.getString("pendulum_wrong_input_end"));
			warning.getChildren().add(message);
		}
	}

	@FXML
	private void onClickReset(Event event) throws Exception{
		structureView.callReset();
		sDS.g = 0; G_INPUT.clear();
		sDS.k = 5; K_INPUT.clear();
	}


	private Double parseInput(TextField inputText, Double defaultInput, Pattern pattern, StringBuilder text) throws NumberFormatException, NullPointerException {

		if (!inputText.getText().trim().isEmpty()) {
			if (pattern.matcher(inputText.getText()).matches()) {
				inputText.setStyle("-fx-background-color:RGB(255,255,255,0.6),RGB(0,255,0,0.3);");
				return Double.parseDouble(inputText.getText());
			}
			else {
				text.append(" "+inputText.getPromptText());
				inputText.setStyle("-fx-border-color:RGB(255,0,0,1);-fx-background-color:RGB(255,255,255,0.6),RGB(255,0,0,0.05);");
			}
		}
		else {
			inputText.setStyle("-fx-border-color:RGB(255,255,255,0.3);-fx-background-color:RGB(0,0,0,0.1);-fx-prompt-text-fill:RGB(255,255,255);");
		}
		return defaultInput;
	}

}
