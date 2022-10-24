package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {

	@FXML //<- FXML loader nahraje .fxml soubor i sem a zde uz muzu pracovat s classou, ktera se jmenuje stejne jako id nastaveny necemu pres scene builder
	Label textTimeout;
		
	public void  scrollSize(ActionEvent e) {
		System.out.println("Butt1");
	}
	
	public void countOfFilesPlus(ActionEvent e) {
		System.out.println("Butt1");
	}
	
	public void countOfFilesMinus(ActionEvent e) {
		System.out.println("Butt1");
	}
	
	public void Change(ActionEvent e) {
		System.out.println("Butt1");
	}
	
	public void Start(ActionEvent e) {
		System.out.println("Butt1");
	}
	
	public void Stop(ActionEvent e) {
		System.out.println("Stop");
	}
}
