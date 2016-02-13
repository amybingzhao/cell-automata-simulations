package View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class XMLGeneratorView {
	private VBox rootvbox;
	private VBox stateoptionsvbox;
	private TextField filename;
	private Slider gridsizeslider;
	
	private int pixelWidth;
	private int pixelHeight;
	private int inset;
	private int maxGridSize;
	
	//resources
	public static final String DEFAULT_VIEW_RESOURCE = "View/View";
	public static final String DEFAULT_RULES_RESOURCE = "Rules/Rules";
	private ResourceBundle myViewResources;
	private ResourceBundle myRulesResources;
	
	private Map<String, Slider> slidermap; 
	private Map<String, CheckBox> checkboxmap;
	private Map<String, ComboBox<String>> configmap;
	private XMLGenerator generator;
	
	public XMLGeneratorView(){
		Stage stage = new Stage();
	    stage.setTitle("XML Generator");
	    myRulesResources = ResourceBundle.getBundle(DEFAULT_RULES_RESOURCE);
	    myViewResources = ResourceBundle.getBundle(DEFAULT_VIEW_RESOURCE);
	    pixelWidth = Integer.parseInt(myViewResources.getString("XMLGeneratorWidth"));
	    pixelHeight = Integer.parseInt(myViewResources.getString("XMLGeneratorHeight"));
	    inset = Integer.parseInt(myViewResources.getString("XMLGeneratorInset"));
	    maxGridSize = Integer.parseInt(myViewResources.getString("MaxCellsDisplayed"));
	    
	    Group root = new Group();
	    rootvbox = new VBox(10);
		rootvbox.setPadding(new Insets(inset, inset, inset, inset));
		rootvbox.setAlignment(Pos.CENTER);
		root.getChildren().add(rootvbox);
	    stage.setScene(new Scene(root, pixelWidth, pixelHeight));
	    stage.show();
	    setupUI();
	}
	
	private void setupUI(){
		stateoptionsvbox = new VBox();
		stateoptionsvbox.setPrefWidth(pixelWidth - 2 * inset);
		configmap = new HashMap<String, ComboBox<String>>();
		checkboxmap = new HashMap<String, CheckBox>();
		slidermap = new HashMap<String, Slider>();
		filename = new TextField();
		fillVBox(rootvbox);
	}
	
    private void fillVBox(VBox vbox){
    	Text simulationprompt= new Text("Select Simulation:");
    	final ComboBox<String> simulationComboBox = new ComboBox<String>();
    	configmap.put("Simulation", simulationComboBox);
    	simulationComboBox.setPrefWidth(pixelWidth - 2 * inset);
        simulationComboBox.getItems().addAll(myRulesResources.getString("RuleTypes").split(","));
        simulationComboBox.setValue(simulationComboBox.getItems().get(0));
        simulationComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldsim, String newsim) {
            	updateStateOptions(newsim);
            }    
        });
        
        Text gridtypeprompt= new Text("Select Grid Type:");
        final ComboBox<String> gridtypeComboBox = new ComboBox<String>();
        configmap.put("GridType", gridtypeComboBox);
        gridtypeComboBox.setPrefWidth(pixelWidth - 2 * inset);
        gridtypeComboBox.getItems().addAll(myRulesResources.getString("GridTypes").split(","));
        gridtypeComboBox.setValue(gridtypeComboBox.getItems().get(0));
      
        Text gridsizeprompt = new Text("Select Grid Size:");
        gridsizeslider = new Slider();
		gridsizeslider.setMin(1);
		gridsizeslider.setMax(maxGridSize);
		gridsizeslider.setValue(10);
		gridsizeslider.setShowTickLabels(true);
		gridsizeslider.setShowTickMarks(true);
		gridsizeslider.setMajorTickUnit(15);
		gridsizeslider.setMinorTickCount(3);
		gridsizeslider.setBlockIncrement(5);
        
        Text stateoptionprompt = new Text("Select and change state percentages:");
        Text filenameprompt = new Text("Enter filename:");
        
        TextField filename = new TextField();
        
        Button button = new Button("Generate");
        button.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        generatePressed();
		    }
		});
        button.setPrefWidth(pixelWidth - 2*inset);
        
        
        vbox.getChildren().addAll(simulationprompt, simulationComboBox, gridtypeprompt, gridtypeComboBox, 
        		gridsizeprompt, gridsizeslider, stateoptionprompt, stateoptionsvbox, filenameprompt, filename, button);
    }
    
    /**
     * Clears the vbox and populates it with new options
     * @param sim
     */
    private void updateStateOptions(String sim){
    	stateoptionsvbox.getChildren().clear();
    	slidermap.clear();
    	for(String s: myRulesResources.getString(sim+"States").split(",")){
    		HBox hbox = new HBox(8);
    		CheckBox cb = new CheckBox();
    		cb.setText(s);
    		cb.setSelected(false);
    		checkboxmap.put(s, cb);
    		hbox.getChildren().add(cb);
    		
    		Slider slider = new Slider();
    		slider.setMin(0);
    		slider.setMax(100);
    		slider.setValue(0);
    		slider.setShowTickLabels(true);
    		slider.setShowTickMarks(true);
    		slider.setMajorTickUnit(50);
    		slider.setMinorTickCount(5);
    		slider.setBlockIncrement(10);
    		slidermap.put(s, slider);
    		slider.setDisable(true);
    		hbox.getChildren().add(slider);
    		
    		cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
    	        public void changed(ObservableValue<? extends Boolean> ov,
    	            Boolean old_val, Boolean new_val) {
    	                slider.setDisable(!new_val);
    	        }
    	    });
    		stateoptionsvbox.getChildren().add(hbox);
    	}
    }
    
    private Map<String, Double> getParams(){
    	Map <String, Double> params = new HashMap<String, Double>();
    	double total = 0;
    	int remainding = checkboxmap.size();
    	for(String state : checkboxmap.keySet()){
    		boolean active = checkboxmap.get(state).isSelected();
    		if(active){
    			params.put(state, slidermap.get(state).getValue());
    			total += slidermap.get(state).getValue();
    		} else {
    			params.put(state, Double.MAX_VALUE);
    			remainding--;
    		}
    	}
    	
    	if(remainding > 0){
    		Stack<Double> randoms = new Stack<Double>();
    		double totalrandoms = 0;
    		for(int i = 0; i < remainding; i++){
    			double temp = Math.random();
    			totalrandoms += temp;
    			randoms.add(temp);
    		}
    		for(String state : params.keySet()){
    			if(params.get(state).equals(Double.MAX_VALUE)){
    				params.put(state, (randoms.pop() / totalrandoms) * (100 - total));
    			}
    		}
    	}
    	
    	return params;
    }
    
    private void generatePressed(){
    	Map<String, Double> generatorParams = getParams();
    	generator = new XMLGenerator(generatorParams);
    	int sideLength = (int) gridsizeslider.getValue();
    	String rules = (String) configmap.get("Simulation").getValue();
    	String gridType = (String) configmap.get("GridType").getValue();
    	/*
    	 * Todos:
    	 * Value checking for above params
    	 * Display and ask for additional sim specific param
    	 * Factor out dialogs/config code
    	 */
    	generator.generateFile(sideLength, rules, filename.getText() + ".xml", gridType);
    	
    }

    	
}
