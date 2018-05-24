package sample;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;



public class Gravity {

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

        public void setX(double x) { this.x = x; }
        public void setY(double y) { this.y = y; }

        public void setR(double r) { this.r = r; }
        public void setM(double m) { this.m = m; }

        public void setAx(double ax) { this.ax = ax; }
        public void setAy(double ay) { this.ay = ay; }

        public void setVx(double vx) { this.vx = vx; }
        public void setVy(double vy) { this.vy = vy; }

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

    private ArrayList<Body> bodies = new ArrayList<>();
    private double G = GDS.G;
    private double real_t = 0.;
    private double t = 0.;
    private Random rnd = new Random();
    public Body getBody(int index) {
        return bodies.get(index);
    }

    public int getNumOfBodies() {
        return bodies.size();
    }

    public void setG(double g) {
        G = g;
    }

    public Gravity(int numOfBodies) {
        for (int i = 0; i < numOfBodies; i++)
        {
            bodies.add(new Body(rnd.nextDouble()*1000. - 500., rnd.nextDouble()*1000. - 500.,
                                10, GDS.m,
                                rnd.nextDouble()*500 - 250, rnd.nextDouble()*500 - 250));
        }
    }

    public void simulate(double time) {

        double dt = 1e-3;
        real_t += time;
        while (t < real_t) {
//            System.out.println(bodies.get(0).vx);
            calculateAccelerations();
            for (Body i : bodies) {
                i.updatePosition(dt);

            }
            t += dt;
        }
    }
    


    private void calculateAccelerations() {
        for (int i = 0; i < bodies.size(); i++) {
            bodies.get(i).ax = 0;
            bodies.get(i).ay = 0;
        }

        for (int cur = 0; cur < bodies.size(); cur++) {// for each body ...

             // workaround due to impossibility to remove bodies from gravityiew (applySimulation() in SimulationController executes only once)
                for (int i = 0; i < bodies.size(); i++) { // ... calculate resulting acceleration
                    if (cur >= bodies.size()) continue;
                    if (bodies.get(cur).beyondTheCanvas()) {
                        bodies.remove(cur);
                        continue;
                    }

                    if (i != cur) {  // ignore the current body
                        double dist = calcDistance(cur, i);

                        if (dist > (bodies.get(cur).r + bodies.get(i).r) ) {
                            calcNewAcceleration(cur, i, dist); // calculate new acceleration
                        } else {
                            System.out.println(bodies.size());
                            playSound();
                            removeBodyOnCollision(cur, i);    // removes body with i index (!) if collision detected
                        }
                    }
                }
        }
    }

    double calcDistance(int cur, int i) {

        double first = (bodies.get(i).x - bodies.get(cur).x) * (bodies.get(i).x - bodies.get(cur).x);
        double second = (bodies.get(i).y - bodies.get(cur).y) * (bodies.get(i).y - bodies.get(cur).y);
        return Math.sqrt(first + second);
    }

    void calcNewAcceleration(int cur, int i, double dist) {
        bodies.get(cur).ax += G * bodies.get(i).m * (bodies.get(i).x - bodies.get(cur).x) / Math.pow(dist, 3);
        bodies.get(cur).ay += G * bodies.get(i).m * (bodies.get(i).y - bodies.get(cur).y) / Math.pow(dist, 3);
    }

    void removeBodyOnCollision(int cur, int i) {
        bodies.get(cur).m += bodies.get(i).m;
        if (bodies.get(cur).r < 70 && bodies.get(i).r < 70) { // just for usability. prevents unlimited bodies' growth
            if (bodies.get(cur).r > bodies.get(i).r)
                bodies.get(cur).r += Math.pow(bodies.get(i).r, 1 / 3);
            else
                bodies.get(cur).r = bodies.get(i).r + Math.pow(bodies.get(cur).r, 1 / 3);
        }

        bodies.get(cur).vx = (bodies.get(cur).vx * bodies.get(cur).m + bodies.get(i).vx*bodies.get(i).m)/(bodies.get(i).m+bodies.get(cur).m);
        bodies.get(cur).vy = (bodies.get(cur).vy * bodies.get(cur).m + bodies.get(i).vy*bodies.get(i).m)/(bodies.get(i).m+bodies.get(cur).m);
        bodies.get(cur).x = (bodies.get(cur).x + bodies.get(i).x)/2.;
        bodies.get(cur).y = (bodies.get(cur).y + bodies.get(i).y)/2.;
        bodies.remove(i);
    }
    void playSound(){
//        String file = "peww.mp3";     // For example
//        Media sound = new Media(new File(file).toURI().toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(sound);
//        mediaPlayer.play();

    }
}
