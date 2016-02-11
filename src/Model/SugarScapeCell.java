package Model;

public class SugarScapeCell extends Cell {
	private int mySugar;
	private int myMaxSugarCapacity;
	private SugarScapeAgent myAgent;
	private int myRow;
	private int myCol;
	
	public SugarScapeCell(String initialState, int row, int col, int maxSugar) {
		super(initialState, row, col);
		myAgent = null;
		mySugar = maxSugar;
		myMaxSugarCapacity = maxSugar;
	}
	
	public boolean hasAgent() {
		return myAgent != null; 
	}
	
	public SugarScapeAgent getAgent() {
		return myAgent;
	}
	
	public void setAgent(SugarScapeAgent agent) {
		myAgent = agent;
	}
	
	public void removeAgent() {
		myAgent = null;
	}
	
	public int consumeSugar() {
		int sugar = mySugar;
		mySugar = 0;
		return sugar;
	}
	
	public void addSugar(int sugarGrowBackRate) {
		if (mySugar + sugarGrowBackRate < myMaxSugarCapacity) {
			mySugar += sugarGrowBackRate;
		} else {
			mySugar = myMaxSugarCapacity;
		}
	}
	
	public int getMySugarAmount() {
		return mySugar;
	}
}
