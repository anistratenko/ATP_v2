package pl.edu.agh.fis.anistratenko_team_project.Pendulum;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import pl.edu.agh.fis.anistratenko_team_project.Application.SimulationView;

import java.util.ArrayList;

public class PendulumView implements SimulationView {
    private ArrayList<Node> elements = new ArrayList<>();
    private Circle firstBob, secondBob;
    private Line firstCord, secondCord;
    private Pendulum pendulum;
    private static int[] offset = {250, 250};
    private double xsize;
    private double ysize;
    private PDS pDS;
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
    public PendulumView(double length1, double mass1, double length2, double mass2, PDS pds) {
        pDS = pds;
        pendulum = new Pendulum(length1, mass1, length2, mass2, pDS);
        pendulum.setLMF(pDS.l1, pDS.m1, pDS.phi, pDS.l2, pDS.m2, pDS.theta);
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
        pendulum.simulate(pDS.FrameTime, pDS.fx, pDS.c);
        refresh();
        return false;
    }

    public void refresh() {
        double xcenter = xsize / 2;
        double ycenter = ysize / 2;
        double xscale = xsize / pDS.xreal;
        double yscale = ysize / pDS.yreal;
        double scale = Math.min(xscale, yscale);
        firstBob.setCenterX(xcenter + pDS.x1 * scale);
        firstBob.setCenterY(ycenter - pDS.y1 * scale);
        secondBob.setCenterX(xcenter + pDS.x2 * scale);
        secondBob.setCenterY(ycenter - pDS.y2 * scale);
        firstCord.setStartX(xcenter);
        firstCord.setStartY(ycenter);
        firstCord.setEndX(xcenter + pDS.x1 * scale);
        firstCord.setEndY(ycenter - pDS.y1 * scale);
        secondCord.setStartX(xcenter + pDS.x1 * scale);
        secondCord.setStartY(ycenter - pDS.y1 * scale);
        secondCord.setEndX(xcenter + pDS.x2 * scale);
        secondCord.setEndY(ycenter - pDS.y2 * scale);
    }

    @Override
    public void setPaneSize(double px_x, double px_y, double re_x, double re_y) {
        xsize = px_x;
        ysize = px_y;
        pDS.xreal = re_x;
        pDS.yreal = re_y;
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
