/**
 * @author Amy Zhao
 * Defines the variables and methods for a standard sugar scape agent object.
 */

package Model;

public class StandardSugarScapeAgent extends SugarScapeAgent {

	public StandardSugarScapeAgent(int initSugar, int metabolism, int vision, int row, int col) {
		super(initSugar, metabolism, vision, row, col);
	}
	
	/**
	 * Description of the standard sugar scape agent.
	 */
	public String toString() {
		return "[my Sugar: " + String.valueOf(this.getMySugarAmount()) + ", myMetabolism: " + String.valueOf(this.getMetabolism()) + ", myVision: " + String.valueOf(this.getVision() + "]");
	}

}
