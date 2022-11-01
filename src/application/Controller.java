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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
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
import tasks.FileCreateTask;
import tasks.TaskService;
import tasks.TimeControlTask;


/**
 * Main controller of application. Between threads and UI
 * @author Adminator
 *
 */
public class Controller implements Initializable{
	
	/**
	 * FXML components load as object (semiautomatically by @FXML and given name)
	 * (...if there would be better option to load that)
	 */
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
	private CheckBox checkBoxDeleteAfterCreate;	
	@FXML
	private Label labelTime;
	@FXML
	private Label labelMB;
	@FXML
	private Label labelCountOfFiles;
	@FXML
	private Label LabelTimeOut;
	@FXML
	private Label labelPath;
	@FXML
	private Label labelTimeOutSecond;
	@FXML
	private Label labelSizeOfFile;
	@FXML
	private Button buttonStart;
	@FXML
	private Button buttonPlusCountOfFiles;
	@FXML
	private Button buttonMinusCountOfFiles;
	@FXML
	private Button buttonPlusTimeOut;
	@FXML
	private Button buttonMinusTimeOut;
	@FXML
	private Button buttonChangePath;
	@FXML
	private Slider sliderSizeOfFile;
	@FXML
	private ScrollPane scrollPaneTextFlow;
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private TextFlow textAreaLog;
	@FXML
	private Separator separator1;
	@FXML
	private Separator separator2;
	@FXML
	private Separator separator3;	
	
	/**
	 * private variables to save values from UI
	 */
	private boolean isUnlimitedFiles;
	private boolean isTimeOut ;
	private boolean isDeleteAfterCreate;
	private long countsOfFiles ;
	private long time ;
	private long sizeOfFile ;
	
	/**
	 * Task and thread variables to save them out of methods
	 */
	private FileCreateTask createFileTask;
	private TimeControlTask timeControlTask;
	private Thread createFileThread;
	private Thread timeControlThread;
	private Node[] nodes;
	
	private TaskService taskService;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//TODO seems that .setDisable nema vliv na separatory
		nodes = new Node[]{separator1,separator2,separator3,
				labelSizeOfFile,labelTimeOutSecond,labelMB,labelCountOfFiles,LabelTimeOut,labelPath,
				buttonStart,textCountOfFiles,textTimeOut
				,textSizeOfFile, checkBoxTimeOut, checkBoxUnlimited, textPath,buttonPlusCountOfFiles,
				buttonMinusCountOfFiles,buttonMinusTimeOut,buttonPlusTimeOut,
				sliderSizeOfFile,buttonChangePath,checkBoxDeleteAfterCreate};
		
		//listener to make textArea scroll when update
		textAreaLog.getChildren().addListener((ListChangeListener<Node>) ((change) -> { 
                	textAreaLog.layout();
                    scrollPaneTextFlow.layout();
                    scrollPaneTextFlow.setVvalue(1.0f);
                }));
		
		sliderSizeOfFile.setBlockIncrement(100);
		
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
		
		//set path to application as default path 
		textPath.setText(System.getProperty("user.dir")); 
		
	}
	
	
	/**
	 * Disable (or enable) every node in given nodes array on UI
	 * @param disable if true, disable nodes. If false, enable nodes
	 * @param nodes array of nodes to change their disability
	 */
	public void setNodesDiabled(boolean disable, Node[] nodes) {
	    for(Node node : nodes) {
	        node.setDisable(disable);
	    }
	}
	
	/**
	 * Callback method on event on pressing Stop button
	 * @param event
	 */
	public void stop(ActionEvent event) {
		//stopTasks();	
		taskService.stopTasks();
		//set disabled nodes in []nodes enable
		setNodesDiabled(false,nodes);
	}
	
	
	/**
	 * "Main" method - on clicking on Start button load values from UI 
	 * and start FileCreate and TimeControl Tasks.
	 * @param event
	 */
	public void start(ActionEvent event) {
		
		//Stop tasks before init news
		if (taskService != null) {
			taskService.stopTasks();
		}
		
		//Clear logArea and set Time clock
		textAreaLog.getChildren().clear();
		labelTime.setText("00 : 00 : 00");
		
		String path = textPath.getText();
		if(new File(path).exists()) {
			//try-catch used for incorrect inputs in UI fields (parsed in fileCreateTaskInit())
			try{
				taskService = new TaskService(textTimeOut, textCountOfFiles, textSizeOfFile, textPath, checkBoxUnlimited,
						checkBoxTimeOut, checkBoxDeleteAfterCreate);
				taskService.CreateFileTaskInit();
				
				//TODO change to lambda
				//had to add listener here for interaction with UI (can be change to past as parameter)
				taskService.getFileCreateTask().messageProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> obs, String oldMsg, String newMsg) {
						if (taskService.getFileCreateTask().isCancelled() || taskService.getFileCreateTask().isDone()) {
							if (timeControlTask != null && timeControlTask.isRunning()) {
								timeControlTask.cancel();
							}
							setLogError(newMsg);
							setNodesDiabled(false,nodes);
						}
						else {
							setLogInfo(newMsg);
						}
					}
		     });
				
				taskService.timeControlTaskInit();
				//TODO change to lambda
				taskService.getTimeControlTask().valueProperty().addListener(new ChangeListener<Integer>() {
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
				
				taskService.runTimeControlTask();
				taskService.runCreateFileTask();
		 		setNodesDiabled(true,nodes);  
			}
			catch(Exception e) {
				if (e.getMessage().equals("Start declined")) {
					//do nothing, avoid starting thread
				}
				else {
					setLogError("One of parameters is wrong!"); //probably
				}
			}          	           
		}
		else{
			setLogError(" Given folder does not exist! "+textPath.getText() +"\n");
		}
	}

	/**
	 * Write Info text in textArea (black,not bold)
	 * @param msg
	 */
	public void setLogInfo(String msg) {
		Text text = new Text(msg+"\n");
		text.setStyle("-fx-fill: #000000;");
		textAreaLog.getChildren().add(text);
	}
	
	/**
	 * Write Error text in textArea (red,bold)
	 * @param msg
	 */
	public void setLogError(String msg) {
		Text text = new Text(msg+"\n");
		text.setStyle("-fx-fill: #EE4B2B;"
					+ "-fx-font-weight:bold;");
		textAreaLog.getChildren().add(text);
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
	
	
	/**
	 * Listener for "+ count of files" Button. Change text count of files on oldValue+1
	 * @param event
	 */
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
	
	/**
	 * Listener for "- count of files" Button. Change text count of files on oldValue-1
	 * @param event
	 */	
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
	
	
	/**
	 * Listener for "+ time out" Button. Change text timeout on oldValue+1
	 * @param event
	 */	
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
	
	/**
	 * Listener for "- time out" Button. Change text timeout on oldValue-1
	 * @param event
	 */		
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
	
	
	/**
	 * Liestener for checkBoxUnlimited. Set UI textCountOfFiles styles
	 * @param event
	 */
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

	/**
	 * Liestener for checkBoxtimeOutOnOff. Set UI textTimeOut styles
	 * @param event
	 */
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
			textTimeOut.getStyleClass().remove("disabled");
			textTimeOut.getStyleClass().add("enabled");
		}
	}
	


}
