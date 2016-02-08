/**
 * @author Austin Wu
 * The Main file that starts the simulation
 */

package Controller;

import View.CSView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    public static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 100;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND; 
    
    private Simulation mySimulation;
    private CSView myView;

    /**
     * Set things up at the beginning.
     * Create model, create view, assign them to each other.
     * 
     */
    @Override
    public void start (Stage stage) {
    	
        mySimulation = new Simulation();
        myView = new CSView(mySimulation);
        
        stage.setTitle(mySimulation.getTitle());

//        Scene scene = mySimulation.init(BOARDSIZE, WIDTH, HEIGHT, stage);
        Scene scene = myView.getScene(stage);
        stage.setScene(scene);
        stage.show();
        
        // sets the simulation's loop
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                                      e -> mySimulation.step(SECOND_DELAY, false));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
    	launch(args);
    }
}
