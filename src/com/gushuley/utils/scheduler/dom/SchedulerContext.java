package com.gushuley.utils.scheduler.dom;

import com.gushuley.utils.Tools;
import com.gushuley.utils.orm.impl.GenericContext;
import com.gushuley.utils.orm.sql.SqlDialect;

public class SchedulerContext extends GenericContext {

	public SchedulerContext(String scheduleDbJdni, String scheduler, String dbScheme, String doneUpdateProc, SqlDialect sqlDialect) {
		this.scheduleDbJdni = scheduleDbJdni;
		this.scheduler = scheduler;
		this.dbScheme = dbScheme;
		this.doneUpdateProc = doneUpdateProc;
		this.sqlDialect = sqlDialect;
		
		registerMapper(JobDone.class, JobDoneMapper.class);
		registerMapper(JobDef.class, JobDefMapper.class);
	}

	private final String scheduleDbJdni;
	private final String scheduler;
	private final String dbScheme;
	private final String doneUpdateProc;
	private final SqlDialect sqlDialect;

	public String getDbScheme() {
		if (Tools.isEmpty(dbScheme)) {
			return "";
		} else if (!dbScheme.trim().endsWith(".")) {
			return dbScheme + ".";
		} else {
			return dbScheme;
		}
	}

	public String getScheduler() {
		return scheduler;
	}

	public String getScheduleDbJdni() {
		return scheduleDbJdni;
	}

	public String getDoneUpdateProc() {
		return doneUpdateProc;
	}

	public SqlDialect getSqlDialect() {
		return sqlDialect;
	} 
}
