package pl.edu.agh.fis.anistratenko_team_project.Structure;

public class Structure {
	private SDS sDS;

	public Structure(SDS sds)
	{
		sDS = sds;
		resetStructure();
	}

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

	public double getXFromBody(int i)
	{
		return sDS.bodies.get(i).getX();
	}
	public double getYFromBody(int i)
	{
		return sDS.bodies.get(i).getY();
	}

	public void resetStructure()
	{
		sDS.bodies.clear();
		double dx = sDS.gridSizeReal / sDS.gridSize;
		addBodies(dx);
		joinNeighbouringBodies();
	}

	public void wave()
	{
			for (int j = 1; j < sDS.gridSize-1; j++)
			{
				sDS.bodies.get(1 + sDS.gridSize*j).moveY(-0.9*0.5*sDS.gridSizeReal / sDS.gridSize);
			}
	}

	public void randomizeStructure()
	{
		for (int i = 1; i < sDS.gridSize-1; i++)
		{
			for (int j = 1; j < sDS.gridSize-1; j++)
			{
				sDS.bodies.get(i + sDS.gridSize*j).randomizePosition(0.9*0.5*sDS.gridSizeReal / sDS.gridSize);
			}
		}
	}

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

	private void computeForces()
	{
		for (Body b : sDS.bodies)
		{
			b.computeForces(sDS.k, sDS.g);
		}
	}
	private void updatePositions(double dt)
	{
		for (Body b: sDS.bodies)
		{
			b.updatePositions(dt);
		}
	}

}
