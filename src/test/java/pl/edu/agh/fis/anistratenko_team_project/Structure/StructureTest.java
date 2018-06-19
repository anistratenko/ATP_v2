package pl.edu.agh.fis.anistratenko_team_project.Structure;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.Vector;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class StructureTest {
    SDS sds;
    Body body;
    Structure structure;

    @Before
    public void setUp() {
        sds = mock(SDS.class);
        body = mock(Body.class);

        sds.bodies = new Vector<>();
        sds.rnd = new Random();
        structure = new Structure(sds);

        sds.bodies.clear();
        sds.gridSize = 15;
        for (int i = 0; i < sds.gridSize * sds.gridSize; i++) sds.bodies.add(body);
    }

    @Test
    public void joinNeighbouringBodies() {
        structure.joinNeighbouringBodies();
        verify(body, times((sds.gridSize - 2) * (sds.gridSize - 2) * 4)).addNeighbour(any());
    }

    @Test
    public void addBodies() {
        sds.bodies = (Vector<Body>) mock(Vector.class);
        structure.addBodies(1.);
        verify(sds.bodies, times(sds.gridSize * sds.gridSize)).add(any());
    }

    @Test
    public void resetStructure() {
        class StructureStub extends Structure {
            public StructureStub(SDS sds) {
                super(sds);
            }

            @Override
            public void joinNeighbouringBodies() {
            }

            @Override
            public void addBodies(double dx) {
            }

        }
        StructureStub structure = new StructureStub(sds);
        sds.bodies = (Vector<Body>) mock(Vector.class);
        structure.resetStructure();
        verify(sds.bodies, times(1)).clear();
    }

    @Test
    public void wave() {
        structure.wave();
        verify(body, times((sds.gridSize - 2))).moveY(anyDouble());
    }

    @Test
    public void randomizeStructure() {
        structure.randomizeStructure();
        verify(body, atMost((sds.gridSize - 2) * (sds.gridSize - 2))).randomizePosition(anyDouble());
    }
}
