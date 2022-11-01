package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
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
	@FXML
	private TextFlow textAreaLog;

	@FXML
	private Label labelTime;
	
	@FXML
	private ScrollPane scrollPaneTextFlow;
	


	private CreateFileTask createFileTask;
	private TimeControlTask timeControlTask;
	
	private boolean isUnlimitedFiles;
	private boolean isTimeOut ;
	private long countsOfFiles ;
	private long time ;
	private long sizeOfFile ;
	private Thread createFileThread;
	private Thread timeControlThread;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
				
	      /*
	      //Setting the line spacing between the text objects 
		textAreaLog.setTextAlignment(TextAlignment.JUSTIFY); 
	       
	      //Setting the width  
		textAreaLog.setPrefSize(600, 300); 
	       
	      //Setting the line spacing  
		textAreaLog.setLineSpacing(5.0); 
		*/
		//textAreaLog.setBackground(null); //delam v css
		
		textAreaLog.getChildren().addListener(
                (ListChangeListener<Node>) ((change) -> {
                	textAreaLog.layout();
                    scrollPaneTextFlow.layout();
                    scrollPaneTextFlow.setVvalue(1.0f);
                }));
		
		sliderSizeOfFile.setBlockIncrement(100);
		
		//TODO Slider which snap to ticks on drag
		//TODO change this lambda expresion, maybe with :: operator and new method
		sliderSizeOfFile.valueProperty().addListener(new ChangeListener<Number>() {
						@Override
						public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
							sliderSizeOfFile.setValue((newValue.intValue()));
							sizeOfFile =  ((int) sliderSizeOfFile.getValue()/100*100);
							textSizeOfFile.setText(Long.toString(sizeOfFile));		
							
						}	
				});
		sliderSizeOfFile.setValue(Integer.parseInt(textSizeOfFile.getText()));
		
		//sliderSizeOfFile
		textPath.setText(System.getProperty("user.dir"));
		
	}
	

	public void stop(ActionEvent event) {
		if(createFileTask.isRunning()) {
			createFileTask.cancel();
		}
		if(timeControlTask.isRunning()) {
			timeControlTask.cancel();
		}
	}
	
	public void start(ActionEvent event) {
		

		
		if (createFileTask != null && createFileTask.isRunning()) {
			/*
			System.out.println("Running");
			return;
			*/
			createFileTask.cancel();
		}
		
		if (timeControlTask != null && timeControlTask.isRunning()) {
			timeControlTask.cancel();
		}
		
		//Vycisti log areu
		textAreaLog.getChildren().clear();
		labelTime.setText("00 : 00 : 00");
		
		String path = textPath.getText();
		if(new File(path).exists()) {
			
			//hlavni cyklus START
			try{
				 isUnlimitedFiles = this.checkBoxUnlimited.isSelected();
				 isTimeOut = this.checkBoxTimeOut.isSelected();
				 countsOfFiles = Integer.parseInt(this.textCountOfFiles.getText());
				 time = Integer.parseInt(this.textTimeOut.getText());
				 sizeOfFile = Long.parseLong(this.textSizeOfFile.getText());
				 
				 if(countsOfFiles<0 || time<0 || sizeOfFile<0) {
					 throw new Exception();
				 }
				 
					//TODO predelat false na promennou, provazat s cheklbvoxem destroy after create
		         createFileTask = new CreateFileTask(path,isUnlimitedFiles, isTimeOut, false, countsOfFiles, time, sizeOfFile);
		         
		         
		        // final Instant start = Instant.now();
		         createFileTask.messageProperty().addListener(new ChangeListener<String>() {
						@Override
						public void changed(ObservableValue<? extends String> obs, String oldMsg, String newMsg) {
									
							if (createFileTask.isCancelled()) {
								setLogError("Cancelled!");
							}
							else if (createFileTask.isDone()){
								setLogInfo("Done!");
								if (timeControlTask != null && timeControlTask.isRunning()) {
									timeControlTask.cancel();
								}
							}
							else {
								setLogInfo(newMsg);
							}
						}
				});		         
		         
		         createFileThread = new Thread (createFileTask);
		         createFileThread.setDaemon(true);
		         //start = Instant.now();
		         createFileThread.start();
		         
		         
		         
		 		if (timeControlTask != null && timeControlTask.isRunning()) {
					/*
					System.out.println("Running");
					return;
					*/
		 			timeControlTask.cancel();
				}
		         
		         timeControlTask = new TimeControlTask();	         
		         timeControlTask.valueProperty().addListener(new ChangeListener<Integer>() {

					@Override
					public void changed(ObservableValue<? extends Integer> list, Integer oldValue, Integer newValue) {
						
						int hod = newValue/(60*24);
						int min = (newValue%(60*24))/60;
						int sec = (newValue%(60*24))%(60);
						
						labelTime.setText(String.format("%02d", hod)+ 
								" : "+ String.format("%02d", min)+
								" : "+String.format("%02d", sec));	
					}
		    
		         });
		         
		         timeControlThread = new Thread (timeControlTask);
		         timeControlThread.setDaemon(true);
		         //start = Instant.now();
		         timeControlThread.start();
				 
			}
			catch(Exception e) {
				setLogError("One of parameters is wrong!");
			}          	           
		}
		else{
			setLogError(" Given folder does not exist! "+textPath.getText() +"\n");
		}
		
		
		
		
	}

	
	public void setLogInfo(String msg) {
		//String text = textAreaLog.getText();
		Text text = new Text(msg+"\n");
		//text.setFill(Color.RED);
		text.setStyle("-fx-fill: #000000;");
		//text.setFont(Font.font("Calibri", FontPosture.REGULAR, 16));
		textAreaLog.getChildren().add(text);
		//textAreaLog.setStyle("-fx-text-fill: red ;") ;
	}
	
	public void setLogError(String msg) {
		//String text = textAreaLog.getText();
		Text text = new Text(msg+"\n");
		//text.setFill(Color.RED);
		text.setStyle("-fx-fill: #EE4B2B;"
					+ "-fx-font-weight:bold;");
		//text.setFont(Font.font("Calibri", FontPosture.REGULAR, 16));
		textAreaLog.getChildren().add(text);
		//textAreaLog.setStyle("-fx-text-fill: red ;") ;
	}


	
	/**
	 * Listener for Change Directory Button. Open Folder Explorer for choose directory where 
	 * files will be created. Default folder is folder where app was started.
	 * @param event
	 */
	public void changeDirectory(ActionEvent event) {
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		Stage stage = (Stage) anchorPane.getScene().getWindow();
		directoryChooser.setTitle("Select folder to create files");
		
		//Set default directory (default direcotry where program was started)
		directoryChooser.setInitialDirectory(new File (System.getProperty("user.dir")));
		File file = directoryChooser.showDialog(stage);
		
		//check if directory was choosen
		if (file != null) {
			//set directory path to directory label
			textPath.setText(file.getAbsolutePath());
		}
		
	}
	
	/*
	//TODO dragAndDrop on slide isnt implemented, this should be that listener
	public void  scrollSize(ActionEvent event) {
		System.out.println("Butt1");
	}*/
	
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
	


}
