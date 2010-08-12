package com.gushuley.utils.orm.sql;

import java.sql.*;

import com.gushuley.utils.Tools;
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

	public static String creteCallableTxt(SqlDialect sqlDialect, String callable,
			String args, boolean isFunc) {
		if (sqlDialect == SqlDialect.ORACLE) {
			if (isFunc) {
				callable = "? := " + callable;
			}
			String call = "begin " + callable;
			if (!Tools.isEmpty(args)) {
				call += "(" + args + ")";
			}
			call += "; end;";
			return  call;
		} else if (sqlDialect == SqlDialect.POSTGRES) {
			callable = "call " + callable;
			if (isFunc) {
				callable = "? = " + callable;
			}
			if (!Tools.isEmpty(args)) {
				callable += "(" + args + ")";
			}
			return "{ " + callable + " }";
		} else {
			return "Undefined SQL dialect " + sqlDialect;
		}
	}

	public static String creteCallTxt(SqlDialect sqlDialect, String callable, String args) {
		if (sqlDialect == SqlDialect.ORACLE) {
			String call = "begin " + callable;
			if (!Tools.isEmpty(args)) {
				call += "(" + args + ")";
			}
			call += "; end;";
			return  call;
		} else if (sqlDialect == SqlDialect.POSTGRES) {
			callable = "UPDATE e SET e = 0 WHERE " + callable;
			if (!Tools.isEmpty(args)) {
				callable += "(" + args + ")";
			}
			callable += " IS NULL";
			return callable;
		} else {
			return "Undefined SQL dialect " + sqlDialect;
		}
	}
}
