package pl.edu.agh.fis.anistratenko_team_project.Structure;

import java.util.Random;
import java.util.Vector;

/**
 * Class for storing data used in structure simulation
 */
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

/**
 * class represinting single body used in Structure class
 */
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

	/**
	 * Get x position of body
	 * @return x position
	 */
	public double getX()
	{
		return x;
	}

	/**
	 * Get y position of body
	 * @return y position
	 */
	public double getY()
	{
		return y;
	}

	/**
	 * Constructor for single body
	 * @param init_x - initial x position
	 * @param init_y - initial y position
	 * @param init_mass - mass of body
	 */
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

	/**
	 * Method for random position change of body
	 * @param maxDifference - maximum change of position in every direction
	 */
	public void randomizePosition(double maxDifference)
	{
		Random rnd = new Random();
		x += rnd.nextDouble() * maxDifference * 2 - maxDifference;
		y += rnd.nextDouble() * maxDifference * 2 - maxDifference;
	}

	/**
	 * Method used to move body verticlaly
	 * @param dy - distance to move
	 */
	public void moveY(double dy)
	{
		y+=dy;
	}

	/**
	 * Connect with neighbouring body
	 * @param N - neighbour to connect
	 */
	public void addNeighbour(Body N)
	{
		//maximum 4 neighbours
		if (neighbours.size() < 4) neighbours.add(N);
	}

	/**
	 * Method computing effective force acting on body
	 * @param k - rate [N/m]
	 * @param g - gravity acceleration [m/s^2]
	 */
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

	/**
	 * Update position of body applying actual effective force for specified time
	 * @param dt - time of force applying
	 */
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