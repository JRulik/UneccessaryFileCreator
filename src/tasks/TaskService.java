package tasks;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class TaskService {
	
	private TextField textTimeOut;
	private TextField textCountOfFiles;
	private TextField textSizeOfFile;
	private TextField textPath;
	private CheckBox checkBoxUnlimited;
	private CheckBox checkBoxTimeOut;
	private CheckBox checkBoxDeleteAfterCreate;	
	
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
	
	public void CreateFileTaskInit() throws Exception {
	 	 parseUIInputs();
	     createFileTask = new FileCreateTask(textPath.getText(),isUnlimitedFiles, isTimeOut, isDeleteAfterCreate, countsOfFiles, time, sizeOfFile);
	     //TODO do lambda expression
	     createFileTask.messageProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> obs, String oldMsg, String newMsg) {
							
					if (createFileTask.isCancelled() || createFileTask.isDone()) {
						if (timeControlTask != null && timeControlTask.isRunning()) {
							timeControlTask.cancel();
						}
						//setLogError(newMsg);
						//setNodesDiabled(false,nodes);
					}
					else {
						//setLogInfo(newMsg);
					}
				}
	     });
	}
	
	public void timeControlTaskInit() {
        timeControlTask = new TimeControlTask();	         
	}
	
	public void runTimeControlTask() {
	     //start timeControlThread
        timeControlThread = new Thread (timeControlTask);
        timeControlThread.setDaemon(true);
        timeControlThread.start();
	}
	
	public TimeControlTask getTimeControlTask() {
		return timeControlTask;
	}
	
	public void runCreateFileTask() {
	     //start crreateFile Task
	     createFileThread = new Thread (createFileTask);
	     createFileThread.setDaemon(true);
	     createFileThread.start();
	}
	
	public FileCreateTask getFileCreateTask() {
		return createFileTask;
	}
	
}
