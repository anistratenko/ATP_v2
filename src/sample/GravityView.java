package sample;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;


public class GravityView implements SimulationView{
    private ArrayList<Node> elements = new ArrayList<>();
    private Node body;
    private Gravity gravity;
    int k = 0;
    private static int[] offset = {250, 250};
    private double xsize;
    private double ysize;
    private double xreal;
    private double yreal;
    private static Random rnd = new Random();

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
    }

    @Override
    public boolean performSimulationStep() {
        gravity.simulate(GDS.FrameTime);
        double xcenter = xsize/2;
        double ycenter = ysize/2;
        double xscale = xsize/xreal;
        double yscale = ysize/yreal;
        double scale = Math.min(xscale, yscale);
        if (elements.size() != gravity.getNumOfBodies()){
            elements.clear();
            for (int i = 0; i < gravity.getNumOfBodies(); i++){

                Circle newCircle =  new Circle(gravity.getBody(i).getX()*scale +xcenter ,  gravity.getBody(i).getY()*scale - xcenter, gravity.getBody(i).getR()*scale);
                newCircle.setFill(Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
                elements.add(newCircle);

            }
            return true;
        } else
        for (int i = 0; i < elements.size(); i++){
            ((Circle)elements.get(i)).setCenterX(xcenter  + gravity.getBody(i).getX()*scale);
            ((Circle)elements.get(i)).setCenterY(ycenter  + gravity.getBody(i).getY()*scale);
            ((Circle)elements.get(i)).setRadius(gravity.getBody(i).getR()*scale);
        }
        return false;
    }
}
