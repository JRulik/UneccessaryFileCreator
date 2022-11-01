package application;

import javafx.concurrent.Task;

public class TimeControlTask extends Task<Integer>{

	public TimeControlTask() {
		// TODO Auto-generated constructor stub
	}

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
	
/*
    @Override 
    protected void succeeded() {
        super.succeeded();
        updateMessage("Done!");
    }*/

    @Override 
    protected void cancelled() {
        super.cancelled();
        System.out.println("StopWatch thread cancelled");
    }

 /*
    @Override 
    protected void failed() {
        super.failed();    
        updateMessage("Failed!");
       }*/
	
}
