package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;

/**
 * Created by yevhenii on 5/9/18.
 */
public class PendulumGuiController {

    @FXML
    private Button Start;

    @FXML
    private Button Type;

    @FXML
    private void onClickStart(Event event) throws Exception
    {
        PDS.running = !PDS.running;
        if ( PDS.running )Start.setText("Stop");
        else Start.setText("Start");
    }

    @FXML
    private void onClickType(Event event) throws Exception
    {
        PDS.doublependulum = !PDS.doublependulum;
        if ( PDS.doublependulum ) Type.setText("Double");
        else Type.setText("Single");
    }
}
