package cellsociety_team21;

import java.util.ArrayList;
import java.util.List;

public abstract class Rules {
	private List<Cell> toBeUpdated = new ArrayList<Cell>();
	
	public void applyRulesToGrid(Grid grid) {
		for (int row = 0; row < grid.getNumRows(); row++) {
			for (int col = 0; col < grid.getNumCols(); col++) {
				applyRulesToCell(grid.getCell(row, col), grid);
			}
		}
		
		System.out.println(toBeUpdated + "\n");
	}
	
	public abstract void applyRulesToCell(Cell cell, Grid grid);

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
	
	public List<Cell> getToBeUpdatedList() {
		return toBeUpdated;
	}
	
	public void addCellToBeUpdated(Cell cell) {
		toBeUpdated.add(cell);
	}
	
	public void removeCellToBeUpdated(Cell cell) {
		toBeUpdated.remove(cell);
	}
}
