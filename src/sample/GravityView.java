package sample;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.TreeMap;


public class GravityView implements SimulationView{
    public static final double FRAMERATE = 1 / 60.;
    private ArrayList<Node> elements = new ArrayList<>();
    private Node body;
    private Gravity gravity;
    int k = 0;
    private static int[] offset = {250, 250};
    private double xsize;
    private double ysize;
    private double xreal;
    private double yreal;

    public GravityView(int numOfBodies){
        gravity = new Gravity(numOfBodies);
        for (int i = 0; i < numOfBodies; i++){
            elements.add(new Circle(gravity.getBody(i).getX(),
                                    gravity.getBody(i).getY(),
                                    gravity.getBody(i).getR()));
        }
    }

    @Override
    public void setPaneSize(double px_x, double px_y, double re_x, double re_y)
    {
        xsize = px_x;
        ysize = px_y;
        xreal = re_x;
        yreal = re_y;
    }

    @Override
    public ArrayList<Node> getNodes() {
        return elements;
    }

    @Override
    public void setParams(TreeMap<String, Double> TM) {
        gravity.setParams(10);
    }

    @Override
    public boolean performSimulationStep() {
        gravity.simulate(FRAMERATE);
        if (elements.size() != gravity.getNumOfBodies()){
            elements.clear();
            for (int i = 0; i < gravity.getNumOfBodies(); i++){
                elements.add(new Circle(gravity.getBody(i).getX() + offset[0],offset[1] +  gravity.getBody(i).getY(), gravity.getBody(i).getR()));
            }
            return true;
        } else
        for (int i = 0; i < elements.size(); i++){
            ((Circle)elements.get(i)).setCenterX(offset[0]  + gravity.getBody(i).getX());
            ((Circle)elements.get(i)).setCenterY(offset[1]  + gravity.getBody(i).getY());
            ((Circle)elements.get(i)).setRadius(gravity.getBody(i).getR());
        }
        return false;
    }

    public static void setOffsetWidth(int x) {
        offset[0] = x/2;
    }
    public static void setOffsetHeight(int y) {
        offset[1] = y/2;
    }

}
