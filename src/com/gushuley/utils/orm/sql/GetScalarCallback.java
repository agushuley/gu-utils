package com.gushuley.utils.orm.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface GetScalarCallback<T> 
extends ExecCallback {
	T getValue(ResultSet rs) throws SQLException;
	T getNull();
}
