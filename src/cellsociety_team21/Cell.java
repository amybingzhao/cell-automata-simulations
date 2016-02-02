package cellsociety_team21;

public class Cell {
	private String myCurState;
	private String myNextState;
	private int myRow;
	private int myCol;
	
	public Cell(String initialState, int row, int col) {
		myCurState = initialState;
		myRow = row;
		myCol = col;
		myNextState = null;
	}
	
	public int getRow() {
		return myRow;
	}
	
	public void setLocation(int row, int col) {
		myRow = row;
		myCol = col;
	}
	
	public int getCol() {
		return myCol;
	}
	
	public String getCurState() {
		return myCurState;
	}
	
	public void setCurState(String state) {
		myCurState = state;
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
}
