package Rules;

import Model.Cell;
import Model.ForagingAntsCell;
import Model.Grid;
import javafx.scene.paint.Color;

public class ForagingAntsRules extends Rules {
	private int numTotalAnts;
	private static final String HOME = "HOME";
	private static final String GROUND = "GROUND";
	private static final String OBSTACLE = "OBSTACLE";
	private static final String FOOD = "FOOD";
	
	@Override
	protected Cell createCell(String initialState, int row, int col) {
		return new ForagingAntsCell(initialState, row, col, numTotalAnts);
	}

	public void applyRulesToCell(ForagingAntsCell cell, Grid grid) {
		int numAnts = cell.getNumAnts();
		
		for (int i = 0; i < numAnts; i++) {
			handleAnt(cell, grid);
		}
	}
	
	private void handleAnt(ForagingAntsCell cell, Grid grid) {
		
	}

	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		applyRulesToCell((ForagingAntsCell) cell, grid);
	}

	@Override
	public Color getFill(String state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "Foraging Ants";
	}

}
