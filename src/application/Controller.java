package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Controller implements Initializable{

	@FXML //<- FXML loader nahraje .fxml soubor i sem a zde uz muzu pracovat s classou, ktera se jmenuje stejne jako id nastaveny necemu pres scene builder
	private TextField textTimeOut;
	@FXML
	private TextField textCountOfFiles;
	@FXML
	private TextField textSizeOfFile;
	@FXML
	private TextField textPath;
	@FXML
	private CheckBox checkBoxUnlimited;
	@FXML
	private CheckBox checkBoxTimeOut;
	@FXML
	private Slider sliderSizeOfFile;
	@FXML
	private AnchorPane anchorPane;

	private int sizeOfFile;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
				
		sliderSizeOfFile.setBlockIncrement(100);
		
		//TODO Slider which snap to ticks on drag
		//TODO change this lambda expresion, maybe with :: operator and new method
		sliderSizeOfFile.valueProperty().addListener(new ChangeListener<Number>() {

						@Override
						public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
							sliderSizeOfFile.setValue((newValue.intValue()));
							sizeOfFile =   (int) sliderSizeOfFile.getValue();
							textSizeOfFile.setText(Integer.toString(sizeOfFile));		
							
						}	
				});
		
		//sliderSizeOfFile
		textPath.setText(System.getProperty("user.dir"));
		
	}
	
	
	public void changeDirectory(ActionEvent event) {
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		Stage stage = (Stage) anchorPane.getScene().getWindow();
		directoryChooser.setTitle("Select folder to create files");
		//fileChooser.setInitialDirectory(null);
		directoryChooser.setInitialDirectory(new File (System.getProperty("user.dir")));
		File file = directoryChooser.showDialog(stage);
		
		if (file != null) {
			textPath.setText(file.getAbsolutePath());
		}
		
	}
	
	public void  scrollSize(ActionEvent event) {
		System.out.println("Butt1");
	}
	
	public void countOfFilesPlus(ActionEvent event) {
	
		if (!this.checkBoxUnlimited.isSelected()) {
			try {
				int count = Integer.parseInt(textCountOfFiles.getText());
				count++;
				textCountOfFiles.setText(Integer.toString(count));
			}
			catch (NumberFormatException e) {
				textCountOfFiles.setText("ERR");
			}
			catch (Exception e) {
				System.out.println(e);
			}		
		}

		
	}
	
	public void countOfFilesMinus(ActionEvent event) {
		
		if (!this.checkBoxUnlimited.isSelected()) {
			try {
				int count = Integer.parseInt(textCountOfFiles.getText());
				if (count>1) {
					count--;
					textCountOfFiles.setText(Integer.toString(count));
				}
			}
			catch (NumberFormatException e) {
				textCountOfFiles.setText("ERR");
			}
			catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	public void timeOutPlus(ActionEvent event) {
	
		if (this.checkBoxTimeOut.isSelected()) {
			try {
				int count = Integer.parseInt(textTimeOut.getText());
				count++;
				textTimeOut.setText(Integer.toString(count));
			}
			catch (NumberFormatException e) {
				textTimeOut.setText("ERR");
			}
			catch (Exception e) {
				System.out.println(e);
			}
		}
		
	}
	
	public void timeOutMinus(ActionEvent event) {
		
		if (this.checkBoxTimeOut.isSelected()) {
			try {
				int count = Integer.parseInt(textTimeOut.getText());
				if (count>0) {
					count--;
					textTimeOut.setText(Integer.toString(count));
				}
			}
			catch (NumberFormatException e) {
				textTimeOut.setText("ERR");
			}
			catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	public void unlimitedOnOff(ActionEvent event) {	
		if ( this.checkBoxUnlimited.isSelected()) {
			textCountOfFiles.setEditable(false);
			textCountOfFiles.setText("\u221e");
			textCountOfFiles.getStyleClass().remove("enabled");
			textCountOfFiles.getStyleClass().add("disabled");
			
		}
		else {
			textCountOfFiles.setEditable(true);
			textCountOfFiles.setText("1");
			textCountOfFiles.getStyleClass().remove("disabled");
			textCountOfFiles.getStyleClass().add("enabled");
		}
	}
	
	public void timeOutOnOff(ActionEvent event) {
		textTimeOut.getStyleClass().remove("textTimeOut");
		if (!this.checkBoxTimeOut.isSelected()) {
			textTimeOut.setText("0");
			textTimeOut.setEditable(false);
			textTimeOut.getStyleClass().remove("enabled");
			textTimeOut.getStyleClass().add("disabled");
			
		}
		else {
			textTimeOut.setEditable(true);
			//textTimeOut.setText("0");
			textTimeOut.getStyleClass().remove("disabled");
			textTimeOut.getStyleClass().add("enabled");
		}
	}
	

	
	public void start(ActionEvent event) {
		System.out.println("Butt1");
	}
	
	public void stop(ActionEvent event) {
		System.out.println("Stop");
	}

}
