package sample;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.lang.Math;



public class Gravity {
    public Body getBody(int index) {
        return GDS.bodies.get(index);
    }

    public int getNumOfBodies() {
        return GDS.bodies.size();
    }

    public void setG(double g) {
        GDS.G = g;
    }

    public Gravity(int numOfBodies) {
        GDS.numOfBodies = numOfBodies;
        for (int i = 0; i < GDS.numOfBodies; i++){
            GDS.bodies.add(new Body(GDS.rnd.nextDouble()*1000. - 500., GDS.rnd.nextDouble()*1000. - 500.,
                                10, GDS.m,
                                   GDS.rnd.nextDouble()*500 - 250, GDS.rnd.nextDouble()*500 - 250));
        }
    }

    public void resetGravity(int numOfBodies){
        GDS.numOfBodies = numOfBodies;
        GDS.bodies.clear();
        for (int i = 0; i < GDS.numOfBodies; i++){
            GDS.bodies.add(new Body(GDS.rnd.nextDouble()*1000. - 500., GDS.rnd.nextDouble()*1000. - 500.,
                    10, GDS.m,
                    GDS.rnd.nextDouble()*500 - 250, GDS.rnd.nextDouble()*500 - 250));
        }


    }
    public void simulate(double time) {
        double dt = 1e-3;
        GDS.real_t += time;
        while (GDS.t < GDS.real_t) {
//            System.out.println(GDS.bodies.get(0).vx);
            calculateAccelerations();
            for (Body i : GDS.bodies) {
                i.updatePosition(dt);
            }
            GDS.t += dt;
        }
    }


    private void calculateAccelerations() {
        for (int i = 0; i < GDS.bodies.size(); i++) {
            GDS.bodies.get(i).ax = 0;
            GDS.bodies.get(i).ay = 0;
        }

        for (int cur = 0; cur < GDS.bodies.size(); cur++) {// for each body ...
            for (int i = 0; i < GDS.bodies.size(); i++) { // ... calculate resulting acceleration
                if (cur >= GDS.bodies.size()) continue;
                if (GDS.bodies.get(cur).beyondTheCanvas()) {
                    GDS.bodies.remove(cur);
                    continue;
                }

                if (i != cur) {  // ignore the current body
                    double dist = calcDistance(cur, i);

                    if (dist > (GDS.bodies.get(cur).r + GDS.bodies.get(i).r) ) {
                        calcNewAcceleration(cur, i, dist); // calculate new acceleration
                    } else {
//                            System.out.println(GDS.bodies.size());
                        playSound();
                        removeBodyOnCollision(cur, i);    // removes body with i index (!) if collision detected
                    }
                }
            }
        }
    }

    double calcDistance(int cur, int i) {
        double first = (GDS.bodies.get(i).x - GDS.bodies.get(cur).x) * (GDS.bodies.get(i).x - GDS.bodies.get(cur).x);
        double second = (GDS.bodies.get(i).y - GDS.bodies.get(cur).y) * (GDS.bodies.get(i).y - GDS.bodies.get(cur).y);
        return Math.sqrt(first + second);
    }

    void calcNewAcceleration(int cur, int i, double dist) {
        GDS.bodies.get(cur).ax += GDS.G * GDS.bodies.get(i).m * (GDS.bodies.get(i).x - GDS.bodies.get(cur).x) / Math.pow(dist, 3);
        GDS.bodies.get(cur).ay += GDS.G * GDS.bodies.get(i).m * (GDS.bodies.get(i).y - GDS.bodies.get(cur).y) / Math.pow(dist, 3);
    }

    void removeBodyOnCollision(int cur, int i) {
        GDS.bodies.get(cur).m += GDS.bodies.get(i).m;
        if (GDS.bodies.get(cur).r < 70 && GDS.bodies.get(i).r < 70) { // just for usability. prevents unlimited GDS.bodies' growth
            if (GDS.bodies.get(cur).r > GDS.bodies.get(i).r)
                GDS.bodies.get(cur).r += Math.pow(GDS.bodies.get(i).r, 1 / 3);
            else
                GDS.bodies.get(cur).r = GDS.bodies.get(i).r + Math.pow(GDS.bodies.get(cur).r, 1 / 3);
        }

        GDS.bodies.get(cur).vx = (GDS.bodies.get(cur).vx * GDS.bodies.get(cur).m + GDS.bodies.get(i).vx*GDS.bodies.get(i).m)/(GDS.bodies.get(i).m+GDS.bodies.get(cur).m);
        GDS.bodies.get(cur).vy = (GDS.bodies.get(cur).vy * GDS.bodies.get(cur).m + GDS.bodies.get(i).vy*GDS.bodies.get(i).m)/(GDS.bodies.get(i).m+GDS.bodies.get(cur).m);
        GDS.bodies.get(cur).x = (GDS.bodies.get(cur).x + GDS.bodies.get(i).x)/2.;
        GDS.bodies.get(cur).y = (GDS.bodies.get(cur).y + GDS.bodies.get(i).y)/2.;
        GDS.bodies.remove(i);
    }
    void playSound(){
        String file = "peww.mp3";
        Media sound = new Media(new File(file).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}
