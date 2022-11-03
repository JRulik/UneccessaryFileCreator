package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Main Class to init JAVAFX 
 * @author Adminator
 *
 */
public class Main {
	

	/**
	 * Main method, using lunch metod on GUIManager.class from JavaFX to run main JavaFX thread
	 * (this is made to not need to pass parameters to JVM and due to run .jar only from command line) <-it was not helpful
	 * @param args
	 */
	public static void main(String[] args) {
		GUIManager.main(args);
	}

}
