package application;

import javafx.concurrent.Task;


/**
 * 
 * @author Adminator
 *Simple task which call updateValue every second, to update GUI time
 */
public class TimeControlTask extends Task<Integer>{

	/**
	 * 
	 */
	@Override
	protected Integer call() throws Exception {
		int time=0;

	    // no longer an infinite loop
	    while (true) {
	        try {
	        	Thread.sleep(1000);
            } catch (InterruptedException interrupted) {
            	break;
            }
	    	time++;
	    	updateValue(time);	
	    }
	    return time;
	}

/*//Blob code, here for later usage in class
 * 
    @Override 
    protected void cancelled() {
        super.cancelled();
    }
    
	

    @Override 
    protected void succeeded() {
        super.succeeded();
        updateMessage("Done!");
    }

    @Override 
    protected void failed() {
        super.failed();    
        updateMessage("Failed!");
       }
 */
	
}
