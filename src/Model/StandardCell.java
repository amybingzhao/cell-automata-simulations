/**
 * @author Amy Zhao
 */

package Model;

public class StandardCell extends Cell {
	/**
	 * Constructs a standard cell compatible with most simulations.
	 * @param initialState: initial state of the cell.
	 * @param row: row of the cell.
	 * @param col: column of the cell.
	 */
	public StandardCell(String initialState, int row, int col) {
		super(initialState, row, col);
	}

}
