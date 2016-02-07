/**
 * @author Amy Zhao
 * Defines the variables and methods for each cell object
 */

package Model;

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

	/**
	 * Gets the current row of the Cell.
	 * @return current row of Cell.
	 */
	public int getCurRow() {
		return myCurRow;
	}

	/**
	 * Sets the current row and column of the Cell.
	 * @param row: current row of the Cell.
	 * @param col: current column of the Cell.
	 */
	public void setLocation(int row, int col) {
		myCurRow = row;
		myCurCol = col;
	}
	
	/**
	 * Gets the current column of the Cell.
	 * @return current column of Cell.
	 */
	public int getCurCol() {
		return myCurCol;
	}
	
	/**
	 * Gets the current state of the Cell.
	 * @return current state of Cell.
	 */
	public String getCurState() {
		return myCurState;
	}
	
	/**
	 * Sets the current state of the Cell.
	 */
	public void setCurState(String state) {
		myCurState = state;
	}
	
	/**
	 * Gets the Cell's anticipated next row.
	 * @return the Cell's next row.
	 */
	public int getNextRow() {
		return myNextRow;
	}
	
	/**
	 * Gets the Cell's anticipated next column.
	 * @return the Cell's next column.
	 */
	public int getNextCol() {
		return myNextCol;
	}
	
	/**
	 * Sets the Cell's anticipated next row and column.
	 * @param row: Cell's anticipated next row.
	 * @param col: Cell's anticipated next column.
	 */
	public void setNextLocation(int row, int col) {
		myNextRow = row;
		myNextCol = col;
	}

	/**
	 * Gets the next state of the Cell.
	 * @return string representing the next state of the Cell.
	 */
	public String getNextState() {
		return myNextState;
	}
	
	/**
	 * Sets the state of the Cell for the next round.
	 * @param state: next state of the Cell.
	 */
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
