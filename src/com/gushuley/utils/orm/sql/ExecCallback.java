package com.gushuley.utils.orm.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.gushuley.utils.orm.ORMException;

public interface ExecCallback {
	String getSql() throws ORMException;
	void setParams(PreparedStatement stm) throws SQLException, ORMException;
}
