package Rules;

import Model.Cell;
import Model.Grid;
import Model.StandardSugarScapeAgent;
import Model.SugarScapeAgent;
import Model.SugarScapeCell;

public class SugarScapeMigrationPreset extends SugarScapeRules {
	private static final int PRESET_2_VISION_LIMIT = 10;
	private static final int PRESET_2_GROW_BACK_INTERVAL = 2;
	
	public SugarScapeMigrationPreset(int sugarGrowBackRate, int sugarGrowBackInterval, int maxSugarCapacity, int sugarLimit,
			int visionLimit, int metabolismLimit) {
		super(sugarGrowBackRate, sugarGrowBackInterval, maxSugarCapacity, sugarLimit, visionLimit, metabolismLimit);
		setMyVisionLimit(PRESET_2_VISION_LIMIT);
		setMySugarGrowBackInterval(PRESET_2_GROW_BACK_INTERVAL);
	}

	@Override
	protected Cell createCell(String initialState, int row, int col) {
		SugarScapeAgent agent = null;
		if (initialState.equals("OCCUPIED")) {
			agent = new StandardSugarScapeAgent(generateRandom(getMyAgentSugarLimit()) + 1, generateRandom(getMyAgentMetabolismLimit()) + 1, generateRandom(getMyAgentVisionLimit()) + 1, row, col);
		}
		return new SugarScapeCell(initialState, row, col, getMyMaxCellSugarCapacity(), agent);
	}
	
	@Override
	public void applyExtraPresetRules(Cell cell, Grid grid) {
		// None.
	}

}
