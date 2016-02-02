package cellsociety_team21;

public class FireRules extends Rules {
	private static final int NUM_NEIGHBORS = 4;
	private static final String EMPTY = "EMPTY";
	private static final String TREE = "TREE";
	private static final String BURNING = "BURNING";
	private double myProbCatch;
	
	public FireRules(double probCatch) {
		myProbCatch = probCatch;
	}
	
	@Override
	public void applyRulesToCell(Cell cell, Grid grid) {
		String curState = cell.getCurState();

		if (curState.equals(TREE)) {
			handleTreeCell(cell, grid);
		} else if (curState.equals(BURNING)) {
			handleBurningCell(cell);
		}
	}

	private void handleTreeCell(Cell cell, Grid grid) {
		Cell[][] neighborhood = grid.getNeighborhood(cell.getCurRow(), cell.getCurCol(), NUM_NEIGHBORS);
		if (neighborIsBurning(cell, neighborhood, grid)) {
			double x = Math.random();
			System.out.println("My prob catch: " + myProbCatch);
			System.out.println("My random: " + x + "\n");
			if (x < myProbCatch) {
				cell.setNextState(BURNING);
				addCellToBeUpdated(cell);
			}
		}
	}
	
	private void handleBurningCell(Cell cell) {
		cell.setNextState(EMPTY);
		addCellToBeUpdated(cell);
	}

	private boolean neighborIsBurning(Cell cell, Cell[][] neighborhood, Grid grid) {
		System.out.println(neighborhood[1][1].toString() + "\n");
		int cellRow = cell.getCurRow();
		int cellCol = cell.getCurCol();
		
		if (cellCol > 0) {
			if (cellIsBurning(neighborhood[1][0])) {
				return true;
			}
		}
		if (cellRow > 0) {
			if (cellIsBurning(neighborhood[0][1])) {
				return true;
			}
		}
		if (cellCol < (grid.getNumCols() - 1)) {
			if (cellIsBurning(neighborhood[1][2])) {
				return true;
			}
		}
		if (cellRow < (grid.getNumRows() - 1)) {
			if (cellIsBurning(neighborhood[2][1])) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean cellIsBurning(Cell cell) {
		return cell.getCurState().equals(BURNING);
	}
}
