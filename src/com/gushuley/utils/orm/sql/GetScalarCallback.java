package com.gushuley.utils.orm.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface GetScalarCallback<T> 
extends ExecCallback<PreparedStatement> {
	T getValue(ResultSet rs) throws SQLException;
	T getNull();
}
