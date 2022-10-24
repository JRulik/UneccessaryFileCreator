package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

	@FXML //<- FXML loader nahraje .fxml soubor i sem a zde uz muzu pracovat s classou, ktera se jmenuje stejne jako id nastaveny necemu pres scene builder
	TextField textTimeOut;
	@FXML
	TextField textCountOfFiles;
		
	public void  scrollSize(ActionEvent event) {
		System.out.println("Butt1");
	}
	
	public void countOfFilesPlus(ActionEvent event) {
	
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
	
	public void countOfFilesMinus(ActionEvent event) {
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
	
	public void timeOutPlus(ActionEvent event) {
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
	
	public void timeOutMinus(ActionEvent event) {
		try {
			int count = Integer.parseInt(textTimeOut.getText());
			if (count>1) {
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
