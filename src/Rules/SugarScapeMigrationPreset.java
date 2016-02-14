/**
 * @author Amy Zhao
 * Defines the variables and methods for each rules object for the Sugar Scape simulation's Migration preset.
 */

package Rules;

import java.util.ResourceBundle;

import Model.Cell;
import Model.Grid;
import Model.StandardSugarScapeAgent;
import Model.SugarScapeAgent;

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
	 * No extra rules for this preset.
	 */
	@Override
	public void applyExtraPresetRules(Cell cell, Grid grid) {
		// None.
	}

	/**
	 * Creates agent specific to thie Migration preset.
	 */
	@Override
	protected SugarScapeAgent createPresetAgent(int row, int col) {
		return new StandardSugarScapeAgent(generateRandom(getMyAgentSugarLimit()) + 1, generateRandom(getMyAgentMetabolismLimit()) + 1, generateRandom(getMyAgentVisionLimit()) + 1, row, col);
	}

}
