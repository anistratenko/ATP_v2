package sample;

public class GDS {
    private GDS(){}
    static public boolean running = true;
    public static double FrameTime = 1./60;//s

    public static double G = 6.67e-11   ;             //m^3/(kg*s^2)
    public static int num_of_bodies = 20;
    public static double m = 2e16;

    public static double xreal = 1000;
    public static double yreal = 1000;
}
