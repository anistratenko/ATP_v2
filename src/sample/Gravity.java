package sample;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;



public class Gravity {

    class Body {
        double x;
        double y;
        double r;
        double m;
        double ax, ay, vx, vy;
        boolean active;

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

        public Body(int newX, int newY, int newR, int newM, int newVx, int newVy) {
            this.x = newX;
            this.y = newY;
            this.r = newR;
            this.m = newM;
            this.ax = this.ay = 0;
            this.vx = newVx;
            this.vy = newVy;
            this.active = true; // workaround. defines if body was removed(false) or wasn't(true)
        }

        boolean beyondTheCanvas() {
            return Math.abs(this.getX()) > 1000 || Math.abs(this.getY()) > 1000;
        }

        void updatePosition() {
            this.x += this.vx*0.2;
            this.y += this.vy*0.2;
            this.vx += this.ax*0.2;
            this.vy += this.ay*0.2;
        }

    }

    private ArrayList<Body> bodies = new ArrayList<>();
    private double G = 2;
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
//        Random rnd = new Random();
        for (int i = 0; i < numOfBodies; i++) {
            bodies.add(new Body(rnd.nextInt(300)*1, rnd.nextInt(300)*1,
                                10, 10,
                                (rnd.nextInt(10) - 5)*5, (rnd.nextInt(10) - 5)*5));
        }
    }

    public void setParams(int numOfBodies) {
        real_t = 0.;
        t = 0.;
        bodies.clear();
        for (int i = 0; i < numOfBodies; i++) {
            bodies.add(new Body(rnd.nextInt(600),
                    rnd.nextInt(500), 10, 10,  (rnd.nextInt(10) - 5) , (rnd.nextInt(10) - 5)));
        }
    }

    public void simulate(double time) {
        double dt = 0.008;
        real_t += time;
        while (t < real_t) {
            calculateAccelerations();
            for (Body i : bodies) {
                i.updatePosition();
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

            if (bodies.get(cur).active) { // workaround due to impossibility to remove bodies from gravityiew (applySimulation() in SimulationController executes only once)
                for (int i = 0; i < bodies.size(); i++) { // ... calculate resulting acceleration
                    if (cur >= bodies.size()) continue;
                    if (bodies.get(cur).beyondTheCanvas()) {
                        bodies.remove(cur);
//                        bodies.get(cur).active = false;
//                        bodies.get(cur).r = 0;
//                        bodies.get(cur).m = 0;
                        continue;
                    }

                    if (i != cur && bodies.get(i).active) {  // ignore the current body
                        double dist = calcDistance(cur, i);

                        if (dist > bodies.get(cur).r) {
                            calcNewAcceleration(cur, i, dist); // calculate new acceleration
                        } else {
                            removeBodyOnCollision(cur, i);    // removes body with i index (!) if collision detected
                        }
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
        bodies.get(cur).ax += G * bodies.get(i).m * (bodies.get(i).x - bodies.get(cur).x) / Math.pow(dist, 2);
        bodies.get(cur).ay += G * bodies.get(i).m * (bodies.get(i).y - bodies.get(cur).y) / Math.pow(dist, 2);
    }

    void removeBodyOnCollision(int cur, int i) {
        bodies.get(cur).m += bodies.get(i).m;
        if (bodies.get(cur).r < 70 && bodies.get(i).r < 70) { // just for usability. prevents unlimited bodies' growth
            if (bodies.get(cur).r > bodies.get(i).r)
                bodies.get(cur).r += Math.pow(bodies.get(i).r, 1 / 3);
            else
                bodies.get(cur).r = bodies.get(i).r + Math.pow(bodies.get(cur).r, 1 / 3);
        }

//        bodies.get(cur).ax = 0.0;
//        bodies.get(cur).ay = 0.0; // probably the momentum preserving law will be added.
        bodies.get(cur).vx = 0.0;
        bodies.get(cur).vy = 0.0;
//        bodies.get(i).active = false;
//        bodies.get(i).r = 0;
//        bodies.get(i).m = 0;
        bodies.remove(i);
    }


}
