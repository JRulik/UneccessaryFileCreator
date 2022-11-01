package application;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import javafx.concurrent.Task;

public class CreateFileTask extends Task<Long>{


	private final boolean isUnlimitedFiles;
	private final boolean isTimeOut; 
	private final boolean isDestroyAfterCreate;
	private final long countOfFiles;
	private final long time;
	private final long sizeOfFile;
	private final String path;
	private final int threadSleepConstant = 6;
	
	public CreateFileTask(String path, boolean isUnlimitedFiles, boolean isTimeOut, boolean isDestroyAfterCreate,
		long countOfFiles, long time, long sizeOfFile) {
		super();
		this.isUnlimitedFiles = isUnlimitedFiles;
		this.isTimeOut = isTimeOut;
		this.isDestroyAfterCreate = isDestroyAfterCreate;
		
		if(isUnlimitedFiles) {
			this.countOfFiles=Long.MAX_VALUE;
		}else {
			this.countOfFiles = countOfFiles;
		}
		
		this.time = time;
		this.sizeOfFile= sizeOfFile;
		this.path=path;
	}


	private File createFile(final String filename, final long sizeInKiloBytes) throws IOException {
		  File file = new File(path + "\\"+filename);
		  file.createNewFile();
		  
		  RandomAccessFile raf = new RandomAccessFile(file, "rw");
		  raf.setLength(sizeInKiloBytes*1024);
		  raf.close();
		  
		  return file;
		}



	@Override
	protected Long call() throws Exception {
		
		//set sleep Time
		long sleepTime =0;
		if (isTimeOut) {
			sleepTime = time;
		}
		
		long i;
		Instant start,stop;
		for (i=0; i< countOfFiles; i++){
			//start = Instant.now();
			long numberOfFile = i+1;
			File file = createFile(Long.toString(numberOfFile)+"-TestFile", sizeOfFile*1024);
			//stop = Instant.now();
			//TODO -> nekdy vraci nulu, nekdy cas + nastaveny sleeptim?
			//updateMessage( Long.toString(i)+"# file created"+" ["+(double) Duration.between(start, stop).toNanos()/(double) 1000+" ms]");
			String msg;
			msg = "["+java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"))+"] - " + Long.toString(numberOfFile)+"# file created";
			if (isDestroyAfterCreate) {
				file.delete();
				msg+="\n"+"["+java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"))+"] - " + Long.toString(numberOfFile)+"# file deleted";
			}
			updateMessage(msg);
			
			//updateValue(i);
			System.out.println(i);
			
			if (numberOfFile==countOfFiles) {
				Thread.sleep(threadSleepConstant);
				break;
			}
			
            try {
            
            	Thread.sleep(threadSleepConstant+sleepTime*1000);
            } catch (InterruptedException interrupted) {
            	/*
                if (isCancelled()) {
                    updateMessage("Cancelled");
                    break;
                }*/
	        }
            
            if (isCancelled()) {
                break;
            }

		}
		//Nelze tady pouzit -> updatne massage driv nez se puvodni propsie do textArea
		//updateMessage("Finished");
		Thread.sleep(threadSleepConstant);
        return i;
	}
	
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
