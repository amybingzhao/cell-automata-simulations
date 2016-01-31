package cellsociety_team21;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class Grid {
	private static List<Cell> toBeUpdated;
	private static Rules mySimRules;
	private static States mySimStates;
	private static Cell[][] myGrid;
	
	/* 
	 * Sets the rules, possible states, grid size, and initial states for the current simulation.
	 * @param: rules is the rules for the specific simulation, as determined by the XMLParser.
	 * @param: states is the states for the specific simulation, as determined by the XMLParser.
	 * @param: rows is the number of rows for the specific simulation, as determined by the XMLParser.
	 * @param: cols is the number of columns for the specific simulation, as determined by the XMLParser.
	 * @param: initialStates is the initialStates for the specific simulation, as determined by the XMLParser, with
	 * each state denoted by an integer. initialStates' size should match that of the Grid.
	 */
	public Grid(Rules rules, States states, int rows, int cols, int[][] initialStates) {
		mySimRules = rules;
		mySimStates = states;
		toBeUpdated = new ArrayList<Cell>();
		myGrid = new Cell[rows][cols];
		init(initialStates);
	}
	
	/*
	 * Initializes the states of each cell in the grid based on XML info.
	 */
	private void init(int[][] initialStates) {
		for (int r = 0; r < getNumRows(); r++) {
			for (int c = 0; c < getNumCols(); c++) {
				Cell cell = new Cell(initialStates[r][c]);
			}
		}
	}
	
	/*
	 * Iterates over each Cell and applies the rules to them.
	 */
	public void applyRulesToCells() {
		for (int r = 0; r < getNumRows(); r++) {
			for (int c = 0; c < getNumCols(); c++) {
				// this.getNeighbors(myGrid[r][c]);
				// apply rules
				
				// add the ones that must be updated to the toBeUpdated list
			}
		}
	}
	
	/*
	 * Iterates over each Cell in the toBeUpdated list 
	 * @param: toUpdate is the list of Cells that must be updated this generation.
	 */
	public void applyStateChangesToCells(List toUpdate) {
		for (int i = 0; i < toUpdate.size(); i++) {
			// apply state changes
		}
	}
	
	// TODO: Do we want to return a Cell[3][3] or do we want to return a Cell[8] with some predetermined order for
	// which neighbors are at which index (i.e. getNeighbors or getNeighborhood?)
	/*
	 * Returns all neighbors of a given cell.
	 * @param: myCell is the cell whose neighbors are being returned
	 */
	private Cell[] getNeighbors(Cell myCell) {
		// get # of neighbors needed from the Rules object (i.e. 4 or 8)
		
		return new Cell[9];
	}
	// TODO: unsure how to render the grid 
	/*
	public Image render() {
		return new Image();
	}*/
	
	private int getNumRows() {
		return myGrid.length;
	}
	
	private int getNumCols() {
		return myGrid[0].length;
	}
	
	public List getToBeUpdatedList() {
		return toBeUpdated;
	}
}
