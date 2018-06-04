package pl.edu.agh.fis.anistratenko_team_project.Pendulum;

public class Pendulum {
    private PDS pDS;

    public void setXY(double X1, double Y1) {
        setXY(X1, Y1, X1, Y1);
    }

    public void setLMF(double L1, double M1, double PHI) {
        setLMF(L1, M1, PHI, L1, 0, 0);
    }

    public void setLMF(double L1, double M1, double PHI, double L2, double M2, double THETA) {
        pDS.l1 = L1;
        pDS.m1 = M1;
        pDS.phi = PHI;

        pDS.l2 = L2;
        pDS.m2 = M2;
        pDS.theta = THETA;

        pDS.d_phi = 0.;
        pDS.d2_phi = 0.;
        pDS.d_theta = 0.;
        pDS.d2_theta = 0.;

        pDS.real_t = 0.;
        pDS.t = 0.;
        //Code repetition:
        //in this function and in 'setXY' assigning 0 to multiple fields of pDS is repeated, it could be extracted.
    }

    public void setXY(double X1, double Y1, double X2, double Y2) {
        //Double operations are safe, because overflow evaluates to Inf. statement ( 1.0/0.0 )
        pDS.l1 = Math.sqrt(X1 * X1 + Y1 * Y1);
        pDS.phi = Math.acos(X1 / pDS.l1);
        if (Y1 >= 0) {
            pDS.phi += Math.PI / 2.;
        } else {
            pDS.phi = Math.PI / 2. - pDS.phi;
        }

        if (pDS.doublependulum) {
            pDS.l2 = Math.sqrt(((X2 - X1) * (X2 - X1)) + ((Y2 - Y1) * (Y2 - Y1)));
            double arg = (X2 - X1) / pDS.l2;
            if (arg > 1)
                arg = 1.0;
            if (arg < -1)
                arg = -1.0;
            pDS.theta = Math.acos(arg);
            if (Y2 >= Y1) {
                pDS.theta += Math.PI / 2.;
            } else {
                pDS.theta = Math.PI / 2. - pDS.theta;
            }
            pDS.d_theta = 0;
            pDS.d2_theta = 0;
        }

        pDS.d_phi = 0;
        pDS.d2_phi = 0;
        pDS.d_theta = 0;
        pDS.d2_theta = 0;
        pDS.real_t = 0.;
        pDS.t = 0.;
    }

    //constructor for double pendulum
    public Pendulum(double l_1, double m_1, double l_2, double m_2, PDS pds) {
    	pDS = pds;
        pDS.l1 = l_1;
        pDS.m1 = m_1;
        if (pDS.doublependulum) {
            pDS.l2 = l_2;
            pDS.m2 = m_2;
        } else {
            pDS.l2 = 10; // magic number should be replaced by define ( why 10 ?  did you mean 0 ?)
            pDS.m2 = 0;
        }
    }

    //function for standard simulation, time >= 0
    public void simulate(double time) {
        simulate(time, 0., 0.55);
    }
    // 'dragCooefficient' parameter of method simulate is magic number. It could be defined in PDS
    // 'force' is also one of the parameters that could change during the simulation, therefore it could fit in PDS.
    // This could decrease number of arguments in function 'simulate', and it would make function overloading unnecessary

    //function for simulation with additional horizontal force, works only for single pendulum, time >= 0, force [m/s^2] in (-inf;inf)
    public void simulate(double time, double force, double dragCooefficient) {
        //internal time-step for computations
        double dt = 5e-6;
        //time measuring
        pDS.real_t += time;
        if (pDS.doublependulum) {
            while (pDS.t < pDS.real_t) {
                pDS.d2_phi = (
                        -pDS.m2 * pDS.l1 * pDS.d_theta * pDS.d_theta * Math.sin(pDS.phi - pDS.theta) * Math.cos(pDS.phi - pDS.theta)
                                - pDS.g * pDS.m2 * Math.sin(pDS.theta) * Math.cos(pDS.phi - pDS.theta)
                                - pDS.m2 * pDS.l2 * pDS.d_theta * pDS.d_theta * Math.sin(pDS.phi - pDS.theta)
                                + (pDS.m1 + pDS.m2) * pDS.g * Math.sin(pDS.phi)
                )
                        /
                        (
                                pDS.l1 * (pDS.m1 + pDS.m2) - pDS.m2 * pDS.l1 * Math.cos(pDS.phi - pDS.theta) * Math.cos(pDS.phi - pDS.theta)
                        );

                pDS.d2_theta = (pDS.m2 * pDS.l2 * pDS.d_theta * pDS.d_theta * Math.sin(pDS.phi - pDS.theta) * Math.cos(pDS.phi - pDS.theta)
                        - pDS.g * Math.sin(pDS.phi) * Math.cos(pDS.phi - pDS.theta) * (pDS.m1 + pDS.m2)
                        + pDS.l1 * pDS.d_phi * pDS.d_phi * Math.sin(pDS.phi - pDS.theta) * (pDS.m1 + pDS.m2)
                        + pDS.g * Math.sin(pDS.theta) * (pDS.m1 + pDS.m2)
                )
                        /
                        (
                                pDS.l2 * (pDS.m1 + pDS.m2) - pDS.m2 * pDS.l2 * Math.cos(pDS.phi - pDS.theta) * Math.cos(pDS.phi - pDS.theta)
                        );

                pDS.d_phi += pDS.d2_phi * dt;
                pDS.d_theta += pDS.d2_theta * dt;
                pDS.phi += pDS.d_phi * dt;
                pDS.theta += pDS.d_theta * dt;
                pDS.t += dt;
            }
        } else {
            double ro = 1000; //[kg/m^3] - water density
            double r = Math.cbrt((pDS.m1 / (Math.PI * 4000.)) * (3. / 4.));
            double SD = 2 * Math.PI * r * r;
            double v = 0;
            while (pDS.t < pDS.real_t) {
                v = pDS.d_phi * r;
                pDS.d2_phi = (pDS.g * Math.sin(pDS.phi) + force * Math.cos(pDS.phi) - Math.signum(pDS.d_phi) * dragCooefficient * SD * ro * v * v / 2.) / pDS.l1;
                //pDS.d2_phi = (-pDS.g * Math.sin(phi) + force * Math.cos(phi))/pDS.l1;
                pDS.d_phi += pDS.d2_phi * dt;
                pDS.phi += pDS.d_phi * dt;
                pDS.t += dt;
            }
        }
        definePositions();
    }

    private void definePositions() {
        pDS.x1 = pDS.l1 * Math.sin(pDS.phi);
        pDS.y1 = -pDS.l1 * Math.cos(pDS.phi);
        if (pDS.doublependulum) {
            pDS.x2 = pDS.x1 + pDS.l2 * Math.sin(pDS.theta);
            pDS.y2 = pDS.y1 - pDS.l2 * Math.cos(pDS.theta);
        } else {
            pDS.x2 = pDS.x1;
            pDS.y2 = pDS.y1;
        }
    }

    /*public static void main(String[] args) {
        Pendulum p = new Pendulum(0.25, 1, 0.3, 2);

        Long start = System.nanoTime();
        for (int i = 0; i < 600; i++) p.simulate(1. / 60.);
        Long end = System.nanoTime();

        System.out.println("Computation time per 1 second simulated: " + ((end - start) * 1e-9 / 10.));
        System.out.println("Computation time of 1/60 sec: 			 " + ((end - start) * 1e-9 / 600.));
    }*/
}