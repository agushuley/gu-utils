package com.gushuley.utils.orm.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.gushuley.utils.orm.ORMException;

public interface ExecCallback<S extends PreparedStatement> {
	String getSql() throws ORMException;
	void setParams(S stm) throws SQLException, ORMException;
}
