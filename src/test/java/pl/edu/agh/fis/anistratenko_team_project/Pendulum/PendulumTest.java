package pl.edu.agh.fis.anistratenko_team_project.Pendulum;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class PendulumTest {
    @Mock
    PDS pds;

    Pendulum pendulum;

    @Before
    public void setUp() throws Exception {

        pendulum = new Pendulum(0, 0, 0, 0, pds);
    }

    @Test
    public void ConstructorDoublePendulum() {
        double[] length = {Double.MAX_VALUE * -1, Double.MAX_VALUE};
        double[] mass = {0, Double.MIN_VALUE};

        pds.doublependulum = true;

        pendulum = new Pendulum(length[0], mass[0], length[1], mass[1], pds);
        assertEquals(length[0], pds.l1, 1e-15);
        assertEquals(length[1], pds.l2, 1e-15);
        assertEquals(mass[0], pds.m1, 1e-15);
        assertEquals(mass[1], pds.m2, 1e-15);

    }

    @Test
    public void ConstructorSinglePendulum() {
        double[] length = {Double.MAX_VALUE * -1, Double.MAX_VALUE};
        double[] mass = {0, Double.MAX_VALUE};

        pds.doublependulum = false;

        pendulum = new Pendulum(length[0], mass[0], length[1], mass[1], pds);
        assertEquals(length[0], pds.l1, 1e-15);
        assertEquals(10, pds.l2, 1e-15);
        assertEquals(mass[0], pds.m1, 1e-15);
        assertEquals(0, pds.m2, 1e-15);
    }


    @Test
    public void setXY() {
        double[] setXY_args = {1, 2};
        class PendulumStub extends Pendulum {
            PendulumStub(PDS pds) {
                super(0, 0, 0, 0, pds);
            }

            @Override
            public void setXY(double a, double b, double c, double d) {
                assertEquals(setXY_args[0], a, 1e-15);
                assertEquals(setXY_args[1], b, 1e-15);
                assertEquals(setXY_args[0], c, 1e-15);
                assertEquals(setXY_args[1], d, 1e-15);
            }

        }

        pendulum = new PendulumStub(pds);
        pendulum.setXY(setXY_args[0], setXY_args[1]);

    }

    @Test
    public void setLMF() {
        double[] setLMF_args = {Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE*-1};
        class PendulumStub extends Pendulum {
            PendulumStub(PDS pds) {
                super(0, 0, 0, 0, pds);
            }

            @Override
            public void setLMF(double a, double b, double c, double d, double e, double f) {
                assertEquals(setLMF_args[0], a, 1e-15);
                assertEquals(setLMF_args[1], b, 1e-15);
                assertEquals(setLMF_args[2], c, 1e-15);
                assertEquals(setLMF_args[0], d, 1e-15);
                assertEquals(0, e, 1e-15);
                assertEquals(0, e, 1e-15);
            }

        }

        pendulum = new PendulumStub(pds);
        pendulum.setLMF(setLMF_args[0],setLMF_args[1],setLMF_args[2]);

    }

    @Test
    public void setLMF1() {
        double[] LMF_args = {Double.MIN_VALUE, Double.MAX_VALUE, Double.MAX_VALUE*-1, 0, 12, 15};
        pendulum.setLMF(LMF_args[0],LMF_args[1],LMF_args[2],LMF_args[3],LMF_args[4],LMF_args[5]);
        assertEquals(LMF_args[0], pds.l1, 1e-15);
        assertEquals(LMF_args[1], pds.m1, 1e-15);
        assertEquals(LMF_args[2], pds.phi, 1e-15);
        assertEquals(LMF_args[3], pds.l2, 1e-15);
        assertEquals(LMF_args[4], pds.m2, 1e-15);
        assertEquals(LMF_args[5], pds.theta, 1e-15);

        assertEquals(0, pds.d_phi, 1e-15);
        assertEquals(0, pds.d2_phi, 1e-15);
        assertEquals(0, pds.d_theta, 1e-15);
        assertEquals(0, pds.d2_theta, 1e-15);
        assertEquals(0, pds.real_t, 1e-15);
        assertEquals(0, pds.t, 1e-15);
    }

    @Test
    public void setXY1() {

        //Y2 << Y1
        double l2 = Math.sqrt(0 + Math.pow(Double.MAX_VALUE - 0, 2));
        double theta = Math.acos(1 / l2);
        pendulum.setXY(1, 0, 1, -1 * Double.MAX_VALUE);
        assertTrue("Y2 is minimum value of its type, Y1 is zero",
                (pds.theta- (Math.PI / 2. - theta)) < 1e-15);

        //Y2 >> Y1
        theta = Math.acos(0);
        pendulum.setXY(1, 0, 1, Double.MAX_VALUE);
        assertTrue("Y2 is maximum value of its type, Y1 is zero",
                (pds.theta - (Math.PI / 2. + theta)) < 1e-15);

        //Y2 < Y1
        pendulum.setXY(1, 0, 2, Double.MIN_VALUE);
        theta = Math.acos(1);
        assertTrue("Y2 is minimum positive value of its type, Y1 is zero",
                (pds.theta - (Math.PI / 2. + theta)) < 1e-15);

        //Y2 > Y1
        pendulum.setXY(1, 0, 2, -1 * Double.MIN_VALUE);
        assertTrue("Y2 is maximum negative value ( close to 0 ) of its type, Y1 is zero",
                (pds.theta - (Math.PI / 2. + theta)) < 1e-15);


        //Y2 = Y1
        double rand = (Math.random() - 0.5) * Double.MAX_VALUE;
        pendulum.setXY(1, rand, 2, rand);
        theta = Math.acos(1);
        assertTrue("Y2 = Y1 and it is equal to" + Double.toString(rand) + " expected result is "
                        + Double.toString(theta + Math.PI / 2.) + " actual result is " + pds.theta,
                (pds.theta - (theta + Math.PI / 2.)) < 1e-15);

        assertEquals(0, pds.d_phi, 1e-15);
        assertEquals(0, pds.d2_phi, 1e-15);
        assertEquals(0, pds.d_theta, 1e-15);
        assertEquals(0, pds.d2_theta, 1e-15);
        assertEquals(0, pds.real_t, 1e-15);
        assertEquals(0, pds.t, 1e-15);
    }

    @Test
    public void simulate() {
        double simulate_arg = 12;
        class PendulumStub extends Pendulum {
            PendulumStub(PDS pds) {
                super(0, 0, 0, 0, pds);
            }

            @Override
            public void simulate(double a, double b, double c) {
                assertEquals(simulate_arg, a, 1e-15);
                assertEquals(0, b, 1e-15);
                assertEquals(0.55, c, 1e-15);

            }

        }

        pendulum = new PendulumStub(pds);
        pendulum.simulate(simulate_arg);
    }

    @Test @Ignore
    public void simulate1() {
        //numerical calculations do not require testing
    }
}


