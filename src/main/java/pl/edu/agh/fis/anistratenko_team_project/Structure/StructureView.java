package pl.edu.agh.fis.anistratenko_team_project.Structure;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import pl.edu.agh.fis.anistratenko_team_project.Application.SimulationView;

import java.util.ArrayList;

public class StructureView implements SimulationView {
	private Structure structure;
	private SDS sDS;
	private ArrayList<Node> elements = new ArrayList<>();
	private static double xsize;
	private static double ysize;

	public StructureView(SDS sds)
	{
		sDS = sds;
		structure = new Structure(sDS);

		double xscale = xsize / sDS.xreal;
		double yscale = (ysize) / sDS.yreal;
		double scale = Math.min(xscale, yscale);
		for (int i = 0; i < Math.pow(sDS.gridSize, 2); i++)
		{
			elements.add(new Circle(structure.getXFromBody(i)*scale,
					structure.getYFromBody(i)*scale,5));
		}
	}

	@Override
	public void setPaneSize(double px_x, double px_y, double re_x, double re_y)
	{
		xsize = px_x;
		ysize = px_y;
		sDS.xreal = re_x;
		sDS.yreal = re_y;
	}

	@Override
	public ArrayList<Node> getNodes() {
		return elements;
	}

	@Override
	public boolean performSimulationStep() {
		structure.simulate(sDS.FrameTime);
		refresh();
		return false;
	}

	public void callWave()
	{
		structure.wave();
		refresh();
	}

	public void callRandomize()
	{
		structure.randomizeStructure();
		refresh();
	}

	public void callReset()
	{
		structure.resetStructure();
		refresh();
	}

	public void refresh()
	{
		double xcenter = xsize / 2.;
		double ycenter = (ysize+30) / 2.;
		double xscale = xsize / sDS.xreal;
		double yscale = ysize / sDS.yreal;
		double scale = Math.min(xscale, yscale);
		for (int i = 0; i < elements.size(); i++) {
			((Circle) elements.get(i)).setCenterX( -sDS.gridSizeReal*scale/2. + xcenter + structure.getXFromBody(i) * scale);
			((Circle) elements.get(i)).setCenterY( -sDS.gridSizeReal*scale/2. + ycenter + structure.getYFromBody(i) * scale);
		}
	}


}
