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
		Cell[][] neighborhood = grid.getNeighborhood(cell.getRow(), cell.getCol(), NUM_NEIGHBORS);
		if (neighborIsBurning(neighborhood)) {
			if (Math.random() < myProbCatch) {
				cell.setNextState(BURNING);
			}
		}
	}
	
	private void handleBurningCell(Cell cell) {
		cell.setNextState(EMPTY);
	}

	private boolean neighborIsBurning(Cell[][] neighborhood) {
		return (neighborhood[0][1].getCurState().equals(BURNING) ||
				neighborhood[1][0].getCurState().equals(BURNING) ||
				neighborhood[1][2].getCurState().equals(BURNING) ||
				neighborhood[2][1].getCurState().equals(BURNING));
	}
}
