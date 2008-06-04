package com.gushuley.utils.scheduler.dom;

import com.gushuley.utils.orm.ORMException;
import com.gushuley.utils.orm.impl.*;

public class JobDone extends GenericORMObject<JobDoneKey> {

	public JobDone(JobDoneKey key) {
		super(key);
	}

	public interface Mapper {
		JobDone getLastForJob(String jobId) throws ORMException;
	}
}
