package application;
	


import resources.*;

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


public class Main extends Application {
	
	/**
	 * Version of application (seen in main bar)
	 */
	private static double version = 0.86;
	
	/**
	 * Main method, using lunch metod from JavaFX to run main JavaFX thread
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	/**
	 * Mandatory method called from JavaFX. Setting up stage. Using .fxml and .css file to style stage. Using icon.
	 * Setting few of parameters of stage (more in .fxml)
	 */
	public void start(Stage stage) throws Exception {
		System.out.println();
		
		System.out.println((this.getClass()));
		System.out.println((this.getClass().getResource("/resources/application.css")));
		
		Parent root = FXMLLoader.load(this.getClass().getResource("/resources/Main.fxml"));
		
		Scene scene = new Scene(root);
		String css = this.getClass().getResource("/resources/application.css").toExternalForm();
		scene.getStylesheets().add(css);
		
		//------------------------------------------------------------------------------------------------- set icon a hlavni label nazev programu na stage
		Image icon = new  Image(Main.class.getResourceAsStream("/resources/icon.png")); //nevim proc se muselo delat slozite, ale nejak neslo jinak
		stage.getIcons().add(icon);
		stage.setTitle("Unecessary file creator "+version);
		
		
		//------------------------------------------------------------------------------------------------- Pridani ikony an scenu
		Image image = new  Image(Main.class.getResourceAsStream("/resources/icon.png")); //nevim proc se muselo delat slozite, ale nejak neslo jinak
		ImageView imageView = new ImageView(image);
		imageView.setX(0);
		imageView.setY(0);

		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
		
	}
	

}
