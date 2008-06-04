package com.gushuley.utils.scheduler.dom;

import com.gushuley.utils.orm.impl.GenericContext;

public class SchedulerContext extends GenericContext {
	public SchedulerContext(String scheduleDbJdni) {
		this.scheduleDbJdni = scheduleDbJdni;
		
		registerMapper(JobDone.class, JobDoneMapper.class);
		registerMapper(JobDef.class, JobDefMapper.class);
	}

	private String scheduleDbJdni;

	public String getScheduleDbJdni() {
		return scheduleDbJdni;
	} 
}
