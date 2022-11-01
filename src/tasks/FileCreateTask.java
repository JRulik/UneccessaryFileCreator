package tasks;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import javafx.concurrent.Task;

/**
 * 
 * @author Adminator
 *Task to create file with given size, path and other settings from GUI
 */
public class FileCreateTask extends Task<Long>{


	/**
	 * Private parameters for thread which values are given in constructor
	 */
	private final boolean isUnlimitedFiles;
	private final boolean isTimeOut; 
	private final boolean isDestroyAfterCreate;
	private final long countOfFiles;
	private final long time;
	private final long sizeOfFile;
	private final String path;
	
	
	/**
	 * Constant value to wait between create file (and update message) -> without some constant, if thread was cancelled 
	 * (and set no timeout between files), message can show mismash on textArea (seems like messages stuck
	 * after each other and then info message+ last error message is shown at once (with error color)
	 * time in ms
	 */
	private final int threadSleepConstant = 15;
	
	/**
	 * Construc of file creating thread
	 * @param path path to created file
	 * @param isUnlimitedFiles boolean if is limited count of file (if true, count is set to long.maxvalue)
	 * @param isTimeOut boolean if is timeout set between creating files.
	 * @param isDestroyAfterCreate boolean if file is deleted after 
	 * @param countOfFiles number of files to create
	 * @param time timout between between creating files.
	 * @param sizeOfFile size of file to create
	 */
	public FileCreateTask(String path, boolean isUnlimitedFiles, boolean isTimeOut, boolean isDestroyAfterCreate,
		long countOfFiles, long time, long sizeOfFile) {
		super();
		this.isUnlimitedFiles = isUnlimitedFiles;
		this.isTimeOut = isTimeOut;
		this.isDestroyAfterCreate = isDestroyAfterCreate;
		
		//check if unlimited number of files is set
		if(isUnlimitedFiles) {
			this.countOfFiles=Long.MAX_VALUE;
		}else {
			this.countOfFiles = countOfFiles;
		}
		
		this.time = time;
		this.sizeOfFile= sizeOfFile;
		this.path=path;
	}

	/**
	 * Create file on path in private variable path with name given in parameters
	 * @param filename name of file
	 * @param sizeInKiloBytes size of file in KB
	 * @return object of created file
	 * @throws IOException
	 */
	private File createFile(final String filename, final long sizeInKiloBytes) throws IOException {
		  File file = new File(path + "\\"+filename);
		  file.createNewFile();
		  
		  RandomAccessFile raf = new RandomAccessFile(file, "rw");
		  raf.setLength(sizeInKiloBytes*1024);
		  raf.close();
		  
		  return file;
		}


	/**
	 * Main method of Task, which is called in Therad.run
	 */
	@Override
	protected Long call() throws Exception {
		
		//set sleep Time if time out between file is set to 0 (or timeout box is unchecked)
		long sleepTime =0;
		if (isTimeOut) {
			sleepTime = time;
		}
		
		long i;
		for (i=0; i< countOfFiles; i++){
			long numberOfFile = i+1;
			File file = createFile(Long.toString(numberOfFile)+"-TestFile", sizeOfFile*1024);
			String msg;
			
			//prepare message
			msg = "["+java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"))+"] - " + Long.toString(numberOfFile)+"# file created";
			
			//delete file if is set in "isDestroyAfterCreate" and append msg
			if (isDestroyAfterCreate) {
				file.delete();
				msg+="\n"+"["+java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"))+"] - " + Long.toString(numberOfFile)+"# file - deleted";
			}
			//update message
			updateMessage(msg);
			Thread.sleep(threadSleepConstant);
			
			//check if it was last file to create. If yes, break loop (and end of thread)
			if (numberOfFile==countOfFiles) {
				break;
			}
			
			//sleep thread with catching interupt (for example when cancelled)
            try {
            	Thread.sleep(sleepTime*1000);
            } 
            //catch to go to check if isCancelled (so cancelled interruption) after this try-catch block
            catch (InterruptedException interrupted) {
            	
	        }
            
            if (isCancelled()) {
                break;
            }

		}
		//Nelze tady pouzit -> updatne massage driv nez se puvodni propsie do textArea
		//updateMessage("Finished");
        return i;
	}
	
	/**
	 * Method  succeeded(), cancelled(), failed() are called after changing thread status (successfully finished, canceled, failed)
	 * Here to call updateMessage to event Listener, which is then checking in which state thread is. This is blob code (cane be done better)
	 * ,but i liked it here.
	 */
	
    @Override 
    protected void succeeded() {
        super.succeeded();
        updateMessage("Done!");
    }

    @Override 
    protected void cancelled() {
        super.cancelled();
        updateMessage("Cancelled!");
    }

    @Override 
    protected void failed() {
        super.failed();    
        updateMessage("Failed!");
       }





	
}
