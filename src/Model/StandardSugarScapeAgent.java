package Model;

public class StandardSugarScapeAgent extends SugarScapeAgent {

	public StandardSugarScapeAgent(int initSugar, int metabolism, int vision, int row, int col) {
		super(initSugar, metabolism, vision, row, col);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		return "[my Sugar: " + String.valueOf(this.getMySugarAmount()) + ", myMetabolism: " + String.valueOf(this.getMetabolism()) + ", myVision: " + String.valueOf(this.getVision() + "]");
	}

}
