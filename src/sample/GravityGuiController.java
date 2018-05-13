package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

/**
 * Created by yevhenii on 5/8/18.
 */
public class GravityGuiController {

    @FXML
    private Button gravityButton;

    @FXML
    private Button Start;

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

}
