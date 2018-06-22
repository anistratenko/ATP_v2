package pl.edu.agh.fis.anistratenko_team_project.Pendulum;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import pl.edu.agh.fis.anistratenko_team_project.Application.Main;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Class for controlling pendulum simulation
 */
public class PendulumGuiController {
    private PDS pDS;
    private AnimationTimer pendulumAnimation;
    private PendulumView pendulumView;

	@FXML
	private TextFlow warning;

    @FXML
    private Button Start;
    @FXML
    private Button Type;
    @FXML
    private Button Load;
	@FXML
	private Button RESET;

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
	private TextField G_input;
	@FXML
	private TextField F_input;
	@FXML
	private TextField C_input;

	private ArrayList<TextField> textFieldsList = new ArrayList<TextField>() ;

    @FXML
    private Slider Speed;

    private Pane drawPane;

	/**
	 * Constructor that does nothing
	 */
	public PendulumGuiController() {

    }

	/**
	 * Method that needs to be called before simulation can be performed
	 * It is not constructor for technical reasons
	 * @param p - pane in which pendulum will be drawed
	 * @param pds - PDS instance shared with Controller
	 */
	public void initialize(Pane p, PDS pds) {
        pDS = pds;
		pendulumView = new PendulumView(0.15, 0.2, 0.2, 0.1, pDS);
        setPane(p);
        drawPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (!pDS.running) pendulumView.refresh();
        });

        drawPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (!pDS.running) pendulumView.refresh();
        });

        Speed.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number old_val, Number new_val) {
                pDS.FrameTime = new_val.doubleValue() / 60.;
            }
        });

        pendulumAnimation = new AnimationTimer() {
            long lastUpdate = 0;

            public void handle(long now) {
                if (now - lastUpdate >= 16_666_666) {
                    if (pDS.running) {
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
        pDS.l1 = 0.25; pDS.l2 = 0.25; pDS.m1 = 0.2; pDS.m2 = 0.2; pDS.phi = Math.PI/2.; pDS.theta = 0.; pDS.d_phi = 0.; pDS.d_theta = 0.; pDS.real_t = 0.; pDS.t = 0.; pDS.d2_phi = 0.; pDS.d2_theta = 0.; pDS.g = -9.81; pDS.fx = 0.; pDS.fx_when_control=0.; pDS.c=0.;
        pDS.loaded_l1 = 0.25; pDS.loaded_l2 = 0.25; pDS.loaded_m1 = 0.2; pDS.loaded_m2 = 0.2; pDS.loaded_phi = Math.PI/2.; pDS.loaded_theta = 0.; pDS.loaded_g = -9.81;  pDS.loaded_fx_when_control=0.; pDS.loaded_c = 0.;

        if (pDS.running) pendulumAnimation.start();

        //add textfields to collection
		textFieldsList.add(L1_input);
		textFieldsList.add(L2_input);
		textFieldsList.add(M1_input);
		textFieldsList.add(M2_input);
		textFieldsList.add(THETA_input);
		textFieldsList.add(PHI_input);
		textFieldsList.add(G_input);
		textFieldsList.add(F_input);
		textFieldsList.add(C_input);

		//set key controll of horizontal force
		drawPane.setFocusTraversable(true);
		drawPane.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.LEFT) {
				pDS.fx = pDS.fx_when_control;
			}
			else if (e.getCode() == KeyCode.RIGHT) {
				pDS.fx = -pDS.fx_when_control;
			}
		});
		drawPane.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT ) {
				pDS.fx = 0;
			}
		});

    }

	/**
	 * change size of pane in physical units, so whole pendulum can be seen
	 * @param xmin - minimum width of pane in physial units
	 * @param ymin - minimum heigth of pane in physial units
	 */
	private void fitPendulum(Double xmin, Double ymin) {

        Double temp = (pDS.l1 + pDS.l2) * 2.;
        if (temp > pDS.xreal || temp > pDS.yreal) {
            pDS.xreal = temp * 2;
            pDS.yreal = temp * 2;

        } else if (temp < pDS.xreal / 2. || temp < pDS.yreal / 2.) {
            pDS.xreal = temp < xmin ? xmin : temp;
            pDS.yreal = temp < xmin ? xmin : temp;
        }
    }

	/**
	 * Event when Start button is clicked, starts or stops animation
	 * @param event
	 * @throws Exception
	 */
	@FXML
    private void onClickStart(Event event) throws Exception {
        pDS.running = !pDS.running;
        if (pDS.running) {
            Start.setText("Stop");
            startAnimation();
        } else {
            Start.setText("Start");
            stopAnimation();
        }
    }

	/**
	 * Event when the Defaut button is clicked, resets pendulums gui and simulation to default state
	 * @param event
	 * @throws Exception
	 */
	@FXML
	private void onClickDefault(Event event) throws Exception {
		//change data stored in pDS
		pDS.l1 = 0.25; pDS.l2 = 0.25; pDS.m1 = 0.2; pDS.m2 = 0.2; pDS.phi = Math.PI/2.; pDS.theta = 0.; pDS.d_phi = 0.; pDS.d_theta = 0.; pDS.real_t = 0.; pDS.t = 0.; pDS.d2_phi = 0.; pDS.d2_theta = 0.; pDS.g = -9.81; pDS.fx = 0.; pDS.fx_when_control=0.; pDS.c=0.;
        pDS.loaded_l1 = 0.25; pDS.loaded_l2 = 0.25; pDS.loaded_m1 = 0.2; pDS.loaded_m2 = 0.2; pDS.loaded_phi = Math.PI/2.; pDS.loaded_theta = 0.; pDS.loaded_g = -9.81;  pDS.loaded_fx_when_control=0.; pDS.loaded_c = 0.;

        //clear input fields
        for (TextField x : textFieldsList)
		{
			x.clear();
			x.setStyle("-fx-background-color:RGB(255,255,255,1);");
		}
	}

	/**
	 * Event when the Reset button is clicked, reloads simulation with latest loaded data
	 * @param event
	 * @throws Exception
	 */
	@FXML
    private void onClickReset(Event event) throws Exception
    {
        pDS.l1 = pDS.loaded_l1; pDS.l2 = pDS.loaded_l2; pDS.m1 = pDS.loaded_m1; pDS.m2 = pDS.loaded_m2; pDS.phi = pDS.loaded_phi; pDS.theta = pDS.loaded_theta; pDS.d_phi = 0.; pDS.d_theta = 0.; pDS.real_t = 0.; pDS.t = 0.; pDS.d2_phi = 0.; pDS.d2_theta = 0.; pDS.g = pDS.loaded_g; pDS.fx = 0.; pDS.fx_when_control = pDS.loaded_fx_when_control;
    }

	/**
	 * Event when Type button is clicked, change pendulum type between double and single
	 * @param event
	 * @throws Exception
	 */
	@FXML
    private void onClickType(Event event) throws Exception {
        pDS.doublependulum = !pDS.doublependulum;
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle("language.Locale", Main.fxmlLoader.getResources().getLocale());

        if (pDS.doublependulum)
            Type.setText(bundle.getString("button_double"));
        else
            Type.setText(bundle.getString("button_single"));

    }

	/**
	 * Event when Load button is clicked, checks input fields and loads correct input to pDS
	 * @param event
	 * @throws Exception
	 */
	@FXML
    private void onClickLoad(Event event) throws Exception {
        Double tempL1, tempL2, tempM1, tempM2, tempPHI, tempTHETA, tempGravity, tempForce, tempDrag;

        warning.getChildren().clear();
		StringBuilder text = new StringBuilder("");
        String regex_signed_double = "^-?[0-9]*\\.?[0-9]+$";
		String regex_unsigned_double = "^[0-9]*\\.?[0-9]+$";
		String regex_unsigned_double_not_zero = "^(?!-?0*\\.*0*$)[0-9]*\\.?[0-9]+$";

        Pattern pattern_unsigned = Pattern.compile(regex_unsigned_double_not_zero);
        Pattern pattern_signed = Pattern.compile(regex_signed_double);
        Pattern pattern_unsigned_not_zero = Pattern.compile(regex_unsigned_double);

        tempL1 = parseInput(L1_input, pDS.loaded_l1, pattern_unsigned, text);
        tempL2 = parseInput(L2_input, pDS.loaded_l2, pattern_unsigned, text);
        tempM1 = parseInput(M1_input, pDS.loaded_m1, pattern_unsigned, text);
        tempM2 = parseInput(M2_input, pDS.loaded_m2, pattern_unsigned, text);
        tempPHI = parseInput(PHI_input, pDS.loaded_phi, pattern_signed, text);
        tempTHETA = parseInput(THETA_input, pDS.loaded_theta, pattern_signed, text);
        tempGravity = parseInput(G_input, pDS.loaded_g, pattern_signed, text);
        tempForce = parseInput(F_input, pDS.loaded_fx_when_control, pattern_unsigned, text);
        tempDrag = parseInput(C_input, pDS.loaded_c, pattern_unsigned_not_zero, text);

        pDS.loaded_l1 = tempL1;
        pDS.loaded_l2 = tempL2;
        pDS.loaded_m1 = tempM1;
        pDS.loaded_m2 = tempM2;
        pDS.loaded_phi = tempPHI;
        pDS.loaded_theta = tempTHETA;
        pDS.loaded_g = tempGravity;
        pDS.loaded_fx_when_control = tempForce;
        pDS.loaded_c = tempDrag;
		if (!text.toString().isEmpty())
		{
			ResourceBundle bundle = ResourceBundle.getBundle("language.Locale",  Main.fxmlLoader.getResources().getLocale());

			Text message = new Text( bundle.getString("pendulum_wrong_input_begin")+ text.toString() +bundle.getString("pendulum_wrong_input_end"));
			warning.getChildren().add(message);
		}
    }

	/**
	 * Method used to parse input form input field
	 * @param inputText - input field
	 * @param defaultInput - default value if string in inputText is invalid
	 * @param pattern - correct pattern of input
	 * @param text - StringBuilder instance to which textfield name will be appended is wrong input
	 * @return - parsed or default value
	 * @throws NumberFormatException
	 * @throws NullPointerException
	 */
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

	/**
	 * Sets pane in which pendulum will be drawed
	 * @param p - pane to dra on
	 */
	private void setPane(Pane p) {
        drawPane = p;
        for (Node i : pendulumView.getNodes())
            drawPane.getChildren().add(i);
    }

	/**
	 * Method used to start animation
	 */
	public void startAnimation() {
        pendulumAnimation.start();
    }

    /**
	 * Method used to stop animation
	 */
    public void stopAnimation() {
        pendulumAnimation.stop();
    }

	/**
	 * Set size of pane in which pendulum is drawed in pixels and in physical units
	 * @param x1 - width of pane in pixels
	 * @param y1 - heigth of pane in pixels
	 * @param x2 - width of pane [m]
	 * @param y2 - heigth of pane [m]
	 */
    public void setPaneSize(double x1, double y1, double x2, double y2) {
        pendulumView.setPaneSize(x1, y1, x2, y2);
    }
}
