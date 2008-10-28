package com.gushuley.utils.scheduler.dom;

import java.util.Collection;
import java.util.Date;

import com.gushuley.utils.orm.ORMException;
import com.gushuley.utils.orm.impl.*;

public class JobDone 
extends GenericORMObject<Integer> 
{
	public interface Mapper {
		Collection<JobDone> getJobDoneDayAndAfter(final Date date, final String jobId) throws ORMException;
	}
	
	public JobDone(int key) {
		super(key);
	}

	public JobDone(Integer create, Date date2, String name) {
		this(create);
		this.date = date2;
		this.jobId = name;
	}

	private Date date;

	public Date getDate() {
		return date;
	}

	private String jobId;

	public String getJobId() {
		return jobId;
	}

	public void setDate(Date date) {
		checkRo("date");
		this.date = date;
	}

	public void setJobId(String jobId) {
		checkRo("job_id");
		this.jobId = jobId;
	}
}
