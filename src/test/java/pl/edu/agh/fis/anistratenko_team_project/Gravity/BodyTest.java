package pl.edu.agh.fis.anistratenko_team_project.Gravity;

import org.junit.Test;

import static org.junit.Assert.*;

public class BodyTest {

    @Test
    public void updatePosition() {
        Body body = new Body(0,0,1,1,1,1);

        double x = body.getX();
        double y = body.getY();

        body.updatePosition(1);


        assertNotEquals(x, body.getX(), 0.01);
        assertNotEquals(y, body.getY(), 0.01);
    }
}