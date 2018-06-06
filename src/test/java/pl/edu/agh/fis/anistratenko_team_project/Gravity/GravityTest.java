package pl.edu.agh.fis.anistratenko_team_project.Gravity;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;


@SuppressWarnings("unchecked")
public class GravityTest {
    int numberOfBodies;
    GDS gds;
    Gravity gravity;

    @Before
    public void setUp() {
        numberOfBodies = 34;
        gds = mock(GDS.class);
        gds.numOfBodies = numberOfBodies;
        gds.bodies = new ArrayList<>();
        gds.rnd = new Random();
        gravity = new Gravity(numberOfBodies, gds);
    }

    @Test
    public void getBody() {
        gds.bodies = (ArrayList<Body>) mock(ArrayList.class);
        int index = 9;

        gravity.getBody(index);

        verify(gds.bodies, times(1)).get(index);
    }

    @Test
    public void getNumOfBodies() {
        gds.bodies = (ArrayList<Body>) mock(ArrayList.class);

        gravity.getNumOfBodies();

        verify(gds.bodies, times(1)).size();
    }

    @Test
    public void setG() {
        gds.bodies = (ArrayList<Body>) mock(ArrayList.class);
        double g = 3.4;

        gravity.setG(3.4);

        assertEquals(gds.G, g, 0.01);
    }

    @Test
    public void resetGravity() {
        gds.bodies = (ArrayList<Body>) mock(ArrayList.class);

        gravity.resetGravity(numberOfBodies);

        assertEquals(gds.numOfBodies, numberOfBodies);
        verify(gds.bodies, times(1)).clear();
        verify(gds.bodies, atLeast(numberOfBodies)).add(any());
    }

    @Test
    public void simulate() {
        Body body = mock(Body.class);
        gds.bodies = new ArrayList<>();
        for (int i = 0; i < numberOfBodies; i++) {
            gds.bodies.add(body);
        }

        gravity.simulate(1e-3);
        verify(body, atMost(numberOfBodies)).updatePosition(anyDouble());
    }

    @Test
    public void calcDistance() {
        gds.numOfBodies = numberOfBodies;
        gds.bodies = new ArrayList<>();
        gds.bodies.add(new Body(0., 0., 1., 1., 1., 1.));
        gds.bodies.add(new Body(1., 1., 1., 1., 1., 1.));

        double calculatedDistance = gravity.calcDistance(0, 1);

        assertEquals(Math.sqrt(2), calculatedDistance, 0.01);
    }

    @Test
    public void calcNewAcceleration() {
        gds.bodies = (ArrayList<Body>) mock(ArrayList.class, RETURNS_DEEP_STUBS);
        Body body = mock(Body.class);
        when(gds.bodies.get(anyInt())).thenReturn(body);

        gravity.calcNewAcceleration(3, 5, 0.4);

        verify(gds.bodies, times(4)).get(3);
        verify(gds.bodies, times(4)).get(5);
    }

    @Test
    public void removeBodyOnCollision() {
        gds.bodies = (ArrayList<Body>) mock(ArrayList.class, RETURNS_DEEP_STUBS);
        Body body = mock(Body.class);
        when(gds.bodies.get(anyInt())).thenReturn(body);

        gravity.removeBodyOnCollision(3, 5);

        verify(gds.bodies, atLeast(16)).get(3);
        verify(gds.bodies, atLeast(9)).get(5);
        verify(gds.bodies, times(1)).remove(5);

    }
}