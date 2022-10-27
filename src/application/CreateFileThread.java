package application;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CreateFileThread extends Thread{

	
	private boolean unlimitedFiles;
	private boolean timeOut; 
	private boolean destroyAfterCreate;
	private long countOfFiles;
	private long time;
	
	public CreateFileThread(boolean unlimitedFiles, boolean timeOut,boolean destroyAfterCreate, long countOfFiles, long time) {
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		
	}
	
	private File createFile(final String filename, final long sizeInKiloBytes) throws IOException {
		  File file = new File(filename);
		  file.createNewFile();
		  
		  RandomAccessFile raf = new RandomAccessFile(file, "rw");
		  raf.setLength(sizeInKiloBytes*1024);
		  raf.close();
		  
		  return file;
		}
	
}
