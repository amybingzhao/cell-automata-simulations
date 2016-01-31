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
}
