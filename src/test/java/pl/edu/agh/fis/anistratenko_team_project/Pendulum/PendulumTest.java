package pl.edu.agh.fis.anistratenko_team_project.Pendulum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

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
    }

    @Test
    public void setXY1() {
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

    @Test
    public void simulate1() {
    }
}


