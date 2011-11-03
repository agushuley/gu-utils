package com.gushuley.utils.scheduler;

import com.gushuley.utils.jmx.JmxException;
import com.gushuley.utils.jmx.SelfManagedBean;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


public abstract class Job extends SelfManagedBean implements JobMBean {
	public Job() {
		this.setLoggerName(getClass().getName());
	}
	
	public Date getDate() {
		return startDate;
	}

	private Date startDate; 
	
	public void start(String beanName, String jobId, Date date) throws JmxException {
		this.startDate = date;
		this.jobId = jobId;
		register(beanName);
	}

	private String jobId;

	public String getJobId() {
		return jobId;
	}
	
	abstract protected void jobTask() throws Exception;

	private class Thread extends java.lang.Thread {
		public void run() {
			try {
				jobTask();
				for (JobFinishListener l : successListeners) {
					l.jobFinished(Job.this);
				}
			} catch (Error e) {
				log.error("Error executing of job " + jobId + ": " + e.getClass() + ": " + e.getMessage(), e);
			} catch (Exception e) {
				log.error("Error executing of job " + jobId + ": " + e.getClass() + ": " + e.getMessage(), e);
			} finally {
				for (JobFinishListener l : finishedListeners) {
					try {
						l.jobFinished(Job.this);
					}
					catch(Exception e) {}
				}
				try {
					unregister();
				} catch (JmxException e) {}
			}
		}
	}
	
	Collection<JobFinishListener> successListeners = new ArrayList<JobFinishListener>();
	public void addJobSuccesListener(JobFinishListener listener) {
		successListeners.add(listener);
	}

	Collection<JobFinishListener> finishedListeners = new ArrayList<JobFinishListener>();
	public void addJobFinishedListener(JobFinishListener listener) {
		finishedListeners.add(listener);
	}

	Thread t;
	
	@Override
	synchronized public void register(String name) throws JmxException {
		super.register(name);
		if (t != null) {
			t.interrupt();
			t = null;
		}
		t = new Thread();
		t.setName(getName());
		t.start();
	}

	private String properties;

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getProperties() {
		return properties;
	}

	@Override
	synchronized public void unregister() throws JmxException {
		if (t != null) {
			t.interrupt();
			t = null;
		}
		super.unregister();
	}

	private Logger log = Logger.getLogger(getClass());
	
	public String getLoggerName() {
		return log.getName();
	}

	public void setLoggerName(String logger) {
		log = Logger.getLogger(logger);
	}

	public Logger getLog() {
		return log;
	}	
		
	public void stopWork() {
		try {
			unregister();
		} catch (JmxException e) {}
	}
}
