package pl.edu.agh.fis.anistratenko_team_project.Pendulum;

public class PDS {
    public PDS() {
    }
    public double FrameTime = 1. / 60;//s

    public double fx = 0.;
    public double fx_when_control = 0.;
    public double c = 0.;


    public boolean running = false;

    public boolean doublependulum = true;

    public double xreal = 1.;
    public double yreal = 1.;


    //variables for simulation
    public double phi = Math.PI / 2;
    public double theta = 0.;
    public double d_phi = 0.;
    public double d_theta = 0.;
    public double d2_phi = 0.;
    public double d2_theta = 0.;
    public double l1 = 0.;
    public double l2 = 0.;
    public double m1 = 0.;
    public double m2 = 0.;
    public double g = -9.81;
    public double real_t = 0.;
    public double t = 0.;

    //loaded variables for restert
    public double loaded_phi = Math.PI / 2;
    public double loaded_theta = 0.;
    public double loaded_l1 = 0.;
    public double loaded_l2 = 0.;
    public double loaded_m1 = 0.;
    public double loaded_m2 = 0.;
    public double loaded_g = -9.81;
    public double loaded_fx_when_control = 0.;
    public double loaded_c = 0.;

    //public variables x1, y1 - coordinates of 1st pendulum end, x2, y2 - 2nd pendulum or x2==x1, y2==y1 if DP equals false
    //coordinate system origin is located in pendulum pivot
    //modification of these values won't affect simulate function
    public double x1;
    public double y1;
    public double x2;
    public double y2;
}
