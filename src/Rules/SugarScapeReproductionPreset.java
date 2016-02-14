/**
 * @author Amy Zhao
 * Defines the variables and methods for each rules object for the Sugar Scape simulation's Reproduction preset.
 */

package Rules;

import java.util.ResourceBundle;

import Model.Cell;
import Model.Grid;
import Model.ReproductionSugarScapeAgent;
import Model.SugarScapeAgent;
import Model.SugarScapeCell;

public class SugarScapeReproductionPreset extends SugarScapeRules{
	public static final String DEFAULT_RESOURCE = "Rules/SugarScapeReproductionRules";
	private ResourceBundle myResource = ResourceBundle.getBundle(DEFAULT_RESOURCE);
	private int MAX_AGE_MIN = Integer.parseInt(myResource.getString("MaxAgeMinimum"));
	private int MAX_AGE_MAX = Integer.parseInt(myResource.getString("MaxAgeMaximum"));
	private int FERTILITY_MIN = Integer.parseInt(myResource.getString("FertilityMinimum"));
	private int FERTILITY_MAX = Integer.parseInt(myResource.getString("FertilityMaximum"));
	private int NUM_GENDERS = Integer.parseInt(myResource.getString("NumGenders"));
	
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
		if (childCell != null) {
			childCell.setAgent(new ReproductionSugarScapeAgent(sugarForChild, generateRandom(getMyAgentMetabolismLimit()), generateRandom(getMyAgentVisionLimit()), childCell.getCurRow(), childCell.getCurCol(), generateRandom(NUM_GENDERS - 1),
				generateLimitedRandom(MAX_AGE_MAX, MAX_AGE_MIN), FERTILITY_MIN, FERTILITY_MAX));
		}
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

	/**
	 * Creates agent specific to the reproduction preset.
	 */
	@Override
	protected SugarScapeAgent createPresetAgent(int row, int col) {
		return new ReproductionSugarScapeAgent(generateRandom(getMyAgentSugarLimit()) + 1, generateRandom(getMyAgentMetabolismLimit()) + 1, generateRandom(getMyAgentVisionLimit()) + 1, row, col, generateRandom(NUM_GENDERS),
				generateLimitedRandom(MAX_AGE_MAX, MAX_AGE_MIN), FERTILITY_MIN, FERTILITY_MAX);
	
	}

}
