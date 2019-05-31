package com.gushuley.utils.jmx;


import org.apache.log4j.*;

import com.gushuley.utils.thread2.*;

public class ManagedThread extends SelfManagedBean implements ManagedThreadMBean {
	final RepetableThread thread = new RepetableThread() {
		@Override
		public void onStart() {
			ManagedThread.this.onStart();
		}
		
		@Override
		public void onStop() {
			try {
				ManagedThread.this.onStop();
			}
			finally {
				try {
					unregister();
				} catch (JmxException e) {
					getLogger().warn("Error while unregistering object " + e.getMessage());
				}
			}
		}		

		@Override
		public void onRunStep() throws Error {
			ManagedThread.this.onRunStep();
		}
	};

	private RepeatableRunner runner = null;
	protected void onRunStep() throws Error {
		if (runner != null) {
			runner.onRunStep();
		}
	}
	
	public void addThreadFinishedListener(ThreadFinishListener listener) {
		thread.addThreadFinishedListener(listener);
	}

	public long getIteration() {
		return thread.getIteration();
	}

	public long getSleepTime() {
		return thread.getSleepTime();
	}

	public void interruptStep() {
		thread.interruptStep();
	}

	public boolean isInterupted() {
		return thread.isInterupted();
	}

	public boolean isStopped() {
		return thread.isStopped();
	}

	public void onStart() {
	}

	public void onStop() {
	}

	public void setLogger(Logger log) {
		thread.setLogger(log);
	}

	public Logger getLogger() {
		return thread.getLogger();
	}

	public void setName(String name) {
		thread.setName(name);
	}

	public void setSleepTime(long sleepTime) {
		thread.setSleepTime(sleepTime);
	}

	public void startWork() {
		thread.startWork();
	}

	public void stopWork() {
		thread.stopWork();
	}

	@Override
	public String getName() {
		return thread.getName();
	}

	@Override
	public void register(String newName) throws JmxException {
		super.register(newName);
		thread.setName(newName);
		startWork();
	}

	public void register(String newName, RepeatableRunner runner) throws JmxException {
		this.runner = runner;
		register(newName);
	}

	public void stopSleep() {
		thread.stopSleep();
	}
}
