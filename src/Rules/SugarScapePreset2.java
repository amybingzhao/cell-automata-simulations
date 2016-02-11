package Rules;

public class SugarScapePreset2 extends SugarScapeRules {
	private static final int PRESET_2_VISION_LIMIT = 10;
	private static final int PRESET_2_GROW_BACK_INTERVAL = 2;
	
	public SugarScapePreset2(int sugarGrowBackRate, int sugarGrowBackInterval, int maxSugarCapacity, int sugarLimit,
			int visionLimit, int metabolismLimit) {
		super(sugarGrowBackRate, sugarGrowBackInterval, maxSugarCapacity, sugarLimit, visionLimit, metabolismLimit);
		setMyVisionLimit(PRESET_2_VISION_LIMIT);
		setMySugarGrowBackInterval(PRESET_2_GROW_BACK_INTERVAL);
	}

	@Override
	public void applyExtraPresetRules() {
		// None.
	}

}
