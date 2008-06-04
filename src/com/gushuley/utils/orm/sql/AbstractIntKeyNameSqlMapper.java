package com.gushuley.utils.orm.sql;

import java.sql.*;

import com.gushuley.utils.orm.impl.*;


public abstract class AbstractIntKeyNameSqlMapper<C extends AbstractIntKeyNameObject> extends
		AbstractKeyNameSqlMapper<C, Integer> {

	public AbstractIntKeyNameSqlMapper(String string, String string2, String string3, SqlAttribute... attrs) {
		super(string, string2, string3, attrs);
	}
	

	@Override
	protected void setKeyValue(PreparedStatement stm, int n, Integer key) throws SQLException {
		stm.setInt(n, key);
	}

	public Integer createKey(ResultSet rs) throws SQLException {
		return rs.getInt(this.idColumn);
	}
}
