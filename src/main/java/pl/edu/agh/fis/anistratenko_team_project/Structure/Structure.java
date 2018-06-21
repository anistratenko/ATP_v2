package pl.edu.agh.fis.anistratenko_team_project.Structure;

import java.util.Random;

/**
 * Class for complete structure simulation.
 */
public class Structure {
	private SDS sDS;

	/**
	 * Constructor for Structure
	 * @param sds - SDS instance shared with StructureView
	 */
	public Structure(SDS sds)
	{
		sDS = sds;
		//initial state generation
		resetStructure();
	}

	/**
	 * Method creating connections between bodies
	 */
	public void joinNeighbouringBodies()
	{
		for (int i = 1; i < sDS.gridSize-1; i++)
		{
			for (int j = 1; j < sDS.gridSize-1; j++)
			{
				sDS.bodies.get(i + sDS.gridSize*j).addNeighbour(sDS.bodies.get(i+1 + sDS.gridSize*j));
				sDS.bodies.get(i + sDS.gridSize*j).addNeighbour(sDS.bodies.get(i-1 + sDS.gridSize*j));
				sDS.bodies.get(i + sDS.gridSize*j).addNeighbour(sDS.bodies.get(i + sDS.gridSize*(j+1)));
				sDS.bodies.get(i + sDS.gridSize*j).addNeighbour(sDS.bodies.get(i + sDS.gridSize*(j-1)));
			}
		}
	}

	/**
	 * Method creating grid of bodies
	 * @param dx - spacing between bodies
	 */
	public void addBodies(double dx)
	{
		for (int i = 0; i < sDS.gridSize; i++)
		{
			for (int j = 0; j < sDS.gridSize; j++)
			{
				sDS.bodies.add(new Body(dx*i, dx*j, sDS.mass));
			}
		}
	}

	/**
	 * Get x position of specified body
	 * @param i - body index
	 * @return
	 */
	public double getXFromBody(int i)
	{
		return sDS.bodies.get(i).getX();
	}
	/**
	 * Get y position of specified body
	 * @param i - body index
	 * @return
	 */
	public double getYFromBody(int i)
	{
		return sDS.bodies.get(i).getY();
	}

	/**
	 * Method to initialize structure state
	 */
	public void resetStructure()
	{
		sDS.bodies.clear();
		double dx = sDS.gridSizeReal / sDS.gridSize;
		addBodies(dx);
		joinNeighbouringBodies();
	}

	/**
	 * Method used to adding wave-like pattern o structure
	 */
	public void wave()
	{
			for (int j = 1; j < sDS.gridSize-1; j++)
			{
				sDS.bodies.get(1 + sDS.gridSize*j).moveY(-0.9*0.5*sDS.gridSizeReal / sDS.gridSize);
			}
	}

	/**
	 * Method used to randomize few bodies position
	 */
	public void randomizeStructure()
	{
		Random rnd = new Random();
		for (int i = 1; i < sDS.gridSize-1; i++)
		{
			for (int j = 1; j < sDS.gridSize-1; j++)
			{
				if (rnd.nextDouble() < 0.03 )
				{
					sDS.bodies.get(i + sDS.gridSize * j).randomizePosition(rnd.nextDouble() * 0.5 * sDS.gridSizeReal / sDS.gridSize);
				}
			}
		}
	}


	/**
	 * Method for standard simulation
	 * @param time - simulated time
	 */
	public void simulate(double time)
	{
		double dt = 1e-4;
		sDS.real_t += time;
		while(sDS.t < sDS.real_t)
		{
			computeForces();
			updatePositions(dt);
			sDS.incrementTime(dt);
		}
	}

	/**
	 * Method computing effective forces action on all bodies
	 */
	private void computeForces()
	{
		for (Body b : sDS.bodies)
		{
			b.computeForces(sDS.k, sDS.g);
		}
	}

	/**
	 * Method updating postions by applying computed forces to bodies over specified time
	 * @param dt - time of force applying
	 */
	private void updatePositions(double dt)
	{
		for (Body b: sDS.bodies)
		{
			b.updatePositions(dt);
		}
	}

}
