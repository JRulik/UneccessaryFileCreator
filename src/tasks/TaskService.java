package tasks;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 * Class to service interface between main thread (javaFX thread) and background threads
 * @author Adminator
 *
 */
public class TaskService {
	
	/**
	 * Params needed to check User input in UI. Given in constructor from main thread.
	 */
	private TextField textTimeOut;
	private TextField textCountOfFiles;
	private TextField textSizeOfFile;
	private TextField textPath;
	private CheckBox checkBoxUnlimited;
	private CheckBox checkBoxTimeOut;
	private CheckBox checkBoxDeleteAfterCreate;	
	
	/**
	 * Task and thread variables to save them out of methods
	 */
	private FileCreateTask createFileTask;
	private TimeControlTask timeControlTask;
	private Thread createFileThread;
	private Thread timeControlThread;
	
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
	 * Constructor of Task service thread
	 * @param textTimeOut TextField from UI (JAVAFX)
	 * @param textCountOfFiles  TextField from UI (JAVAFX)
	 * @param textSizeOfFile  TextField from UI (JAVAFX)
	 * @param textPath  TextField from UI (JAVAFX)
	 * @param checkBoxUnlimited  CheckBox from UI (JAVAFX)
	 * @param checkBoxTimeOut CheckBox from UI (JAVAFX)
	 * @param checkBoxDeleteAfterCreate CheckBox from UI (JAVAFX)
	 */
	public TaskService(TextField textTimeOut, TextField textCountOfFiles, TextField textSizeOfFile, TextField textPath,
			CheckBox checkBoxUnlimited, CheckBox checkBoxTimeOut, CheckBox checkBoxDeleteAfterCreate) {
		super();
		this.textTimeOut = textTimeOut;
		this.textCountOfFiles = textCountOfFiles;
		this.textSizeOfFile = textSizeOfFile;
		this.textPath = textPath;
		this.checkBoxUnlimited = checkBoxUnlimited;
		this.checkBoxTimeOut = checkBoxTimeOut;
		this.checkBoxDeleteAfterCreate = checkBoxDeleteAfterCreate;
	}

	/**
	 * Stop Tasks if running
	 */
	public void stopTasks() {
		//cancel creatiFile and timeControl Tasks
		if(createFileTask != null && createFileTask.isRunning()) {
			createFileTask.cancel();
		}
		if(timeControlTask != null && timeControlTask.isRunning()) {
			timeControlTask.cancel();
		}	
	}
	
	/**
	 * Callback method on event on pressing Stop button
	 * @param event
	 */
	public void stop(ActionEvent event) {
		stopTasks();	
	}
	
	/**
	 * Parse user input from UI elements given in constructor
	 * @throws Exception if something is not able to be parsed 
	 */
	public void parseUIInputs() throws Exception {
		 isUnlimitedFiles = this.checkBoxUnlimited.isSelected();
		 isTimeOut = this.checkBoxTimeOut.isSelected();
		 isDeleteAfterCreate = this.checkBoxDeleteAfterCreate.isSelected();
		 time = Integer.parseInt(this.textTimeOut.getText());
		 
		 //Show alert to confirm settings unilimetd files and 0 timelimit
		 if (isUnlimitedFiles && (!isTimeOut || time==0)) {
			 Alert alert = new Alert(AlertType.WARNING, "Are you sure you want unlimited files and no timeout? This can use your disk capacity and lower its lifespan", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
			 alert.showAndWait();
			 if (alert.getResult() != ButtonType.YES) {
				   throw new Exception("Start declined");
				}
		 }
		 
		 //check "∞" symbol in UI field for number of files (generated automaticaly when checkBoxUnlimited is checked) for parse purpose
		 if (this.textCountOfFiles.getText().equals("∞")) {			 
			 countsOfFiles = 0; //<- check is also done in createTask with isUnlimited param, this value is only for it is
			 isUnlimitedFiles = true;
		 }
		 else {
			 countsOfFiles = Integer.parseInt(this.textCountOfFiles.getText());
		 }
		 
		 sizeOfFile = Long.parseLong(this.textSizeOfFile.getText());
		 
		 //check bad inputs but in Integer range
		 if(countsOfFiles<0 || time<0 || sizeOfFile<0) {
			 throw new Exception();
		 }
	}	
	
	/**
	 * Create Task to creating files. Parse inputs from UI (therefore throws Exception)
	 * @throws Exception
	 */
	public void CreateFileTaskInit() throws Exception {
	 	 parseUIInputs();
	     createFileTask = new FileCreateTask(textPath.getText(),isUnlimitedFiles, isTimeOut, isDeleteAfterCreate, countsOfFiles, time, sizeOfFile);
	}
	
	/**
	 * Create Task to control time (timewatch)
	 */
	public void timeControlTaskInit() {
        timeControlTask = new TimeControlTask();	         
	}
	

	/**
	 * start Task to control time (timewatch)
	 */
	public void runTimeControlTask() {
	     //start timeControlThread
        timeControlThread = new Thread (timeControlTask);
        timeControlThread.setDaemon(true);
        timeControlThread.start();
	}
	
	/**
	 * return instance of timeControlTask
	 * @return timeControlTask
	 */
	public TimeControlTask getTimeControlTask() {
		return timeControlTask;
	}
	
	/**
	 * start Task to creating files
	 */
	public void runCreateFileTask() {
	     //start crreateFile Task
	     createFileThread = new Thread (createFileTask);
	     createFileThread.setDaemon(true);
	     createFileThread.start();
	}
	
	/**
	 * return instance of createFileTask
	 * @return createFileTask
	 */
	public FileCreateTask getFileCreateTask() {
		return createFileTask;
	}
	
}
