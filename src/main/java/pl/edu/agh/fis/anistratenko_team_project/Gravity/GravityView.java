package pl.edu.agh.fis.anistratenko_team_project.Gravity;

import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import pl.edu.agh.fis.anistratenko_team_project.Application.SimulationView;

import java.util.ArrayList;
import java.util.Random;


public class GravityView implements SimulationView {
    String externalForm = getClass().getResource("/sound/peww.mp3").toExternalForm();
    Media sound = new Media(externalForm);
//    MediaPlayer mediaPlayer = new MediaPlayer(sound);
    public int getNumElements() {
        return elements.size();
    }

    private ArrayList<Node> elements = new ArrayList<>();
    private Node body;
    private Gravity gravity;
    int k = 0;
    private static int[] offset = {250, 250};
    public static double xsize;
    public static double ysize;
    public static double xreal;
    public static double yreal;
    private static Random rnd = new Random();
    private GDS gDS;

    public GravityView(int numOfBodies, GDS gds) {
        gDS = gds;
        gravity = new Gravity(numOfBodies, gDS);
        for (int i = 0; i < numOfBodies; i++) {
            elements.add(new Circle(gravity.getBody(i).getX(),
                    gravity.getBody(i).getY(),
                    gravity.getBody(i).getR()));
        }
    }

    @Override
    public void setPaneSize(double px_x, double px_y, double re_x, double re_y) {
        xsize = px_x;
        ysize = px_y;
        xreal = re_x;
        yreal = re_y;
    }

    @Override
    public ArrayList<Node> getNodes() {
        return elements;
    }

    public boolean refresh() {
        double xcenter = xsize / 2.;
        double ycenter = ysize / 2.;
        double xscale = xsize / xreal;
        double yscale = ysize / yreal;
        double scale = Math.min(xscale, yscale);
        if (elements.size() != gravity.getNumOfBodies()) {
            elements.clear();
//            playSound();
            for (int i = 0; i < gravity.getNumOfBodies(); i++){
                Circle newCircle =  new Circle(xcenter + gravity.getBody(i).getX() * scale,
                                                ycenter + gravity.getBody(i).getY() * scale,
                        						gravity.getBody(i).getR());
                if (gravity.getBody(i).isBlackHole){
//                    newCircle.setCenterX(gravity.getBody(i).getX());
//                    newCircle.setCenterY(gravity.getBody(i).getY());
                    newCircle.setFill(Color.rgb(0, 0,0));
                    System.out.println("BHV: " + newCircle.getCenterX() + " " + newCircle.getCenterY());
                }
                else
                    newCircle.setFill(Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
                elements.add(newCircle);
            }
            return true;
        } else
            for (int i = 0; i < elements.size(); i++) {
//                if(!gravity.getBody(i).isBlackHole) {
                    ((Circle) elements.get(i)).setCenterX(xcenter + gravity.getBody(i).getX() * scale);
                    ((Circle) elements.get(i)).setCenterY(ycenter + gravity.getBody(i).getY() * scale);
                    ((Circle) elements.get(i)).setRadius(gravity.getBody(i).getR());
//                }
//                else{
//                    ((Circle) elements.get(i)).setCenterX(gravity.getBody(i).getX());
//                    ((Circle) elements.get(i)).setCenterY(gravity.getBody(i).getY());
//                    ((Circle) elements.get(i)).setRadius(gravity.getBody(i).getR());
//                }
            }
        return false;
    }

    @Override
    public boolean performSimulationStep() {
        gravity.simulate(gDS.FrameTime);
        return refresh();
    }

    public static void setOffsetWidth(int x) {
        offset[0] = x / 2;
    }

    public static void setOffsetHeight(int y) {
        offset[1] = y / 2;
    }

    public void resetGravityView(int numOfBodies) {
        gravity.resetGravity(numOfBodies);
    }

    void playSound(){
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    void addBlackHoleView(double x, double y){
        double xcenter = xsize / 2.;
        double ycenter = ysize / 2.;
        double xscale = xsize / xreal;
        double yscale = ysize / yreal;
        double scale = Math.min(xscale, yscale);
        double properX = (x  - xcenter) / scale ;
        double properY = (y  - ycenter - 60 ) / scale ;
        gravity.addBlackHole(properX, properY);
    }
}
