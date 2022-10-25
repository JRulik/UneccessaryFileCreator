package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class Controller {

	@FXML //<- FXML loader nahraje .fxml soubor i sem a zde uz muzu pracovat s classou, ktera se jmenuje stejne jako id nastaveny necemu pres scene builder
	TextField textTimeOut;
	@FXML
	TextField textCountOfFiles;
	@FXML
	CheckBox checkBoxUnlimited;
	@FXML
	CheckBox checkBoxTimeOut;
	
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
	
	public void change(ActionEvent event) {
		System.out.println("Butt1");
	}
	
	public void start(ActionEvent event) {
		System.out.println("Butt1");
	}
	
	public void stop(ActionEvent event) {
		System.out.println("Stop");
	}
}
