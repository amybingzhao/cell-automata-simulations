package Rules;

import java.util.ArrayList;
import java.util.List;
import Model.Cell;
import Model.ForagingAntsCell;
import Model.Grid;
import javafx.scene.paint.Color;
import Model.Ant;

public class ForagingAntsRules extends Rules {
	private int numTotalAnts;
	private static final String HOME = "HOME";
	private static final String GROUND = "GROUND";
	private static final String OBSTACLE = "OBSTACLE";
	private static final String FOOD = "FOOD";
	private static final int NUM_NEIGHBORS = 8;
	private static final Integer[] NORTH = new Integer[]{0, 1};
	private static final Integer[] SOUTH = new Integer[]{2, 1};
	private static final Integer[] WEST = new Integer[]{1, 0};
	private static final Integer[] EAST = new Integer[]{1, 2};
	private static final Integer[] NW = new Integer[]{0, 0};
	private static final Integer[] NE = new Integer[]{0, 2};
	private static final Integer[] SW = new Integer[]{2, 0};
	private static final Integer[] SE = new Integer[]{2, 2};
	private List<Integer[]> myDirections;
	
	
	public ForagingAntsRules(int numAnts) {
		numTotalAnts = numAnts;
		initDirections();
	}
	
	private void initDirections() {
		myDirections = new ArrayList<Integer[]>();
		myDirections.add(NORTH);
		myDirections.add(SOUTH);
		myDirections.add(WEST);
		myDirections.add(EAST);
		myDirections.add(NE);
		myDirections.add(NW);
		myDirections.add(SE);
		myDirections.add(SW);
	}

	@Override
	protected Cell createCell(String initialState, int row, int col) {
		return new ForagingAntsCell(initialState, row, col, numTotalAnts);
	}

	public void applyRulesToCell(ForagingAntsCell cell, Grid grid) {
		List<Ant> ants = cell.getAnts();

		for (int i = 0; i < ants.size(); i++) {
			handleAnt(ants.get(i), cell, grid);
		}
	}
	
	private void handleAnt(Ant ant, ForagingAntsCell cell, Grid grid) {
		Cell[][] neighborhood = grid.getNeighborhood(cell.getCurRow(), cell.getCurCol(), NUM_NEIGHBORS);
		List<Integer[]> directions = getDirectionsToCheck(ant);
		if (ant.hasFood()) {
			ant.returnToNest(cell, neighborhood, directions);
		} else {
			ant.findFoodSource(cell, neighborhood, directions);
		}
	}

	private List<Integer[]> getDirectionsToCheck(Ant ant) {
		List<Integer[]> directions = new ArrayList<Integer[]>();
		directions.addAll(getForwardDirections(ant.getDirection()));

		for (int i = 0; i < myDirections.size(); i++) {
			if (!directions.contains(myDirections.get(i))) {
				directions.add(myDirections.get(i));
			}
		}
		
		return directions;
	}
	
	private List<Integer[]> getForwardDirections(Integer[] curDirection) {
		int curRow = curDirection[0];
		int curCol = curDirection[1];

		List<Integer[]> forwardDirections = new ArrayList<Integer[]>();
		forwardDirections.add(curDirection);
		
		if (curRow == 0) {
			if (curCol == 0) {
				forwardDirections.add(NORTH);
				forwardDirections.add(WEST);
			} else if (curCol == 1) {
				forwardDirections.add(NW);
				forwardDirections.add(NE);
			} else if (curCol == 2) {
				forwardDirections.add(NORTH);
				forwardDirections.add(EAST);
			}
		} else if (curRow == 1) {
			if (curCol == 0) {
				forwardDirections.add(NW);
				forwardDirections.add(SW);
			} else if (curCol == 2) {
				forwardDirections.add(NE);
				forwardDirections.add(SE);
			}
		} else if (curRow == 2) {
			if (curCol == 0) {
				forwardDirections.add(WEST);
				forwardDirections.add(SOUTH);
			} else if (curCol == 1) {
				forwardDirections.add(SW);
				forwardDirections.add(SE);
			} else if (curCol == 2) {
				forwardDirections.add(EAST);
				forwardDirections.add(SOUTH);
			}
		}
		
		return forwardDirections;
	}
	
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		applyRulesToCell((ForagingAntsCell) cell, grid);
	}

	@Override
	public String toString() {
		return "Foraging Ants";
	}

	@Override
	public ArrayList<String> getParameters() {
		ArrayList<String> parameters = new ArrayList<String>();
		return parameters;
	}
}
