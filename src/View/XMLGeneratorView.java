package View;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import XML.XMLGenerator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class XMLGeneratorView {
	//UI Metrics
	private int pixelWidth;
	private int pixelHeight;
	private int inset;
	private int maxGridSize;

	// resources
	public static final String DEFAULT_VIEW_RESOURCE = "View/View";
	public static final String DEFAULT_RULES_RESOURCE = "Rules/Rules";
	private static final String PARAMETERS = "Parameters";
	private ResourceBundle myViewResources;
	private ResourceBundle myRulesResources;

	private Stage stage;
	private VBox rootvbox;
	private VBox stateoptionsvbox;
	private Slider gridsizeslider;
	private Button generate;
	private Map<String, Slider> slidermap;
	private Map<String, CheckBox> checkboxmap;
	private Map<String, ComboBox<String>> configmap;
	private Map<String, TextField> customparamsmap;
	private XMLGenerator generator;

	public XMLGeneratorView() {
		stage = new Stage();
		stage.setTitle("XML Generator");
		myRulesResources = ResourceBundle.getBundle(DEFAULT_RULES_RESOURCE);
		myViewResources = ResourceBundle.getBundle(DEFAULT_VIEW_RESOURCE);
		pixelWidth = Integer.parseInt(myViewResources.getString("XMLGeneratorWidth"));
		pixelHeight = Integer.parseInt(myViewResources.getString("XMLGeneratorHeight"));
		inset = Integer.parseInt(myViewResources.getString("XMLGeneratorInset"));
		maxGridSize = Integer.parseInt(myViewResources.getString("DefaultMaxCellsDisplayed"));

		Group root = new Group();
		rootvbox = new VBox(10);
		rootvbox.setPadding(new Insets(inset, inset, inset, inset));
		rootvbox.setAlignment(Pos.CENTER);
		root.getChildren().add(rootvbox);
		stage.setScene(new Scene(root, pixelWidth, pixelHeight));
		stage.show();
		setupUI();
	}

	private void setupUI() {
		stateoptionsvbox = new VBox();
		stateoptionsvbox.setPrefWidth(pixelWidth - 2 * inset);
		configmap = new HashMap<String, ComboBox<String>>();
		checkboxmap = new HashMap<String, CheckBox>();
		slidermap = new HashMap<String, Slider>();
		customparamsmap = new HashMap<String, TextField>();
		fillVBox(rootvbox);
	}

	/**
	 * Fills the main vbox with the contents of the generator ui
	 */
	private void fillVBox(VBox vbox) {
		generate = new Button("Generate");
		generate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				generatePressed();
			}
		});
		generate.setPrefWidth(pixelWidth - 2 * inset);
		
		addSimulationUnit(vbox);
		addGridTypeUnit(vbox);
		
		Label gridsizelabel = new Label("Select Grid Size:");
		gridsizeslider = generateSlider(0, maxGridSize, 10, 5, 3);
		gridsizeslider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				enableGenerate();
				gridsizelabel.setText("Grid Size: " + Integer.toString(new_val.intValue()));
			}
		});
		gridsizeslider.setValue(maxGridSize/2);
		
		Text stateoptionprompt = new Text("Set parameters and state percentages:");
		vbox.getChildren().addAll(gridsizelabel, gridsizeslider, stateoptionprompt, stateoptionsvbox, generate);
	}
	
	/**
	 * Adds simulation UI unit to vbox
	 * @param vbox
	 */
	private void addSimulationUnit(VBox vbox){
		Text simulationprompt = new Text("Select Simulation:");
		String[] simulationOptions = myRulesResources.getString("RuleTypes").split(",");
		ComboBox<String> simulationComboBox = generateComboBox("Simulation", simulationOptions);
		vbox.getChildren().addAll(simulationprompt, simulationComboBox);
	}
	
	/**
	 * Adds gridtype UI unit to vbox
	 * @param vbox
	 */
	private void addGridTypeUnit(VBox vbox){
		Text gridtypesprompt = new Text("Select Grid Type:");
		String[] gridTypesOptions = myRulesResources.getString("GridTypes").split(",");
		ComboBox<String> gridTypesComboBox = generateComboBox("GridTypes", gridTypesOptions);
		vbox.getChildren().addAll(gridtypesprompt, gridTypesComboBox);
	}

	/**
	 * Generates a combobox with an event listener
	 * @param name name of combobox
	 * @param options available options to combobox
	 * @return combobox generated
	 */
	private ComboBox<String> generateComboBox(String name, String[] options){
		final ComboBox<String> comboBox = new ComboBox<String>();
		configmap.put(name, comboBox);
		comboBox.setPrefWidth(pixelWidth - 2 * inset);
		comboBox.getItems().addAll(options);
		comboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String oldsim, String newsim) {
				if(name.equals("Simulation")){
					updateStateOptions(newsim);
				} 
			}
		});
		comboBox.setValue(comboBox.getItems().get(0));
		return comboBox;
	}
	
	
	/**
	 * generates a new slider
	 * 
	 * @param min
	 * @param max
	 * @return returns a slider linked to a label
	 */
	private Slider generateSlider(int min, int max, int majortick, int minortick, int blockinc) {
		Slider slider = new Slider(min, max, min);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(majortick);
		slider.setMinorTickCount(minortick);
		slider.setBlockIncrement(blockinc);
		return slider;
	}

	/**
	 * Clears the vbox and populates it with new options
	 * 
	 * @param sim
	 */
	private void updateStateOptions(String sim) {
		stateoptionsvbox.getChildren().clear();
		slidermap.clear();
		for (String s : myRulesResources.getString(sim + "States").split(",")) {
			HBox hbox = new HBox(8);
			CheckBox cb = new CheckBox();
			cb.setText(s);
			cb.setSelected(false);
			checkboxmap.put(s, cb);
			hbox.getChildren().add(cb);

			Slider slider = generateSlider(0, 100, 50, 5, 10);
			slidermap.put(s, slider);
			slider.setDisable(true);
			slider.setMaxWidth(100);
			hbox.getChildren().add(slider);

			Label label = new Label();
			label.setText("0%");
			label.setMaxWidth(50);
			slider.valueProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
					enableGenerate();
					label.setText(Integer.toString(new_val.intValue()) + "%");
				}
			});
			slider.setValue(0);
			hbox.getChildren().add(label);

			cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
				public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
					slider.setValue(0);
					slider.setDisable(!new_val);
					label.setDisable(!new_val);
				}
			});
			stateoptionsvbox.getChildren().add(hbox);
		}
		addCustomParamTextFields(stateoptionsvbox, sim);
		enableGenerate();
	}
	
	/**
	 * adds custom param text fields to vbox
	 * @param vbox
	 */
	private void addCustomParamTextFields(VBox vbox, String rule){
		customparamsmap.clear();
		String[] resourcesParams = myRulesResources.getString(rule + PARAMETERS).split(",");
		if(resourcesParams[0].equals("NONE")) return;
		for(String s : resourcesParams){
			HBox hbox = new HBox(8);
			Label label = new Label(s + ":");
			label.setPrefWidth(300);
			TextField textfield = new TextField();
			customparamsmap.put(s, textfield);
			textfield.textProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
					enableGenerate();
				}
			});
			hbox.getChildren().addAll(label, textfield);
			vbox.getChildren().add(hbox);
		}

	}

	/**
	 * Enables the generate button
	 */
	private void enableGenerate(){
		double total = 0;
		for(String k : slidermap.keySet()){
			total += slidermap.get(k).getValue();
		}
		
		boolean complete = true;
		for(String k: customparamsmap.keySet()){
			if(customparamsmap.get(k).getText().equals("")){
				complete = false;
			}
		}
		generate.setDisable(total > 100 || !complete);
	}
	
	/**
	 * handles the pressing of the generate button
	 */
	private void generatePressed() {
		Map<String, Double> generatorParams = new HashMap<String, Double>();
		for (String state : slidermap.keySet()) {
			generatorParams.put(state, slidermap.get(state).getValue());
		}
		
		generator = new XMLGenerator(generatorParams);
		int sideLength = (int) gridsizeslider.getValue();
		String rules = (String) configmap.get("Simulation").getValue();
		String gridtypes = (String) configmap.get("GridTypes").getValue();
		List<String> params = new ArrayList<String>();
		for(String k: customparamsmap.keySet()){
			params.add(customparamsmap.get(k).getText());
		}
		
		FileChooser myFileChooser = new FileChooser();
		FileChooser.ExtensionFilter myFilter = new FileChooser.ExtensionFilter("XML Files (.xml)", "*.xml");
		myFileChooser.getExtensionFilters().add(myFilter);
		File myFile = myFileChooser.showSaveDialog(stage);
		if(myFile != null){
			generator.generateFile(sideLength, sideLength, rules, gridtypes, params, myFile);
		}
	}
}
