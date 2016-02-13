package Rules;

import Model.Cell;
import Model.Grid;
import Model.ReproductionSugarScapeAgent;
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
	}

	/**
	 * Applies reproduction rules to the cells.
	 */
	@Override
	public void applyExtraPresetRules(Cell cell, Grid grid) {
		SugarScapeCell curCell = (SugarScapeCell) cell;
		if (curCell.hasAgent()) {
			ReproductionSugarScapeAgent agent = (ReproductionSugarScapeAgent) curCell.getAgent();
			ReproductionSugarScapeAgent mate = agent.findMate(grid);
			if (mate != null) {
				reproduce(agent, mate, grid);
			}
			agent.increaseAge();
			if (agent.isTooOld()) {
				agent.agentDies(curCell);
			}
		}
		
	}

	/**
	 * Creates a new child agent with half of each parent agent's sugar. 
	 * @param curAgent: current agent being handled.
	 * @param neighbor: neighbor agent to mate with.
	 * @param grid: simulation grid.
	 */
	private void reproduce(ReproductionSugarScapeAgent curAgent, ReproductionSugarScapeAgent neighbor, Grid grid) {
		int sugarForChild = curAgent.splitSugar(neighbor);
		SugarScapeCell childCell = curAgent.getEmptyNeighbor(grid);
		childCell.setAgent(new ReproductionSugarScapeAgent(sugarForChild, generateRandom(getMyAgentMetabolismLimit()), generateRandom(getMyAgentVisionLimit()), childCell.getCurRow(), childCell.getCurCol(), generateRandom(NUM_GENDERS - 1),
				generateLimitedRandom(MAX_AGE_MAX, MAX_AGE_MIN), FERTILITY_MIN, FERTILITY_MAX));
	}
	
	/**
	 * Creates a cell for the reproduction simulation and initializes an agent if the cell is occupied.
	 */
	@Override
	protected Cell createCell(String initialState, int row, int col) {
		SugarScapeAgent agent = null;
		if (initialState.equals("OCCUPIED")) {
			agent = new ReproductionSugarScapeAgent(generateRandom(getMyAgentSugarLimit()) + 1, generateRandom(getMyAgentMetabolismLimit()) + 1, generateRandom(getMyAgentVisionLimit()) + 1, row, col, generateRandom(NUM_GENDERS - 1),
					generateLimitedRandom(MAX_AGE_MAX, MAX_AGE_MIN), FERTILITY_MIN, FERTILITY_MAX);
		}
		return new SugarScapeCell(initialState, row, col, getMyMaxCellSugarCapacity(), agent);
	}
	
	/**
	 * Generates a random number between (min, max).
	 * @param max: maximum number.
	 * @param min: minimum number.
	 * @return random neighbor within the limits.
	 */
	private int generateLimitedRandom(int max, int min) {
		return generateRandom(max - min) + min;
	}

}
