package Rules;

import java.util.ArrayList;

import Model.Cell;
import Model.Grid;
import Model.StandardSugarScapeAgent;
import Model.SugarScapeAgent;
import Model.SugarScapeCell;
import javafx.scene.paint.Color;

public abstract class SugarScapeRules extends Rules {
	
	private int mySugarGrowBackRate;
	private int mySugarGrowBackInterval;
	private int mySugarGrowBackCountdown;
	private int myMaxCellSugarCapacity;
	private int myAgentSugarLimit;
	private int myAgentVisionLimit;
	private int myAgentMetabolismLimit;
	
	public SugarScapeRules(int sugarGrowBackRate, int sugarGrowBackInterval, int maxSugarCapacity, int sugarLimit, int visionLimit, int metabolismLimit) {
		mySugarGrowBackRate = sugarGrowBackRate;
		mySugarGrowBackInterval = sugarGrowBackInterval;
		mySugarGrowBackCountdown = sugarGrowBackInterval;
		setMyMaxCellSugarCapacity(maxSugarCapacity);
		setMyAgentSugarLimit(sugarLimit);
		setMyAgentVisionLimit(visionLimit);
		setMyAgentMetabolismLimit(metabolismLimit);
	}
	
	@Override
	protected abstract Cell createCell(String initialState, int row, int col);
	
	public void setMySugarGrowBackInterval(int interval) {
		mySugarGrowBackInterval = interval;
	}
	
	public void setMyVisionLimit(int limit) {
		setMyAgentVisionLimit(limit);
	}
	
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		SugarScapeCell sugarScapeCell = (SugarScapeCell) cell;
		
		if (sugarScapeCell.hasAgent()) {
			SugarScapeAgent agent = sugarScapeCell.getAgent();
			if (agent.hasNotMoved()) {
				SugarScapeCell nextPatch = agent.findNextPatch(grid);
				if (nextPatch != null) {
					agent.moveToPatch(sugarScapeCell, nextPatch);
				}
			}
		}
		
		if (isLastCellInGrid(cell, grid)) {
			System.out.println("sugar growback coutndown: " + mySugarGrowBackCountdown);
			if (canGrowSugarBack()) {
				growBackSugarInCells(grid);
			} else {
				mySugarGrowBackCountdown--;
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
			System.out.println("hello");
			mySugarGrowBackCountdown = mySugarGrowBackInterval;
			return true;
		} else {
			return false;
		}
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
	
	public abstract void applyExtraPresetRules(Cell cell, Grid grid);

	public int getMyAgentSugarLimit() {
		return myAgentSugarLimit;
	}

	public void setMyAgentSugarLimit(int myAgentSugarLimit) {
		this.myAgentSugarLimit = myAgentSugarLimit;
	}

	public int getMyMaxCellSugarCapacity() {
		return myMaxCellSugarCapacity;
	}

	public void setMyMaxCellSugarCapacity(int myMaxCellSugarCapacity) {
		this.myMaxCellSugarCapacity = myMaxCellSugarCapacity;
	}

	public int getMyAgentMetabolismLimit() {
		return myAgentMetabolismLimit;
	}

	public void setMyAgentMetabolismLimit(int myAgentMetabolismLimit) {
		this.myAgentMetabolismLimit = myAgentMetabolismLimit;
	}

	public int getMyAgentVisionLimit() {
		return myAgentVisionLimit;
	}

	public void setMyAgentVisionLimit(int myAgentVisionLimit) {
		this.myAgentVisionLimit = myAgentVisionLimit;
	}
}
