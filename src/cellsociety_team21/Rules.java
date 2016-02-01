package cellsociety_team21;

import java.util.ArrayList;
import java.util.List;

public abstract class Rules {
	private List<Cell> toBeUpdated = new ArrayList<Cell>();
	
	public void applyRulesToGrid(Grid grid) {
		for (int r = 0; r < grid.getNumRows(); r++) {
			for (int c = 0; c < grid.getNumCols(); c++) {
				applyRulesToCell(grid.getCell(r, c), grid);
			}
		}
	}
	
	public abstract void applyRulesToCell(Cell cell, Grid grid);

	protected void switchCells(Cell cell1, Cell cell2) {
		int cell1Row = cell1.getRow();
		int cell1Col = cell1.getCol();
		String cell1State = cell1.getCurState();
		
		cell1.setLocation(cell2.getRow(), cell2.getCol());
		cell1.setNextState(cell2.getCurState());
		
		cell2.setLocation(cell1Row, cell1Col);
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
