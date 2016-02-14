/**
 * @author Austin Wu
 * The Main file that starts the simulation
 */

package Controller;

import View.CSView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Simulation mySimulation;
    private CSView myView;

    /**
     * Set things up at the beginning.
     * Create model, create view, assign them to each other.
     */
    @Override
    public void start (Stage stage) {
    	
        mySimulation = new Simulation();
        myView = new CSView(mySimulation);
        
        stage.setTitle(mySimulation.getTitle());
        Scene scene = myView.getScene(stage);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
    	launch(args);
    }
}
