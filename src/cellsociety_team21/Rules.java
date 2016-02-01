package cellsociety_team21;

public abstract class Rules {
	
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
	}
}
