package sample;

import java.util.ArrayList;
import java.util.Random;

public class GDS {
    private GDS(){}

    public static boolean running = false;
    public static double FrameTime = 1./60.;

    public static double G=1e3;
    public static int numOfBodies = 20;
    public static double m=1000.;


    public static double xreal = 900.;
    public static double yreal = 900.;


    public static ArrayList<Body> bodies = new ArrayList<>();
    public static double real_t = 0.;
    public static double t = 0.;
    public static Random rnd = new Random();
}

class Body {
    double x;               //m
    double y;               //m
    double r;               //m
    double m;               //kg
    double ax, ay;          //m/s^2
    double vx, vy;          //m/s

    public double getX()  { return x; }
    public double getY()  { return y; }
    public double getVx() { return vx;}
    public double getVy() { return vy; }

    public double getR() {
        return r;
    }
    public double getM() {
        return m;
    }

    public Body(double newX, double newY, double newR, double newM, double newVx, double newVy) {
        this.x = newX;
        this.y = newY;
        this.r = newR;
        this.m = newM;
        this.ax = this.ay = 0;
        this.vx = newVx;
        this.vy = newVy;
    }

    boolean beyondTheCanvas() {
        return Math.abs(this.getX()) > 5000 || Math.abs(this.getY()) > 5000;
    }

    void updatePosition(double dt) {
        this.vx += this.ax*dt;
        this.vy += this.ay*dt;
        this.x += this.vx*dt;
        this.y += this.vy*dt;
    }
}
