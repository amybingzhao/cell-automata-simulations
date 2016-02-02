package cellsociety_team21;

public class Cell {
	private String myCurState;
	private String myNextState;
	private int myRow;
	private int myCol;
	
	public Cell(String initialState, int r, int c) {
		myCurState = initialState;
		myRow = r;
		myCol = c;
		myNextState = null;
	}
	
	public int getRow() {
		return myRow;
	}
	
	public void setLocation(int r, int c) {
		myRow = r;
		myCol = c;
	}
	
	public int getCol() {
		return myCol;
	}
	
	public String getCurState() {
		return myCurState;
	}
	
	public void setCurState(String s) {
		myCurState = s;
	}
	
	public String getNextState() {
		return myNextState;
	}
	
	public void setNextState(String s) {
		myNextState = s;
	}
	
	/**
	 * Sets myCurState to myNextState
	 * Sets myNextState back to null
	 */
	public void updateState(){
		myCurState = myNextState;
		myNextState = null;
	}
}
