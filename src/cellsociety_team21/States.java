package cellsociety_team21;

public abstract class States {

		public void updateStates(Grid grid) {
			for (int r = 0; r < grid.getNumRows(); r++) {
				for (int c = 0; c < grid.getNumCols(); c++) {
					updateStateOfCell(grid.getCell(r, c));
				}
			}
		}

		public abstract void updateStateOfCell(Cell cell);
}
