package com.gushuley.utils.orm.sql;

import java.sql.*;

import com.gushuley.utils.orm.ORMObject;


public final class OrmSqlTools {
	public static void setParam(PreparedStatement stm, int n,
			ORMObject<Integer> obj) throws SQLException {
		if (obj != null) {
			stm.setInt(n, obj.getKey());		
		}
		else {
			stm.setNull(n, Types.VARCHAR);
		}
	}

	public static void setParamStr(PreparedStatement stm, int n,
			ORMObject<String> obj) throws SQLException {
		if (obj != null) {
			stm.setString(n, obj.getKey());		
		}
		else {
			stm.setNull(n, Types.INTEGER);
		}
	}
}
