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
	
	public void setRow(int r) {
		myRow = r;
	}
	
	public int getCol() {
		return myCol;
	}
	
	public void setCol(int c) {
		myCol = c;
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

}
