package com.gushuley.utils.jmx;


import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;

import com.gushuley.utils.thread2.*;

public class ThreadedService extends ServiceMBeanSupport
implements ThreadedServiceMBean {
	final RepetableThread thread = new RepetableThread() {
		@Override
		public void onStart() {
			ThreadedService.this.onStart();
		}

		@Override
		public void onStop() {
			ThreadedService.this.onStop();
		}	
		
		@Override
		public void onRunStep() throws Throwable {
			ThreadedService.this.onRunStep();
		}
	};

	protected void onRunStep() throws Throwable {
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
	protected void startService() throws Exception {
		setName(getServiceName().toString());
		startWork();
	}


	@Override
	protected void stopService() throws Exception {
		stopWork();
	}


	public void stopSleep() {
		thread.stopSleep();
	}
}
