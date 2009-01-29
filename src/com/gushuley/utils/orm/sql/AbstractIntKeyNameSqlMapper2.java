package com.gushuley.utils.orm.sql;

import java.sql.*;

import com.gushuley.utils.orm.impl.*;


public abstract class AbstractIntKeyNameSqlMapper2<C extends AbstractIntKeyNameObject, X extends GenericContext> 
extends AbstractKeyNameSqlMapper2<C, Integer, X> 
{
	public AbstractIntKeyNameSqlMapper2(String string, String string2, String string3, SqlAttribute... attrs) {
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
