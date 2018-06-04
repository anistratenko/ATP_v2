package pl.edu.agh.fis.anistratenko_team_project.Pendulum;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class PendulumTest {
    PDS pds;
    Pendulum pendulum;

    @Before
    public void setUp(){
        pds = mock(PDS.class);
    }

    @Test
    public void Constructor(){
        double[] length = {1,2};
        double[] mass = {3,4};

        pds.doublependulum = true;

        pendulum = new Pendulum(length[0],mass[0],length[1],mass[1],pds);
        assertEquals(length[0],pds.l1, 1e-15);
        assertEquals(length[1],pds.l2, 1e-15);
        assertEquals(mass[0],pds.m1, 1e-15);
        assertEquals(mass[1],pds.m2, 1e-15);



    }

    @Test
    public void setXY() {

    }

    @Test
    public void setLMF() {
    }

    @Test
    public void setLMF1() {
    }

    @Test
    public void setXY1() {
    }

    @Test
    public void simulate() {
    }

    @Test
    public void simulate1() {
    }
}