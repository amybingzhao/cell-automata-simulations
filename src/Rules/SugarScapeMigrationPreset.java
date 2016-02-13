package Rules;

import java.util.ResourceBundle;

import Model.Cell;
import Model.Grid;
import Model.StandardSugarScapeAgent;
import Model.SugarScapeAgent;
import Model.SugarScapeCell;

public class SugarScapeMigrationPreset extends SugarScapeRules {
	public static final String DEFAULT_RESOURCE = "Rules/SugarScapeMigrationRules";
	private ResourceBundle myResource = ResourceBundle.getBundle(DEFAULT_RESOURCE);
	private int PRESET_2_VISION_LIMIT = Integer.parseInt(myResource.getString("Preset2VisionLimit"));
	private int PRESET_2_GROW_BACK_INTERVAL = Integer.parseInt(myResource.getString("Preset2GrowBackInterval"));
	
	public SugarScapeMigrationPreset(int sugarGrowBackRate, int sugarGrowBackInterval, int maxSugarCapacity, int sugarLimit,
			int visionLimit, int metabolismLimit) {
		super(sugarGrowBackRate, sugarGrowBackInterval, maxSugarCapacity, sugarLimit, visionLimit, metabolismLimit);
		setMyVisionLimit(PRESET_2_VISION_LIMIT);
		setMySugarGrowBackInterval(PRESET_2_GROW_BACK_INTERVAL);
	}

	/**
	 * Creates a standard sugar scape cell and initializes an agent if it is occupied.
	 */
	@Override
	protected Cell createCell(String initialState, int row, int col) {
		SugarScapeAgent agent = null;
		if (initialState.equals("OCCUPIED")) {
			agent = new StandardSugarScapeAgent(generateRandom(getMyAgentSugarLimit()) + 1, generateRandom(getMyAgentMetabolismLimit()) + 1, generateRandom(getMyAgentVisionLimit()) + 1, row, col);
		}
		return new SugarScapeCell(initialState, row, col, getMyMaxCellSugarCapacity(), agent);
	}
	
	/**
	 * No extra rules for this preset.
	 */
	@Override
	public void applyExtraPresetRules(Cell cell, Grid grid) {
		// None.
	}

}
