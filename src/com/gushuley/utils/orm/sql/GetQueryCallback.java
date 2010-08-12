package com.gushuley.utils.orm.sql;

import java.sql.*;

import com.gushuley.utils.orm.*;


public interface GetQueryCallback<T extends ORMObject<?>> {
	String getSql() throws ORMException;
	void setParams(PreparedStatement stm, T obj) throws SQLException, ORMException;
	void executeStep(Connection cnn, T obj) throws SQLException, ORMException;
}
