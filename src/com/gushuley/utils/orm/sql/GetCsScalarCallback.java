package com.gushuley.utils.orm.sql;

import java.sql.*;

public interface GetCsScalarCallback<T> 
extends ExecCallback<CallableStatement>
{
	T getValue(CallableStatement stm) throws SQLException;
}
