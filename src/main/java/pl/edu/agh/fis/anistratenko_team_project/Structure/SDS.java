package pl.edu.agh.fis.anistratenko_team_project.Structure;

import java.util.Random;
import java.util.Vector;

public class SDS {
	public SDS() {
	}

	public boolean running = false;
	public double FrameTime = 1. / 60.;

	public double g = 0;
	public double k = 5;
	public int gridSize = 15;
	public double gridSizeReal = 0.5;
	public double mass = 0.02;

	public double xreal = 0.5;
	public double yreal = 0.5;


	public Vector<Body> bodies = new Vector<>();
	public double real_t = 0.;
	public double t = 0.;
	public Random rnd = new Random();

	public void incrementTime(double dt)
	{
		t += dt;
	}
}

class Body
{
	private double x;
	private double y;
	private double vx;
	private double vy;
	private double fx;
	private double fy;
	private final double mass;
	private Vector<Body> neighbours = new Vector<Body>();

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	Body(double init_x, double init_y, double init_mass)
	{
		mass = init_mass;
		x  = init_x;
		y  = init_y;
		fx = 0.;
		fy = 0.;
		vx = 0.;
		vy = 0.;
	}

	public void randomizePosition(double maxDifference)
	{
		Random rnd = new Random();
		x += rnd.nextDouble() * maxDifference * 2 - maxDifference;
		y += rnd.nextDouble() * maxDifference * 2 - maxDifference;
	}

	public void moveY(double dy)
	{
		y+=dy;
	}

	public void addNeighbour(Body N)
	{
		if (neighbours.size() < 4) neighbours.add(N);
	}

	public void computeForces(double k, double g)
	{
		fx = 0.;
		fy = 0.;
		for (Body N: neighbours)
		{
			fx += (N.x - x)*k;
			fy += (N.y - y)*k - g*mass;
		}
	}

	public void updatePositions(double dt)
	{
		double ax = fx/mass;
		double ay = fy/mass;
		vx += dt*ax;
		vy += dt*ay;
		x += dt*vx;
		y += dt*vy;
	}
}