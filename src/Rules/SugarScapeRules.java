package Rules;

import java.util.ArrayList;

import Model.Cell;
import Model.Grid;
import Model.SugarScapeAgent;
import Model.SugarScapeCell;

public abstract class SugarScapeRules extends Rules {
	
	private int mySugarGrowBackRate;
	private int mySugarGrowBackInterval;
	private int mySugarGrowBackCountdown;
	private int myMaxCellSugarCapacity;
	private int myAgentSugarLimit;
	private int myAgentVisionLimit;
	private int myAgentMetabolismLimit;
	private static final String DEFAULT_STATE = "NONE";
	
	public SugarScapeRules(int sugarGrowBackRate, int sugarGrowBackInterval, int maxSugarCapacity, int sugarLimit, int visionLimit, int metabolismLimit) {
		mySugarGrowBackRate = sugarGrowBackRate;
		mySugarGrowBackInterval = sugarGrowBackInterval;
		mySugarGrowBackCountdown = sugarGrowBackInterval;
		setMyMaxCellSugarCapacity(maxSugarCapacity);
		setMyAgentSugarLimit(sugarLimit);
		setMyAgentVisionLimit(visionLimit);
		setMyAgentMetabolismLimit(metabolismLimit);
	}
	
	/**
	 * Creates an instance of the cell required for the particular simulation.
	 */
	@Override
	protected abstract Cell createCell(String initialState, int row, int col);
	
	/**
	 * Sets the interval for sugar to grow back for this simulation.
	 * @param interval: number of steps before sugar grows back.
	 */
	public void setMySugarGrowBackInterval(int interval) {
		mySugarGrowBackInterval = interval;
	}
	
	/**
	 * Max vision an agent in this simulation can have.
	 * @param limit: max vision for an agent.
	 */
	public void setMyVisionLimit(int limit) {
		setMyAgentVisionLimit(limit);
	}
	
	/**
	 * Applies simulation rules to the cell.
	 */
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		applyRulesToCell((SugarScapeCell) cell, grid);
	}
	
	/**
	 * Applies simulation rules to a sugar scape cell.
	 */
	public void applyRulesToCell(SugarScapeCell cell, Grid grid) {		
		if (cell.hasAgent()) {
			SugarScapeAgent agent = cell.getAgent();
			if (agent.hasNotMoved()) {
				SugarScapeCell nextPatch = agent.findNextPatch(grid);
				if (nextPatch != null) {
					agent.moveToPatch(cell, nextPatch);
					nextPatch.setNextState("OCCUPIED");
					cell.setNextState("NONE");
					addCellToBeUpdated(nextPatch);
					addCellToBeUpdated(cell);
				}
				applyExtraPresetRules(cell, grid); // is this the right place?
			}
		}
		
		if (isLastCellInGrid(cell, grid)) {
			System.out.println("sugar growback countdown: " + mySugarGrowBackCountdown);
			if (canGrowSugarBack()) {
				growBackSugarInCells(grid);
			} else {
				mySugarGrowBackCountdown--;
			}
		}
	}
	
	/**
	 * Grows back the sugar in all cells in the grid.
	 * @param grid: simulation grid.
	 */
	private void growBackSugarInCells(Grid grid) {
		for (int row = 0; row < grid.getNumRows(); row++) {
			for (int col = 0; col < grid.getNumCols(); col++) {
				SugarScapeCell cell = (SugarScapeCell) grid.getCell(row, col);
				cell.addSugar(mySugarGrowBackRate);
				addCellToBeUpdated(cell);
			}
		}
	}
	
	/**
	 * Checks if the sugar can grow back yet.
	 * @return true if the sugar grow back interval has been satisfied; false otherwise.
	 */
	private boolean canGrowSugarBack() {
		if (mySugarGrowBackCountdown == 0) {
			System.out.println("hello");
			mySugarGrowBackCountdown = mySugarGrowBackInterval;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Description of the simulation.
	 */
	@Override
	public String toString() {
		return "Sugarscape";
	}
	
	/**
	 * Gets the parameters of the simulation.
	 */
	@Override
	public ArrayList<String> getParameters() {
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add("SugarGrowBackInterval:" + mySugarGrowBackInterval);
		parameters.add("SugarGrowBackCountdown:" + mySugarGrowBackCountdown);
		parameters.add("MaxCellSugarCapacity:" + myMaxCellSugarCapacity);
		parameters.add("AgentSugarLimit:" + myAgentSugarLimit);
		parameters.add("AgentVisionLimit:" + myAgentVisionLimit);
		parameters.add("AgentMetabolismLimit:" + myAgentMetabolismLimit);
		return parameters;
	}
	
	/**
	 * Applies extra rules specific to a given preset.
	 * @param cell: cell to apply rules to.
	 * @param grid: simulation grid.
	 */
	public abstract void applyExtraPresetRules(Cell cell, Grid grid);

	/**
	 * Max sugar an agent can start with.
	 * @return max sugar an agent can start with.
	 */
	public int getMyAgentSugarLimit() {
		return myAgentSugarLimit;
	}

	/**
	 * Sets the max sugar an agent can start with.
	 * @param myAgentSugarLimit: max sugar an agent can start with.
	 */
	public void setMyAgentSugarLimit(int myAgentSugarLimit) {
		this.myAgentSugarLimit = myAgentSugarLimit;
	}

	/**
	 * Gets the max amount of sugar a cell can hold.
	 * @return max amount of sugar a cell can hold.
	 */
	public int getMyMaxCellSugarCapacity() {
		return myMaxCellSugarCapacity;
	}

	/**
	 * Sets the max amount of sugar a cell can hold.
	 * @param myMaxCellSugarCapacity: max amount of sugar a cell can hold.
	 */
	public void setMyMaxCellSugarCapacity(int myMaxCellSugarCapacity) {
		this.myMaxCellSugarCapacity = myMaxCellSugarCapacity;
	}

	/**
	 * Gets the max metabolism an agent can have in this simulation.
	 * @return the max metabolism an agent can have in this simulation.
	 */
	public int getMyAgentMetabolismLimit() {
		return myAgentMetabolismLimit;
	}

	/**
	 * Sets the max metabolism an agent can have in this simulation.
	 * @param myAgentMetabolismLimit: the max metabolism an agent can have in this simulation.
	 */
	public void setMyAgentMetabolismLimit(int myAgentMetabolismLimit) {
		this.myAgentMetabolismLimit = myAgentMetabolismLimit;
	}

	/**
	 * Gets the max vision an agent can have in this simulation.
	 * @return the max vision an agent can have in this simulation.
	 */
	public int getMyAgentVisionLimit() {
		return myAgentVisionLimit;
	}

	/**
	 * Sets the max vision an agent can have in this simulation.
	 * @param myAgentVisionLimit: the max vision an agent can have in this simulation.
	 */
	public void setMyAgentVisionLimit(int myAgentVisionLimit) {
		this.myAgentVisionLimit = myAgentVisionLimit;
	}
	
	/**
	 * Returns the default state for this simulation.
	 */
	@Override
	protected String getDefault() {
		return DEFAULT_STATE;
	}
}
