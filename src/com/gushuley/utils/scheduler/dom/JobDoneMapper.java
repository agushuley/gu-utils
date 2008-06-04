package com.gushuley.utils.scheduler.dom;

import java.sql.*;

import com.gushuley.utils.Tools;
import com.gushuley.utils.orm.ORMError;
import com.gushuley.utils.orm.ORMException;
import com.gushuley.utils.orm.sql.*;


public class JobDoneMapper extends AbstractSqlMapper<JobDone, JobDoneKey>  implements JobDone.Mapper {
	private final String BASE_SELECT = "SELECT scd_sch_id, scd_date FROM schedules_done";

	@Override
	protected JobDone createInstance(JobDoneKey key, ResultSet rs) throws SQLException {
		return new JobDone(key);
	}

	@Override
	public JobDoneKey createKey(ResultSet rs) throws SQLException {
		return new JobDoneKey(rs.getString("scd_sch_id"), rs.getTimestamp("scd_date"));
	}

	@Override
	protected String getConnectionKey() {
		return ((SchedulerContext)getContext()).getScheduleDbJdni();
	}

	@Override
	protected GetQueryCallback<JobDone> getDeleteQueryCB() {
		return new UnimplementedQueryCallback<JobDone>();
	}

	@Override
	protected GetQueryCallback<JobDone> getInsertQueryCB() {
		return new GetQueryCallback<JobDone>() {
			public void executeStep(Connection cnn, JobDone obj) throws SQLException {
			}

			public String getSql() throws ORMException {
				return "INSERT INTO schedules_done (scd_sch_id, scd_date) VALUES (?, ?)";
			}

			public void setParams(PreparedStatement stm, JobDone obj) throws SQLException, ORMException {
				stm.setString(1, obj.getKey().getJobId());
				Tools.setTimestamp(stm, 2, obj.getKey().getDate());
			}			
		};
	}

	@Override
	protected String getSelectAllSql() {
		throw new ORMError("Unimplemented method");
	}

	@Override
	protected void setSelectAllStatementParams(PreparedStatement stm)
	throws ORMException {
		throw new ORMError("Unimplemented method");
	}
	
	@Override
	protected String getSelectSql() {
		return BASE_SELECT + " WHERE scd_sch_id = ? AND scd_date = ?";
	}

	@Override
	protected void setSelectStatementParams(PreparedStatement stm, JobDoneKey id)
			throws SQLException {
		stm.setString(1, id.getJobId());
		Tools.setTimestamp(stm, 2, id.getDate());
	}
	
	@Override
	protected GetQueryCallback<JobDone> getUpdateQueryCB() {
		return new UnimplementedQueryCallback<JobDone>();
	}

	@Override
	protected void loadInstance(JobDone obj, ResultSet rs) throws SQLException,
			ORMException {
	}

	public JobDoneKey createKey() throws ORMException {
		throw new ORMError("Unimplemented method");
	}

	public JobDone getLastForJob(final String jobId) throws ORMException {
		for (JobDone j : getCollectionForCb(new GetQueryCallback<JobDone>() {
			public void executeStep(Connection cnn, JobDone obj) throws SQLException {
			}

			public String getSql() throws ORMException {
				return "SELECT scd_sch_id, MAX(scd_date) AS scd_date FROM schedules_done t WHERE t.scd_sch_id = ? GROUP BY scd_sch_id";
			}

			public void setParams(PreparedStatement stm, JobDone obj) throws SQLException, ORMException {
				stm.setString(1, jobId);
			}			
		})) {
			return j;
		}
		return null;
	}

}
