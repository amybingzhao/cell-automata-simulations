package cellsociety_team21;
import java.util.Arrays;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * 
 * @author Austin Wu
 */
public class Main extends Application {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 600;
    public static final int BOARDSIZE = 400;
    public static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 100;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND; 
    
    private Simulation mySimulation;

    /**
     * Set things up at the beginning.
     */
    @Override
    public void start (Stage s) {
    	int gridsize = 10;
        // create your own simulation here
        mySimulation = new Simulation();
        s.setTitle(mySimulation.getTitle());

        // attach simulation to the stage and display it
        Scene scene = mySimulation.init(BOARDSIZE, WIDTH, HEIGHT);
        s.setScene(scene);
        s.show();

        // sets the simulation's loop
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                                      e -> mySimulation.step(SECOND_DELAY));
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
