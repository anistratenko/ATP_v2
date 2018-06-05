package pl.edu.agh.fis.anistratenko_team_project.Gravity;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import pl.edu.agh.fis.anistratenko_team_project.Pendulum.PDS;

public class Gravity {
    private GDS gDS;
    public Body getBody(int index) {
        return gDS.bodies.get(index);
    }

    public int getNumOfBodies() {
        return gDS.bodies.size();
    }

    public void setG(double g) {
        gDS.G = g;
    }

    public Gravity(int numOfBodies, GDS gds) {
        gDS = gds;
        gDS.numOfBodies = numOfBodies;
        for (int i = 0; i < gDS.numOfBodies; i++) {
            gDS.bodies.add(new Body(gDS.rnd.nextDouble() * 1000. - 500., gDS.rnd.nextDouble() * 1000. - 500.,
                    10, gDS.m,
                    gDS.rnd.nextDouble() * 500 - 250, gDS.rnd.nextDouble() * 500 - 250));
        }
    }

    public void resetGravity(int numOfBodies) {
        gDS.numOfBodies = numOfBodies;
        gDS.bodies.clear();
        for (int i = 0; i < gDS.numOfBodies; i++) {
            gDS.bodies.add(new Body(gDS.rnd.nextDouble() * 1000. - 500., gDS.rnd.nextDouble() * 1000. - 500.,
                    10, gDS.m,
                    gDS.rnd.nextDouble() * 500 - 250, gDS.rnd.nextDouble() * 500 - 250));
        }


    }

    public void simulate(double time) {
        double dt = 1e-3;
        gDS.real_t += time;
        while (gDS.t < gDS.real_t) {
//            System.out.println(gDS.bodies.get(0).vx);
            calculateAccelerations();
            for (Body i : gDS.bodies) {
                i.updatePosition(dt);
            }
            gDS.t += dt;
        }
    }


    private void calculateAccelerations() {
        for (int i = 0; i < gDS.bodies.size(); i++) {
            gDS.bodies.get(i).ax = 0;
            gDS.bodies.get(i).ay = 0;
        }

        for (int cur = 0; cur < gDS.bodies.size(); cur++) {// for each body ...
            for (int i = 0; i < gDS.bodies.size(); i++) { // ... calculate resulting acceleration
                if (cur >= gDS.bodies.size()) continue;
                if (gDS.bodies.get(cur).beyondTheCanvas()) {
                    gDS.bodies.remove(cur);
                    continue;
                }

                if (i != cur) {  // ignore the current body
                    double dist = calcDistance(cur, i);

                    if (dist > (gDS.bodies.get(cur).r + gDS.bodies.get(i).r)) {
                        calcNewAcceleration(cur, i, dist); // calculate new acceleration
                    } else {
//                            System.out.println(gDS.bodies.size());
                        removeBodyOnCollision(cur, i);    // removes body with i index (!) if collision detected
                    }
                }
            }
        }
    }

    double calcDistance(int cur, int i) {
        double first = (gDS.bodies.get(i).x - gDS.bodies.get(cur).x) * (gDS.bodies.get(i).x - gDS.bodies.get(cur).x);
        double second = (gDS.bodies.get(i).y - gDS.bodies.get(cur).y) * (gDS.bodies.get(i).y - gDS.bodies.get(cur).y);
        return Math.sqrt(first + second);
    }

    void calcNewAcceleration(int cur, int i, double dist) {
        gDS.bodies.get(cur).ax += gDS.G * gDS.bodies.get(i).m * (gDS.bodies.get(i).x - gDS.bodies.get(cur).x) / Math.pow(dist, 3);
        gDS.bodies.get(cur).ay += gDS.G * gDS.bodies.get(i).m * (gDS.bodies.get(i).y - gDS.bodies.get(cur).y) / Math.pow(dist, 3);
    }

    void removeBodyOnCollision(int cur, int i) {
        gDS.bodies.get(cur).m += gDS.bodies.get(i).m;
        if (gDS.bodies.get(cur).r < 70 && gDS.bodies.get(i).r < 70) { // just for usability. prevents unlimited gDS.bodies' growth
            if (gDS.bodies.get(cur).r > gDS.bodies.get(i).r)
                gDS.bodies.get(cur).r += Math.pow(gDS.bodies.get(i).r, 1 / 3);
            else
                gDS.bodies.get(cur).r = gDS.bodies.get(i).r + Math.pow(gDS.bodies.get(cur).r, 1 / 3);
        }

        gDS.bodies.get(cur).vx = (gDS.bodies.get(cur).vx * gDS.bodies.get(cur).m + gDS.bodies.get(i).vx * gDS.bodies.get(i).m) / (gDS.bodies.get(i).m + gDS.bodies.get(cur).m);
        gDS.bodies.get(cur).vy = (gDS.bodies.get(cur).vy * gDS.bodies.get(cur).m + gDS.bodies.get(i).vy * gDS.bodies.get(i).m) / (gDS.bodies.get(i).m + gDS.bodies.get(cur).m);
        gDS.bodies.get(cur).x = (gDS.bodies.get(cur).x + gDS.bodies.get(i).x) / 2.;
        gDS.bodies.get(cur).y = (gDS.bodies.get(cur).y + gDS.bodies.get(i).y) / 2.;
        gDS.bodies.remove(i);
    }

}
