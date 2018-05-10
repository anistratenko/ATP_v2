package sample;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.TreeMap;

public interface SimulationView {
    /**
     * @return list of elements to be drawn
     */
    ArrayList<Node> getNodes();

    void setParams(TreeMap<String, Double> TM);

    void setPaneSize(double px_x, double px_y, double re_x, double re_y);

    /**
     * @return name of the simulation
     */
    String toString();

    /**
     * @return true if nodes has changed
     */
    boolean performSimulationStep();
}

