package View;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Stack;

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
import javafx.scene.text.Text;
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
	private ResourceBundle myViewResources;
	private ResourceBundle myRulesResources;

	private VBox rootvbox;
	private VBox stateoptionsvbox;
	private TextField filename;
	private Slider gridsizeslider;
	private Button generate;
	private Map<String, Slider> slidermap;
	private Map<String, CheckBox> checkboxmap;
	private Map<String, ComboBox<String>> configmap;
	private XMLGenerator generator;

	public XMLGeneratorView() {
		Stage stage = new Stage();
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
		filename = new TextField();
		fillVBox(rootvbox);
	}

	private void fillVBox(VBox vbox) {
		Text simulationprompt = new Text("Select Simulation:");
		final ComboBox<String> simulationComboBox = new ComboBox<String>();
		configmap.put("Simulation", simulationComboBox);
		simulationComboBox.setPrefWidth(pixelWidth - 2 * inset);
		simulationComboBox.getItems().addAll(myRulesResources.getString("RuleTypes").split(","));
		simulationComboBox.setValue(simulationComboBox.getItems().get(0));
		simulationComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String oldsim, String newsim) {
				updateStateOptions(newsim);
			}
		});

		Text gridtypeprompt = new Text("Select Grid Type:");
		final ComboBox<String> gridtypeComboBox = new ComboBox<String>();
		configmap.put("GridType", gridtypeComboBox);
		gridtypeComboBox.setPrefWidth(pixelWidth - 2 * inset);
		gridtypeComboBox.getItems().addAll(myRulesResources.getString("GridTypes").split(","));
		gridtypeComboBox.setValue(gridtypeComboBox.getItems().get(0));

		Label gridsizeprompt = new Label("Select Grid Size:");
		gridsizeslider = generateSlider(0, maxGridSize, 15, 10, 5, 3);
		gridsizeslider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				enableGenerate();
				gridsizeprompt.setText("Grid Size: " + Integer.toString(new_val.intValue()));
			}
		});

		Text stateoptionprompt = new Text("Select and change state percentages:");
		Text filenameprompt = new Text("Enter filename:");

		TextField filename = new TextField();

		generate = new Button("Generate");
		generate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				generatePressed();
			}
		});
		generate.setPrefWidth(pixelWidth - 2 * inset);

		vbox.getChildren().addAll(simulationprompt, simulationComboBox, gridtypeprompt, gridtypeComboBox,
				gridsizeprompt, gridsizeslider, stateoptionprompt, stateoptionsvbox, filenameprompt, filename, generate);
	}

	/**
	 * generates a new slider
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	private Slider generateSlider(int min, int max, int current, int majortick, int minortick, int blockinc) {
		Slider slider = new Slider(min, max, current);
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

			Slider slider = generateSlider(0, 100, 0, 50, 5, 10);
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
	}

	/**
	 * 
	 */
	private void enableGenerate(){
		double total = 0;
		for(String k : slidermap.keySet()){
			total += slidermap.get(k).getValue();
		}
		generate.setDisable(total >= 100);
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
		String gridType = (String) configmap.get("GridType").getValue();
		generator.generateFile(sideLength, rules, filename.getText() + ".xml", gridType);

	}

}
