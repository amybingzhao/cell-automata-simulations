package Rules;

import java.util.ArrayList;

import Model.Cell;
import Model.Grid;
import Model.SugarScapeAgent;
import Model.SugarScapeCell;
import javafx.scene.paint.Color;

public abstract class SugarScapeRules extends Rules {
	
	private int mySugarGrowBackRate;
	private int mySugarGrowBackInterval;
	private int mySugarGrowBackCountdown;
	private int myMaxCellSugarCapacity;
	
	public SugarScapeRules(int sugarGrowBackRate, int sugarGrowBackInterval, int maxSugarCapacity) {
		mySugarGrowBackRate = sugarGrowBackRate;
		mySugarGrowBackInterval = sugarGrowBackInterval;
		myMaxCellSugarCapacity = maxSugarCapacity;
	}
	
	@Override
	protected Cell createCell(String initialState, int row, int col) {
		return new SugarScapeCell(initialState, row, col, myMaxCellSugarCapacity);
	}
	
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		SugarScapeCell sugarScapeCell = (SugarScapeCell) cell;
		
		if (sugarScapeCell.hasAgent()) {
			SugarScapeAgent agent = sugarScapeCell.getAgent();
			SugarScapeCell nextPatch = agent.findNextPatch(grid);
			if (nextPatch != null) {
			agent.moveToPatch(sugarScapeCell, nextPatch);
			}
		}
		
		if (isLastCellInGrid(cell, grid)) {
			if (canGrowSugarBack()) {
				growBackSugarInCells(grid);
			}
		}
	}
	
	private void growBackSugarInCells(Grid grid) {
		for (int row = 0; row < grid.getNumRows(); row++) {
			for (int col = 0; col < grid.getNumCols(); col++) {
				SugarScapeCell cell = (SugarScapeCell) grid.getCell(row, col);
				cell.addSugar(mySugarGrowBackRate);
				addCellToBeUpdated(cell);
			}
		}
	}
	
	private boolean canGrowSugarBack() {
		if (mySugarGrowBackCountdown == 0) {
			mySugarGrowBackCountdown = mySugarGrowBackInterval;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Color getFill(String state) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		return "Sugarscape";
	}
	@Override
	public ArrayList<String> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public abstract void applyExtraPresetRules();
	
	
}
