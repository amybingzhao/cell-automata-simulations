package cellsociety_team21;

public class Cell {
	private String myCurState;
	private String myNextState;
	private int myCurRow;
	private int myCurCol;
	private int myNextRow;
	private int myNextCol;
	private static final int NULL = -1;
	
	public Cell(String initialState, int row, int col) {
		myCurState = initialState;
		myCurRow = row;
		myCurCol = col;
		myNextState = null;
		myNextRow = NULL;
		myNextCol = NULL;
	}

	public int getCurRow() {
		return myCurRow;
	}

	public void setLocation(int row, int col) {
		myCurRow = row;
		myCurCol = col;
	}
	
	public int getCurCol() {
		return myCurCol;
	}
	
	public String getCurState() {
		return myCurState;
	}
	
	public void setCurState(String state) {
		myCurState = state;
	}
	
	public int getNextRow() {
		return myNextRow;
	}
	
	public int getNextCol() {
		return myNextCol;
	}
	
	public void setNextLocation(int row, int col) {
		myNextRow = row;
		myNextCol = col;
	}

	public String getNextState() {
		return myNextState;
	}
	
	public void setNextState(String state) {
		myNextState = state;
	}

	public String toString() {
		return "(" + myCurState + ", " + myNextState + ")";
	}
	
	public void updateState(){
		if (myNextState != null) {
			myCurState = myNextState;
			myNextState = null;
		}
	}
}
