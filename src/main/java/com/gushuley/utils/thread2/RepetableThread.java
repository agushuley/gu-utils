package com.gushuley.utils.thread2;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RepetableThread implements RepeatableThreadControl, RepeatableRunner {
	public RepetableThread() {
		this.runner = null;
	}
	
	public RepetableThread(RepeatableRunner runner) {
		this.runner = runner;
	}

	private Logger log = Logger.getLogger(getClass());
	
	public Logger getLogger() {
		return log;
	}
	
	public void setLogger(Logger log) {
		this.log = log;
	}
	
	protected void onException(Throwable t) {
		log.error(getName() + " Unhandled exception: ", t);
	}

	long iteration = 0;

	public long getIteration() {
		return iteration;
	}
	
	long sleepTime = 1000;

	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}
	
	private final Object monitor = new Object();
	class Runner extends Thread {
		public void run() {
			threadLoop:
			do {
				try {
					if (iteration == Long.MAX_VALUE) {
						iteration = 0;
					}
					iteration++;
					onRunStep();
				} catch (Exception t) {
					onException(t);
				} catch (Error t) {
					onException(t);
				} finally {
					if (!isStop && !isInterrupted()) {
						try {
							if (sleepTime > 0) {
								synchronized (monitor) {
									monitor.wait(sleepTime);
								}
							}
						} catch (InterruptedException e) {
							break threadLoop;
						}
					}
				}
				if (isInterrupted()) {
					break threadLoop;
				}
			} while (!isStop);
			if (isStop) {
				for (ThreadFinishListener l : finishedListeners) {
					l.threadFinished();
				}
				onStop();
				log.info("Exiting thread: " + getName());				
			}
			else {
				lock.lock();
				try {	
					thread = null;
				}
				finally {
					lock.unlock();
				}
				RepetableThread.this.startThread();
			}
		}
		boolean isStop = false;
	}

	public void startWork() {
		startThread();
	}

	protected void startThread() {
		lock.lock();
		try {
			if (thread == null) {
				thread = new Runner();
				if (name != null) {
					thread.setName(getName());
				}
				onStart();
				thread.start();
			}
		}
		finally {
			lock.unlock();
		}
	}

	public void stopWork() {
		lock.lock();
		try {
			if (thread != null) {
				thread.isStop = true;
				thread.interrupt();				
				thread = null;
			}
		}
		finally {
			lock.unlock();
		}
	}

	Runner thread = null;

	final Lock lock = new ReentrantLock();

	private Collection<ThreadFinishListener> finishedListeners = new ArrayList<ThreadFinishListener>();
	public void addThreadFinishedListener(ThreadFinishListener listener) {
		finishedListeners.add(listener);
	}

	public boolean isInterupted() {
		lock.lock();
		try {	
			if (thread != null) {
				return thread.isInterrupted();
			}
			return true;
		}
		finally {
			lock.unlock();
		}
	}

	public boolean isStopped() {
		lock.lock();
		try {	
			if (thread != null) {
				return thread.isStop;
			}		
			return true;
		}
		finally {
			lock.unlock();
		}
	}

	public void interruptStep() {
		lock.lock();
		try {	
			if (thread != null) {
				thread.interrupt();
			}
		}
		finally {
			lock.unlock();
		}
	}

	private String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void onStart() {
	}

	public void onStop() {
	}

	private final RepeatableRunner runner;
	public void onRunStep() throws Error {
		if (runner != null) {
			runner.onRunStep();
		}
	}

	public void stopSleep() {
		synchronized (monitor) {
			monitor.notify();
		}
	}
}
