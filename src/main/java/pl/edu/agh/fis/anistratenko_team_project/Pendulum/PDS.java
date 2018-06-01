package pl.edu.agh.fis.anistratenko_team_project.Pendulum;

public class PDS {
    private PDS() {
    }

    public static double FrameTime = 1. / 60;//s

//    static public double g = -9.81;

//    static public double l1 = .15;
//    static public double m1 = .1;
//    static public double phi = Math.PI/2.;
//
//    static public double l2 = .10;
//    static public double m2 = .05;
//    static public double theta = Math.PI/2.;

    static public double fx = 0.;
    static public double c = 0.;

    static public boolean running = false;

    static public boolean doublependulum = true;

    static public double xreal = 1.;
    static public double yreal = 1.;


    //variables for simulation
    static public double phi = Math.PI / 2;
    static public double theta = 0.;
    static public double d_phi = 0.;
    static public double d_theta = 0.;
    static public double d2_phi = 0.;
    static public double d2_theta = 0.;
    static public double l1 = 0.;
    static public double l2 = 0.;
    static public double m1 = 0.;
    static public double m2 = 0.;
    static public double g = -9.81;
    static public double real_t = 0.;
    static public double t = 0.;

    //public variables x1, y1 - coordinates of 1st pendulum end, x2, y2 - 2nd pendulum or x2==x1, y2==y1 if DP equals false
    //coordinate system origin is located in pendulum pivot
    //modification of these values won't affect simulate function
    static public double x1;
    static public double y1;
    static public double x2;
    static public double y2;
}
