package com.gushuley.utils.scheduler;

import java.util.*;

import javax.management.ObjectInstance;



import org.apache.log4j.Logger;

import com.gushuley.utils.Tools;
import com.gushuley.utils.jmx.*;
import com.gushuley.utils.orm.*;
import com.gushuley.utils.scheduler.dom.*;


public class SchedulerController extends ThreadedService
implements SchedulerControllerMBean 
{	

	private String baseName;
	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String name) {
		baseName = name;
	}

	private JobMBean startJob(JobDef jobDef, Date date) throws InstantiationException, IllegalAccessException, ClassNotFoundException, JmxException {
		JobMBean job =  (JobMBean) Thread.currentThread().getContextClassLoader().loadClass(jobDef.getClassName()).newInstance();
		
		job.setProperties(jobDef.getProperties());
		job.addJobSuccesListener(new JobFinishListener() {
			public void jobFinished(JobMBean job) {
				log.debug("Job finished: " + job.getName());
				SchedulerContext ctx = new SchedulerContext(getDatabaseJdni());
				try {
					Mapper<JobDone, JobDoneKey> mapper = ctx.getMapper(JobDone.class);
					JobDoneKey key = new JobDoneKey(job.getJobId(), job.getDate());
					if (mapper.getById(key) == null) {
						JobDone d = new JobDone(key);
						mapper.add(d);
					}
					ctx.commit();
				}
				catch (Exception ex) {}
				finally {
					ctx.close();
				}
				
			}
		});
		String jobName = baseName + ",jobId=" + jobDef.getKey();
		log.debug("Starting job: " + jobName);

		job.start(jobName, jobDef.getKey(), date);
		return job;
	}
	
	@SuppressWarnings("deprecation")
	public void startJob(String jobId) throws Exception {
		SchedulerContext ctx = new SchedulerContext(getDatabaseJdni());
		try {
			JobDef def = ctx.find(JobDef.class, jobId);
			if (def == null) {
				throw new Exception("Job " + jobId + " not fond.");
			}
			else {
				Calendar today = Calendar.getInstance();
				today.setTime(new Date());
				today.set(Calendar.HOUR_OF_DAY, 0);
				today.set(Calendar.MINUTE, 0);
				today.set(Calendar.SECOND, 0);
				today.set(Calendar.MILLISECOND, 0);
				startJob(def, today.getTime());
			}
			ctx.commit();
		}
		finally {
			ctx.close();
		}
	}

	public void stopJob(String jobId) throws JmxException {
		JobMBean job = ServiceLocator.getObjectInterface(JobMBean.class, baseName + ",jobId=" + jobId);
		if (job != null) {
			job.stopWork();
		}
	}

	public Map<String, JobMBean> getJobs() throws JmxException {
		Collection<ObjectInstance> jobsNames = ServiceLocator.listNames(baseName);
		Map<String, JobMBean> jobs = new HashMap<String, JobMBean>();
		for (ObjectInstance i : jobsNames) {
			jobs.put(i.getObjectName().toString(), ServiceLocator.getObjectInterface(JobMBean.class, i.getObjectName()));
		}
		return jobs;
	}
	
	private String databaseJdni;

	public String getDatabaseJdni() {
		return databaseJdni;
	}

	public void setDatabaseJdni(String databaseJdni) {
		this.databaseJdni = databaseJdni;
	}

	private Logger log = Logger.getLogger(getClass());

	public String getLoggerName() {
		return log.getName();
	}

	public void setLoggerName(String loggerName) {
		log = Logger.getLogger(loggerName);
	}

	@Override
	public void onRunStep() {
		Calendar today = Calendar.getInstance();
		today.setTime(new Date());
		int nowTime = today.get(Calendar.HOUR_OF_DAY) * 100 + today.get(Calendar.MINUTE);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		SchedulerContext ctx = new SchedulerContext(getDatabaseJdni());
		try {
			for (JobDef jobDef : ctx.getMapper(JobDef.class).getAll()) {
				String jobBeanName = getBaseName() + ",jobId=" + jobDef.getKey(); 
				int desiredTime = jobDef.getStartHour() * 100 + jobDef.getStartMinute();

				if ( nowTime >= desiredTime && (
						(jobDef.getType() == JobDef.Type.Weekly && Tools.isBitSetted(jobDef.getDayMask(), today.get(Calendar.DAY_OF_WEEK)))
						|| (jobDef.getType() == JobDef.Type.Monthly && Tools.isBitSetted(jobDef.getDayMask(), today.get(Calendar.DAY_OF_MONTH)))
					))
				{
					JobMBean job = ServiceLocator.getObjectInterface(JobMBean.class, jobBeanName);			
					if (job == null) {
						JobDone done = ((JobDone.Mapper)ctx.getMapper(JobDone.class)).getLastForJob(jobDef.getKey());
						if (done == null || done.getKey().getDate().before(today.getTime())) {
							try {
								job = startJob(jobDef, today.getTime());
							}
							catch (Throwable e) {
								log.error("", e);
							}
						}
					}
				}
			}
			ctx.commit();
		} catch (ORMException e) {
			log.error("", e);
		} catch (JmxException e) {
			log.error("", e);
		}
		finally{
			ctx.close();
		}
	}

	@Override
	public void onStop() {
		log.debug("Stopping jobs");
		try {
			for (JobMBean job : getJobs().values()) {
				job.stopWork();
			}
		} catch (JmxException e) {
			log.error("Error stopping jobs: ", e);
		}			
	}

	@Override
	public void onStart() {
		setSleepTime(1000 * 60);
	}	
}
