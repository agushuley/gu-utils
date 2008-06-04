package com.gushuley.utils.orm.sql;

import java.sql.*;

import com.gushuley.utils.orm.*;


public class UnimplementedQueryCallback<T extends ORMObject<?>> 
implements GetQueryCallback<T> 
{
	public String getSql() throws ORMException {
		throw new ORMError("Method not implemented");
	}

	public void setParams(PreparedStatement stm, T obj)
			throws SQLException {
		throw new ORMError("Method not implemented");
	}

	public void executeStep(Connection cnn, T obj) throws SQLException {
		throw new ORMError("Method not implemented");
	}
}
