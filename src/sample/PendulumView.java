package sample;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.TreeMap;

public class PendulumView implements SimulationView {
    private ArrayList<Node> elements = new ArrayList<>();
    private Circle firstBob, secondBob;
    private Line firstCord, secondCord;
    private Pendulum pendulum;
    private static int[] offset = {250, 250};
    private double xsize;
    private double ysize;
    /**
     * Constructor for single pendulum
     *
     * @param length1 - length of the arm
     * @param mass1   - mass of the pendulum
     */

//    public PendulumView(double length1, double mass1) {
//        pendulum = new Pendulum(length1, mass1);
//        pendulum.setLMF(PDS.l1, PDS.m1, PDS.phi);
//        firstBob = new Circle(300., 270., 10.);
//        secondBob = firstBob;
//        elements.add(firstBob);
//        firstCord = new Line(250, 250, firstBob.getCenterX(), firstBob.getCenterY());
//        secondCord = new Line (250, 250, 250, 250);
//        elements.add(firstCord);
//    }

    /**
     * Double pendulum
     *
     * @param length1 - length of first pendulum
     * @param mass1   - mass of first pendulum
     * @param length2 - length of second pendulum ( which is attached to the previous one )
     * @param mass2   - mass of second pendulum
     */
    public PendulumView(double length1, double mass1, double length2, double mass2) {
        pendulum = new Pendulum(length1, mass1, length2, mass2);
        pendulum.setLMF(PDS.l1, PDS.m1, PDS.phi, PDS.l2, PDS.m2, PDS.theta);
        firstBob = new Circle(300., 270., 10.);
        secondBob = new Circle(400., 270., 5.);
        elements.add(firstBob);
        elements.add(secondBob);
//        pendulum.setXY((firstBob.getCenterX() - 250.) / 250.,
//                (firstBob.getCenterY() - 250.) / 250.,
//                (secondBob.getCenterX() - 250.) / 250.,
//                (secondBob.getCenterY() - 250.) / 250.);
        firstCord = new Line(offset[0], offset[1], firstBob.getCenterX(), firstBob.getCenterY());
        secondCord = new Line(firstBob.getCenterX(), firstBob.getCenterY(), secondBob.getCenterX(), secondBob.getCenterY());
        elements.add(firstCord);
        elements.add(secondCord);
    }

    /**
     * Run simulation and move positions of pendulums
     */
    public boolean performSimulationStep() {
        pendulum.simulate(PDS.FrameTime, PDS.fx, PDS.c);
        refresh();
        return false;
    }

    public void refresh()
    {
        double xcenter = xsize/2;
        double ycenter = ysize/2;
        double xscale = xsize/PDS.xreal;
        double yscale = ysize/PDS.yreal;
        double scale = Math.min(xscale, yscale);
        firstBob.setCenterX(xcenter + PDS.x1 * scale);
        firstBob.setCenterY(ycenter - PDS.y1 * scale);
        secondBob.setCenterX(xcenter + PDS.x2 * scale);
        secondBob.setCenterY(ycenter - PDS.y2 * scale);
        firstCord.setStartX(xcenter);
        firstCord.setStartY(ycenter);
        firstCord.setEndX(xcenter + PDS.x1 * scale);
        firstCord.setEndY(ycenter - PDS.y1 * scale);
        secondCord.setStartX(xcenter + PDS.x1 * scale);
        secondCord.setStartY(ycenter - PDS.y1 * scale);
        secondCord.setEndX(xcenter + PDS.x2 * scale);
        secondCord.setEndY(ycenter - PDS.y2 * scale);
    }

    @Override
    public void setPaneSize(double px_x, double px_y, double re_x, double re_y)
    {
        xsize = px_x;
        ysize = px_y;
        PDS.xreal = re_x;
        PDS.yreal = re_y;
    }

    public Circle getFirstBob() {
        return firstBob;
    }

    public Circle getSecondBob() {
        return secondBob;
    }

    public Line getFirstCord() {
        return firstCord;
    }

    public Line getSecondCord() {
        return secondCord;
    }

    public Pendulum getPendulum() {
        return pendulum;
    }

    /**
     * @return elements to be drawn
     */
    @Override
    public ArrayList<Node> getNodes() {
        return elements;
    }

    @Override
    public String toString() {
        return "Pendulum";
    }
}
