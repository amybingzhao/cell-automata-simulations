package Rules;

import java.util.ArrayList;
import java.util.List;

import Model.Cell;
import Model.Grid;
import Model.ReproductionSugarScapeAgent;
import Model.StandardSugarScapeAgent;
import Model.SugarScapeAgent;
import Model.SugarScapeCell;

public class SugarScapeReproductionPreset extends SugarScapeRules{
	private static final int MAX_AGE_MIN = 60;
	private static final int MAX_AGE_MAX = 100;
	private static final int FERTILITY_MIN = 18;
	private static final int FERTILITY_MAX = 50;
	private static final int NUM_GENDERS = 2;
	public SugarScapeReproductionPreset(int sugarGrowBackRate, int sugarGrowBackInterval, int maxSugarCapacity,
			int sugarLimit, int visionLimit, int metabolismLimit) {
		super(sugarGrowBackRate, sugarGrowBackInterval, maxSugarCapacity, sugarLimit, visionLimit, metabolismLimit);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void applyExtraPresetRules(Cell cell, Grid grid) {
		SugarScapeCell curCell = (SugarScapeCell) cell;
		if (curCell.hasAgent()) {
			ReproductionSugarScapeAgent agent = (ReproductionSugarScapeAgent) curCell.getAgent();
			ReproductionSugarScapeAgent mate = agent.findMate(grid);
			if (mate != null) {
				reproduce(agent, mate, grid);
			}
		}
	}

	private void reproduce(ReproductionSugarScapeAgent curAgent, ReproductionSugarScapeAgent neighbor, Grid grid) {
		int sugarForChild = curAgent.splitSugar(neighbor);
		SugarScapeCell childCell = curAgent.getEmptyNeighbor(grid);
		childCell.setAgent(new ReproductionSugarScapeAgent(generateRandom(getMyAgentSugarLimit()), generateRandom(getMyAgentMetabolismLimit()), generateRandom(getMyAgentVisionLimit()), childCell.getCurRow(), childCell.getCurCol(), generateRandom(NUM_GENDERS - 1),
				generateLimitedRandom(MAX_AGE_MAX, MAX_AGE_MIN), FERTILITY_MIN, FERTILITY_MAX));
	}
	
	@Override
	protected Cell createCell(String initialState, int row, int col) {
		SugarScapeAgent agent = null;
		if (initialState.equals("OCCUPIED")) {
			agent = new ReproductionSugarScapeAgent(generateRandom(getMyAgentSugarLimit()), generateRandom(getMyAgentMetabolismLimit()), generateRandom(getMyAgentVisionLimit()), row, col, generateRandom(NUM_GENDERS - 1),
					generateLimitedRandom(MAX_AGE_MAX, MAX_AGE_MIN), FERTILITY_MIN, FERTILITY_MAX);
		}
		return new SugarScapeCell(initialState, row, col, getMyMaxCellSugarCapacity(), agent);
	}
	
	private int generateLimitedRandom(int max, int min) {
		return generateRandom(max - min) + min;
	}

	@Override
	public List<String> getParameters() {
		// TODO Auto-generated method stub
		List<String> parameters = new ArrayList<String>();
		return parameters;
	}
}
