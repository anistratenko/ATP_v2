package sample;

public class Pendulum {


    public void setXY(double X1, double Y1){
        setXY(X1, Y1, X1, Y1);
    }
    public void setLMF(double L1, double M1, double PHI) { setLMF(L1, M1, PHI, L1, 0, 0 );}
    public void setLMF(double L1, double M1, double PHI, double L2, double M2, double THETA)
    {
        PDS.l1 = L1;
        PDS.m1 = M1;
        PDS.phi = PHI;

        PDS.l2 = L2;
        PDS.m2 = M2;
        PDS.theta = THETA;

        PDS.d_phi = 0.;
        PDS.d2_phi = 0.;
        PDS.d_theta = 0.;
        PDS.d2_theta = 0.;

        PDS.real_t = 0.;
        PDS.t = 0.;
    }

    public void setXY(double X1, double Y1, double X2, double Y2) {
        //Double operations are safe, because overflow evaluates to Inf. statement ( 1.0/0.0 )
        PDS.l1 = Math.sqrt(X1 * X1 + Y1 * Y1);
        PDS.phi = Math.acos(X1 / PDS.l1);
        if (Y1 >= 0) {
            PDS.phi += Math.PI / 2.;
        } else {
            PDS.phi = Math.PI / 2. - PDS.phi;
        }

        if (PDS.doublependulum) {
            PDS.l2 = Math.sqrt(((X2 - X1) * (X2 - X1)) + ((Y2 - Y1) * (Y2 - Y1)));
            double arg = (X2-X1)/PDS.l2;
            if(arg > 1)
                arg = 1.0;
            if(arg < -1)
                arg = -1.0;
            PDS.theta = Math.acos(arg);
            if (Y2 >= Y1) {
                PDS.theta += Math.PI / 2.;
            } else {
                PDS.theta = Math.PI / 2. - PDS.theta;
            }
            PDS.d_theta = 0;
            PDS.d2_theta = 0;
        }

        PDS.d_phi = 0;
        PDS.d2_phi = 0;
        PDS.d_theta = 0;
        PDS.d2_theta = 0;
        PDS.real_t = 0.;
        PDS.t = 0.;
    }

    //constructor for double pendulum
    public Pendulum(double l_1, double m_1, double l_2, double m_2) {
        PDS.l1 = l_1;
        PDS.m1 = m_1;
        if (PDS.doublependulum)
        {
            PDS.l2 = l_2;
            PDS.m2 = m_2;
        }
        else
        {
            PDS.l2 = 10;
            PDS.m2 = 0;
        }
    }

    //function for standard simulation, time >= 0
    public void simulate(double time) {
        simulate(time, 0., 0.55);
    }

    //function for simulation with additional horizontal force, works only for single pendulum, time >= 0, force [m/s^2] in (-inf;inf)
    public void simulate(double time, double force, double dragCooefficient) {
        //internal time-step for computations
        double dt = 5e-6;
        //time measuring
        PDS.real_t += time;
        if (PDS.doublependulum) {
            while (PDS.t < PDS.real_t) {
                PDS.d2_phi = (
                        -PDS.m2 * PDS.l1 * PDS.d_theta * PDS.d_theta * Math.sin(PDS.phi - PDS.theta) * Math.cos(PDS.phi - PDS.theta)
                                - PDS.g * PDS.m2 * Math.sin(PDS.theta) * Math.cos(PDS.phi - PDS.theta)
                                - PDS.m2 * PDS.l2 * PDS.d_theta * PDS.d_theta * Math.sin(PDS.phi - PDS.theta)
                                + (PDS.m1 + PDS.m2) * PDS.g * Math.sin(PDS.phi)
                )
                        /
                        (
                                PDS.l1 * (PDS.m1 + PDS.m2) - PDS.m2 * PDS.l1 * Math.cos(PDS.phi - PDS.theta) * Math.cos(PDS.phi - PDS.theta)
                        );

                PDS.d2_theta = (PDS.m2 * PDS.l2 * PDS.d_theta * PDS.d_theta * Math.sin(PDS.phi - PDS.theta) * Math.cos(PDS.phi - PDS.theta)
                        - PDS.g * Math.sin(PDS.phi) * Math.cos(PDS.phi - PDS.theta) * (PDS.m1 + PDS.m2)
                        + PDS.l1 * PDS.d_phi * PDS.d_phi * Math.sin(PDS.phi - PDS.theta) * (PDS.m1 + PDS.m2)
                        + PDS.g * Math.sin(PDS.theta) * (PDS.m1 + PDS.m2)
                )
                        /
                        (
                                PDS.l2 * (PDS.m1 + PDS.m2) - PDS.m2 * PDS.l2 * Math.cos(PDS.phi - PDS.theta) * Math.cos(PDS.phi - PDS.theta)
                        );

                PDS.d_phi += PDS.d2_phi * dt;
                PDS.d_theta += PDS.d2_theta * dt;
                PDS.phi += PDS.d_phi * dt;
                PDS.theta += PDS.d_theta * dt;
                PDS.t += dt;
            }
        }
        else
        {
            double ro = 1000; //[kg/m^3] - water density
            double r = Math.cbrt( (PDS.m1/(Math.PI*4000.))*(3./4.));
            double SD = 2*Math.PI*r*r;
            double v = 0;
            while (PDS.t < PDS.real_t)
            {
                v = PDS.d_phi*r;
                PDS.d2_phi = (PDS.g * Math.sin(PDS.phi) + force * Math.cos(PDS.phi) - Math.signum(PDS.d_phi)*dragCooefficient*SD*ro*v*v/2.) / PDS.l1;
                //PDS.d2_phi = (-PDS.g * Math.sin(phi) + force * Math.cos(phi))/PDS.l1;
                PDS.d_phi += PDS.d2_phi * dt;
                PDS.phi += PDS.d_phi * dt;
                PDS.t += dt;
            }
        }
        definePositions();
    }

    private void definePositions() {
        PDS.x1 = PDS.l1 * Math.sin(PDS.phi);
        PDS.y1 = -PDS.l1 * Math.cos(PDS.phi);
        if (PDS.doublependulum) {
            PDS.x2 = PDS.x1 + PDS.l2 * Math.sin(PDS.theta);
            PDS.y2 = PDS.y1 - PDS.l2 * Math.cos(PDS.theta);
        } else {
            PDS.x2 = PDS.x1;
            PDS.y2 = PDS.y1;
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