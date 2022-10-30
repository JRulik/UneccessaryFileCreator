package application;
	


//import java.awt.Image;

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

	
	public static void main(String[] args) {
		System.out.println("TEST");
		launch(args);
		System.out.println();
	}

	@Override
	public void start(Stage stage) throws Exception {
		System.out.println();
		System.out.println(getClass());
		//Group root = new Group ();
		Parent root = FXMLLoader.load(this.getClass().getResource("Main.fxml"));
		//Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
		
		Scene scene = new Scene(root);
		//Scene scene = new Scene(root,Color.rgb(115,230,247));
		//Scene scene = new Scene(root,Color.rgb(105,9,201));
		
		String css = this.getClass().getResource("application.css").toExternalForm();
		scene.getStylesheets().add(css);
		//scene2.getStylesheets().add(css);
		
		//------------------------------------------------------------------------------------------------- set icon a hlavi label nazev programu na stage
		Image icon = new  Image(Main.class.getResourceAsStream("icon.png")); //nevim proc se muselo delat slozite, ale nejak neslo jinak
		stage.getIcons().add(icon);
		stage.setTitle("Unecessary file creator");
		
		
		//------------------------------------------------------------------------------------------------- Pridani ikony an scenu
		Image image = new  Image(Main.class.getResourceAsStream("icon.png")); //nevim proc se muselo delat slozite, ale nejak neslo jinak
		ImageView imageView = new ImageView(image);
		imageView.setX(0);
		imageView.setY(0);
		//root.getChildren().add(imageView);
		
		
		/*--------------------------Group root = new Group ();
		//------------------------------------------------------------ Nastaveni pozdeji reseno pres css a fxml
		//------------------------------------------------------------------------------------------------- Nastaveni Labelu
		Text text = new Text();
		text.setText("Boøek");
		text.setX(50);
		text.setY(50);
		text.setFont(Font.font("Verdana",50));
		text.setFill(Color.DARKBLUE);
		
		root.getchi
		root.getChildren().add(text);
		
		//------------------------------------------------------------------------------------------------- Nastaveni podrtzeni line
		Line line = new Line();
		line.setStartX(50);
		line.setStartY(55);
		line.setEndX(200);
		line.setEndY(55);
		line.setStrokeWidth(2);
		line.setStroke(Color.DARKBLUE);
		line.setOpacity(0.5);
		
		root.getChildren().add(line);			
		
		//------------------------------------------------------------------------------------------------- Pridani ikony an scenu
		Image image = new  Image(Main.class.getResourceAsStream("icon.png")); //nevim proc se muselo delat slozite, ale nejak neslo jinak
		ImageView imageView = new ImageView(image);
		imageView.setX(0);
		imageView.setY(0);
		root.getChildren().add(imageView);
		*/
		
		
		//------------------------------------------------------------------------------------------------- Nastaveni parametru stage
		//stage.setHeight(450);
		//stage.setWidth(460);
		stage.setResizable(false);
		//stage.setFullScreen(true);
		//stage.setX(250);
		//stage.setY(250);
		//----------------------------------------------Zmena exit hlasky a klavesy 
		//stage.setFullScreenExitHint("Musis zmacknout \"q\" k ukonceni full screen");
		//stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("q"));
		
	
		
		stage.setScene(scene);
		stage.show();
		
	}
	

}
