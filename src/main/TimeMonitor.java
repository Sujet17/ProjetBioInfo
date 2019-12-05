package main;

import static java.lang.System.nanoTime;

public class TimeMonitor {
	
	private boolean display;
	
	private long start;
	
	private long lastMeasure;
	
	public TimeMonitor() {
		display = true;
		reset();
	}
	
	public void toggleDisplay() {
		if (display)
			display = false;
		else
			display = true;
	}
	
	public void reset() {
		start = nanoTime();
		lastMeasure = start;
	}
	
	public void measure(String msg) {
		long time = nanoTime();
		if (display) 
			System.out.println(msg + " : "+milliSeconds(time-lastMeasure)+" ; Temps total : "+ milliSeconds(time-start));
		lastMeasure = time;
	}
	
	public String milliSeconds(long nanoSeconds) {
		return Integer.toString((int) (nanoSeconds/1000000));
	}
}
