package pl.edu.agh.fis.anistratenko_team_project.Pendulum;

/**
 * Class for complete pendulum simulation.
 * Can simulate single or double pendulum with additional parameters
 */
public class Pendulum {
    private PDS pDS;

	/**
	 * Method used to set position of single pendulum
	 * @param X1 - x coordinate of pendulum
	 * @param Y1 - y coordinate of pendulum
	 */
	public void setXY(double X1, double Y1) {
        setXY(X1, Y1, X1, Y1);
    }

	/**
	 * Method used to set single pendulum's initial parameters
	 * @param L1 - length of pendulum
	 * @param M1 - mass of pendulum
	 * @param PHI - initial amplitude
	 */
    public void setLMF(double L1, double M1, double PHI) {
        setLMF(L1, M1, PHI, L1, 0, 0);
    }

	/**
	 * Method used to set double pendulum's initial parameters
	 * @param L1 - length of first pendulum
	 * @param M1 - mass of first pendulum
	 * @param PHI - initial amplitude of first pendulum
	 * @param L2 - length of second pendulum
	 * @param M2 - mass of second pendulum
	 * @param THETA - initial amplitude of second pendulum
	 */
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
    }

	/**
	 * Method used to set position of double pendulum
	 * @param X1 - x coordinate of first pendulum
	 * @param Y1 - y coordinate of first pendulum
	 * @param X2 - x coordinate of second pendulum
	 * @param Y2 - y coordinate of second pendulum
	 */
    public void setXY(double X1, double Y1, double X2, double Y2) {
        //Double operations are safe, because overflow evaluates to Inf. statement ( 1.0/0.0 )
        pDS.l1 = Math.sqrt(X1 * X1 + Y1 * Y1);
        //acos returns angle from [0; PI]
        pDS.phi = Math.acos(X1 / pDS.l1);
        //convert angle to [0; 2PI]
        if (Y1 >= 0) {
            pDS.phi += Math.PI / 2.;
        } else {
            pDS.phi = Math.PI / 2. - pDS.phi;
        }

        //same for double pendulum
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

        //this should be elsewhere
        pDS.d_phi = 0;
        pDS.d2_phi = 0;
        pDS.d_theta = 0;
        pDS.d2_theta = 0;
        pDS.real_t = 0.;
        pDS.t = 0.;
    }

	/**
	 * Constructor for double pendulum
	 * @param L1 - length of first pendulum
	 * @param M1 - mass of first pendulum
	 * @param L2 - length of second pendulum
	 * @param M2 - mass of second pendulum
	 * @param pds - PDS instance shared with PendulumView, PendulumGUIController and Controller
	 */
    public Pendulum(double L1, double M1, double L2, double M2, PDS pds) {
    	pDS = pds;
        pDS.l1 = L1;
        pDS.m1 = M1;
        if (pDS.doublependulum) {
            pDS.l2 = L2;
            pDS.m2 = M2;
        } else {
            pDS.l2 = 10;
            pDS.m2 = 0;
        }
    }

	/**
	 * Method for standard simulation
	 * @param time - simulated time
	 */
	public void simulate(double time) {
        simulate(time, 0., 0.55);
    }

	/**
	 * Function for simulation with additional parameters for single pendulum
	 * @param time - simulated time
	 * @param acc - horizontal acceleration of system
	 * @param dragCooefficient - drag coefficient of pendulum
	 */
    public void simulate(double time, double acc, double dragCooefficient) {
        //internal time-step for computations
        double dt = 5e-6;
        //time measuring
        pDS.real_t += time;
        if (pDS.doublependulum) {
        	//check if simulated process reached specified moment
            while (pDS.t < pDS.real_t) {
            	//equations for double pendulum
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
        	//additional parameters for estimating drag
            double ro = 1000; //[kg/m^3] - water density
			//radius of bob [m]
            double r = Math.cbrt((pDS.m1 / (Math.PI * 4000.)) * (3. / 4.));
            //effective are of sphere [m^2]
            double SD = Math.PI * r * r;
            //velocity magnitude of bob [m/s]
            double v = 0;
            while (pDS.t < pDS.real_t) {
                v = pDS.d_phi * r;
                pDS.d2_phi = (pDS.g * Math.sin(pDS.phi) + acc * Math.cos(pDS.phi) - Math.signum(pDS.d_phi) * dragCooefficient * SD * ro * v * v / 2.) / pDS.l1;
                pDS.d_phi += pDS.d2_phi * dt;
                pDS.phi += pDS.d_phi * dt;
                pDS.t += dt;
            }
        }
        definePositions();
    }

	/**
	 * Method used to compute postion of bob's in cartesian coordinates
	 */
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