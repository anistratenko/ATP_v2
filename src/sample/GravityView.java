package sample;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;


public class GravityView implements SimulationView{
    public int getNumElements() {
        return elements.size();
    }

    private ArrayList<Node> elements = new ArrayList<>();
    private Node body;
    private Gravity gravity;
    int k = 0;
    private static int[] offset = {250, 250};
    public static double xsize ;
    public static double ysize;
    public static double xreal;
    public static double yreal;
    public static double scale;
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
        gravity.setParams(10);
    }

    @Override
    public boolean performSimulationStep() {

        gravity.simulate(GDS.FrameTime);
        double xcenter = xsize/2.;
        double ycenter = ysize/2.;
        double xscale = xsize/xreal;
        double yscale = ysize/yreal;
        scale = xscale;
        double scale = Math.min(xscale, yscale);
        if (elements.size() != gravity.getNumOfBodies()){
            elements.clear();
            for (int i = 0; i < gravity.getNumOfBodies(); i++){
                Circle newCircle =  new Circle(xcenter + gravity.getBody(i).getX() * scale,
                                               ycenter + gravity.getBody(i).getY() * scale,
                                                                 gravity.getBody(i).getR());
                newCircle.setFill(Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
                elements.add(newCircle);

            }
            return true;
        } else
        for (int i = 0; i < elements.size(); i++){
            ((Circle)elements.get(i)).setCenterX(xcenter  + gravity.getBody(i).getX() * scale);
            ((Circle)elements.get(i)).setCenterY(ycenter  + gravity.getBody(i).getY() * scale);
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
