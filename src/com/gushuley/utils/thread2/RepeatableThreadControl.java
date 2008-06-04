package com.gushuley.utils.thread2;


import org.apache.log4j.Logger;

import com.gushuley.utils.thread2.ThreadFinishListener;


public interface RepeatableThreadControl {
	void addThreadFinishedListener(ThreadFinishListener listener);

	boolean isInterupted();

	boolean isStopped();
	
	void interruptStep();

	long getIteration();

	long getSleepTime();

	void setSleepTime(long sleepTime);
	
	void setLogger(Logger log);
	
	Logger getLogger();
	
	void startWork();
	
	void stopWork();
	
	String getName();
	
	void setName(String name);
	
	void onStart();
	
	void onStop();
	
	void stopSleep();
}
