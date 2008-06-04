package com.gushuley.utils.scheduler;

import java.util.Date;

import org.apache.log4j.Logger;

import com.gushuley.utils.jmx.*;


public interface JobMBean extends SelfManagedBeanMBean {
	void start(String beanName, String jobId, Date date) throws JmxException;
	Date getDate();
	String getJobId();
	void setProperties(String properties);
	public String getProperties();
	void addJobSuccesListener(JobFinishListener listener);
	void addJobFinishedListener(JobFinishListener listener);
	void setLoggerName(String logger);
	String getLoggerName();
	Logger getLog();
	void stopWork();
}
