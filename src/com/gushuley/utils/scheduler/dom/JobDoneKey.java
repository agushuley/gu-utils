package com.gushuley.utils.scheduler.dom;

import java.util.Date;

import com.gushuley.utils.orm.impl.AbstractKey;


public class JobDoneKey extends AbstractKey {
	public JobDoneKey(String jobId, Date date) {
		this.jobId = jobId;
		this.date = date;
	}

	private Date date;

	public Date getDate() {
		return date;
	}

	private String jobId;

	public String getJobId() {
		return jobId;
	}

	public Object[] getValues() {
		return new Object[] { jobId, date };
	}
}
