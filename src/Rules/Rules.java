// This entire file is part of my masterpiece.
// Amy Zhao (abz3)

// For my code masterpiece, I refactored my abstract base class for my Rules inheritance hierarchy. The purpose of this
// code is to provide a base from which rules specific to each simulation can be added. Thus, it is designed to be open 
// to extension by adding subclasses for specific cell automata simulations, while being closed to what class of
// simulations it may be used for (i.e. only for implementing rules for cell automata simulations). 

// By adding subclasses, we allow for extension by the addition of more complicated rules or the use of given methods 
// for new applications (e.g. the countSurroundingNeighborsOfType() method is used for counting the number of neighbors
// of the same type and the total number of neighbors in the Segregation simulation, and for counting the number of 
// live neighbors in the Game of Life simulation). Using deductive commonality analysis, I was able to identify other 
// important methods in addition to the countSurroundingNeighborsOfType() method that are used in many different CA 
// simulations, such as generateRandom() for picking a random index for a list (e.g. picking a random water cell 
// for a fish to move to in the Predator Prey simulation, as well as generating random amounts of initial sugar for 
// agents in the Sugarscape simulations), generateRandomWithinLimits() for picking random numbers between bounds such 
// as age limits, switchCells() for switching the states and locations of two cells, and isLastCellInGrid(), which is
// used by many simulations for identifying when to apply a certain change to all of the cells at that must be applied 
// only at the end of a step (e.g. resetting all flags for ants that have moved in a given turn for the ForagingAnts 
// simulation, and moving the cells that can and want to be moved in the Segregation simulation). This allowed for 
// elimination of duplicate code in many of the subclasses.

// The methods written for this class also make it closed for use by cell automata simulations only because they
// are specific to updating a simulation that handles a grid with cells whose states change based on their relative
// location within the grid and their own state. For example, initGrid() is specific to initializing a simulation grid
// with discrete cells, createCell() is specific to creating a cell with a given state and location, and the 
// toBeUpdatedList functions to identify cells that have changed state this step and must be updated by the view.

// Additionally, this class’s member variables are all private. The setters in this class check for updates to the data 
// that could be harmful, such as null objects, and the data is accessed by getters. Any constants are loaded via a 
// properties file (i.e. myDefaultState, which specifies the default state for a new cell in any simulation, and 
// myCellRow and myCellCol which specify the location of the cell of interest within a 3x3 grid of its neighborhood). 
// The properties file allows changes to these constants to be made easily and in an obvious place (e.g. changing the
// default state for the Fire simulation from TREE to EMPTY would simply require changing the value in the 
// Rules.properties file). 

/**
 * @author Amy Zhao
 * An abstract class that specific simulation rules class are based off of
 */

package Rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import Model.Cell;
import Model.Grid;
import Model.StandardCell;
import javafx.scene.paint.Color;

public abstract class Rules {

	private List<Cell> toBeUpdated = new ArrayList<Cell>();
	private Map<String, Integer> myStatesCount;
	private Map<String, Color> myStatesColors;

	private static final String DEFAULT_RULES_RESOURCE = "Rules/Rules";
	private ResourceBundle myRulesResources;
	private int myCellRow;
	private int myCellCol;
	private String myDefaultState;
	
	/**
	 * Rules for each simulation to be implemented by simulation-specific subclasses.
	 * @param cell: Cell to apply rules to.
	 * @param grid: Simulation grid.
	 */
	public abstract void applyRulesToCell(Cell cell, Grid grid);
	
	/**
	 * Initialize the Grid with the Cells corresponding to this simulation.
	 * @param grid: Simulation grid.
	 * @param initialStates: String 2D array with the initial states of each cell.
	 */
	public void initGrid(Grid grid, String[][] initialStates) {
		for (int row = 0; row < grid.getNumRows(); row++) {
			for (int col = 0; col < grid.getNumCols(); col++) {
				Cell cell = createCell(initialStates[row][col], row, col);
				grid.addCellToGrid(row, col, cell);
				increaseStateCount(cell.getCurState());
			}
		}
	}
	
	/**
	 * Creates the type of Cell corresponding to the correct simulation.
	 * @param initialState: initial state of the Cell.
	 * @param row: row of the initial location of the Cell.
	 * @param col: column of the initial location of the Cell.
	 * @return
	 */
	public Cell createCell(String initialState, int row, int col) {
		return new StandardCell(initialState, row, col);
	}
	
	/**
	 * Returns the default state for a new cell for the simulation.
	 * @return default state for a new cell.
	 */
	public String getDefault() {
		return myDefaultState;
	}
	
	/**
	 * Gets the list of Cells that need to be updated this round of the Simulation.
	 * @return list of Cells to be updated.
	 */
	public List<Cell> getToBeUpdatedList() {
		return toBeUpdated;
	}
	
	/**
	 * Adds a Cell to the list of Cells that need to be updated this round of the Simulation.
	 * @param cell: Cell to be updated.
	 */
	public void addCellToBeUpdated(Cell cell) {
		if (!toBeUpdated.contains(cell) && cell != null) {
			toBeUpdated.add(cell);
		}
	}
	
	/**
	 * Removes a Cell from the list of Cells that need to be updated this round of the Simulation.
	 * @param cell: Cell that no longer needs to be updated.
	 */
	public void removeCellToBeUpdated(Cell cell) {
		if (toBeUpdated.contains(cell)) {
			toBeUpdated.remove(cell);
		}
	}
	
	/**
	 * Clears the "toBeUpdated" list.
	 */
	public void clearToBeUpdatedList(){
		if (toBeUpdated != null) {
			toBeUpdated.clear();
		}
	}	

	/**
	 * Populates the initial state and colors information for this simulation into two maps.
	 */
	public void populateStatesInfo(){
		myStatesCount = new HashMap<String, Integer>();
		myStatesColors = new HashMap<String, Color>();
		myRulesResources = ResourceBundle.getBundle(DEFAULT_RULES_RESOURCE);
		String ruleName = toString().replace(" ", "");
		String[] states = myRulesResources.getString(ruleName + "States").split(",");
		String[] colors = myRulesResources.getString(ruleName + "Colors").split(",");
		for(int i = 0; i < states.length; i++){
			myStatesCount.put(states[i], 0);
			Color color = Color.web(colors[i]);
			myStatesColors.put(states[i], color);
		}
		myDefaultState = myRulesResources.getString(ruleName + "DefaultState");
		myCellRow = Integer.parseInt(myRulesResources.getString("MyCellRow"));
		myCellCol = Integer.parseInt(myRulesResources.getString("MyCellCol"));
	}
	
	/**
	 * Gets a map containing the number cells in each state.
	 * @return map mapping state to number of cells in that state.
	 */
	public Map<String, Integer> getMyStatesCount() {
		return myStatesCount;
	}

	/**
	 * Gets a map containing the color for each
	 * @return map mapping state to color for that state.
	 */
	public Map<String, Color> getMyStatesColors() {
		return myStatesColors;
	}
	
	/**
	 * Increases the count for the number of cells in a particular state by 1.
	 * @param state: state whose count to increase.
	 */
	public void increaseStateCount(String state) {
		if (!myStatesCount.containsKey(state) && state != null) {
			myStatesCount.put(state, 1);
		} else {
			myStatesCount.put(state, myStatesCount.get(state) + 1);
		}
	}
	
	/**
	 * Decreases the count for the number of cells in a particular state by 1.
	 * @param state: state whose count to decrease.
	 */
	public void decreaseStateCount(String state) {
		if (myStatesCount.containsKey(state) && state != null) {
			int num = myStatesCount.get(state);
			if (num > 0) {
				myStatesCount.put(state, num - 1);
			}
		}
	}

	/**
	 * Updates the state counts when a cell switches state.
	 * @param cell: cell that is switching states.
	 */
	public void updateStateCount(Cell cell) {
		if (cell != null && cell.getNextState() != null) {
			decreaseStateCount(cell.getCurState());
			increaseStateCount(cell.getNextState());
		}
	}
	
	
	/**
	 * Counts the number of neighbors of a certain state.
	 * @param neighborhood: Cell[][] containing neighboring cells to check.
	 * @param state: state of cells to count.
	 * @return number of neighbors of a certain state.
	 */
	protected int countSurroundingNeighborsOfType(Cell[][] neighborhood, String state) {
		int ret = 0;
		for (int row = 0; row < neighborhood.length; row++) {
			for (int col = 0; col < neighborhood[row].length; col++) {
				if (neighborhood[row][col] != null) {
					if (row != myCellRow || col != myCellCol) {
						if (neighborhood[row][col].getCurState().equals(state)) {
							ret++;
						}
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * Switch cell1 and cell2's states, set their next location's for reference, and add to the list of cells to be updated.
	 * @param cell1: first cell to be swapped.
	 * @param cell2: second cell to be swapped.
	 */
	protected void switchCells(Cell cell1, Cell cell2) {
		int cell1Row = cell1.getCurRow();
		int cell1Col = cell1.getCurCol();
		String cell1State = cell1.getCurState();
		
		cell1.setNextLocation(cell2.getCurRow(), cell2.getCurCol());
		cell1.setNextState(cell2.getCurState());
		
		cell2.setNextLocation(cell1Row, cell1Col);
		cell2.setNextState(cell1State);
		
		addCellToBeUpdated(cell1);
		addCellToBeUpdated(cell2);
	}
	
	/**
	 * Checks if the cell is the last one in the grid.
	 * @param cell: cell to check.
	 * @param grid: simulation grid.
	 * @return true if it is the last cell; false otherwise.
	 */
	protected boolean isLastCellInGrid(Cell cell, Grid grid) {
		int offset = 1;
		if (grid.hasBeenResizedThisStep()) {
			offset = offset + 1;
		}
		return (cell.getCurRow() == (grid.getNumRows() - offset)) && (cell.getCurCol() == (grid.getNumCols() - offset));
	}

	/**
	 * Generates a random integer between 0 and (max-1) for indexing into a list.
	 * @param max: size of list you're indexing into.
	 * @return an integer for the random index.
	 */
	protected int generateRandom(int max) {
		return (int) Math.round(Math.random() * (max-1));
	}
	
	/**
	 * Generates a random integer between min and max for indexing into a list.
	 * @param max: size of list you're indexing into.
	 * @return an integer for the random index.
	 */
	protected int generateRandomWithinBounds(int min, int max) {
		return generateRandom(max + 1) + min;
	}
	
	
	/**
	 * Retrieves a list of all the rule parameters
	 * @return
	 * An ArrayList of Strings containing the game parameters
	 */
	public abstract List<String> getParameters();
	
	/**
	 * Returns string name of the Simulation
	 */
	public abstract String toString();
}
