package com.gushuley.utils.scheduler;


import java.util.*;


import org.jboss.system.ServiceMBean;

import com.gushuley.utils.jmx.JmxException;

public interface SchedulerControllerMBean extends ServiceMBean {
	Map<String, JobMBean> getJobs() throws JmxException;

	String getBaseName();
	void setBaseName(String name);

	String getDatabaseJdni();
	void setDatabaseJdni(String databaseJdni);

	String getLoggerName();
	void setLoggerName(String loggerName);

	void startJob(String jobId) throws Exception;
	void stopJob(String jobId) throws JmxException;
}
