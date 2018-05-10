package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Created by yevhenii on 5/9/18.
 */
public class PendulumGuiController {

    @FXML
    private Button pendulumButton;

    @FXML
    private void onClickPendulum(Event event) throws Exception{
        System.out.println("CLICK Pendulum");
    }
}
