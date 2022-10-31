package application;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javafx.concurrent.Task;

public class CreateFileTask extends Task<Long>{


	private boolean isUnlimitedFiles;
	private boolean isTimeOut; 
	private boolean isDestroyAfterCreate;
	private long countOfFiles;
	private long time;
	private long sizeOfFile;
	private String path;
	
	public CreateFileTask(String path, boolean isUnlimitedFiles, boolean isTimeOut, boolean isDestroyAfterCreate,
		long countOfFiles, long time, long sizeOfFile) {
		super();
		this.isUnlimitedFiles = isUnlimitedFiles;
		this.isTimeOut = isTimeOut;
		this.isDestroyAfterCreate = isDestroyAfterCreate;
		this.countOfFiles = countOfFiles;
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
		for (i=1; i< countOfFiles+1; i++){
			createFile(Long.toString(i)+"-TestFile", sizeOfFile*1024);
			updateMessage(Long.toString(i)+"# file created");
			//updateValue(i);
			System.out.println(i);
			
            try {
            	Thread.sleep(5+sleepTime*1000);
            } catch (InterruptedException interrupted) {
            	/*
                if (isCancelled()) {
                    updateMessage("Cancelled");
                    break;
                }*/
            }
            if (isCancelled()) {
                updateMessage("Cancelled");
                break;
            }
		}
        
        return (long)0;
	}






	
}
