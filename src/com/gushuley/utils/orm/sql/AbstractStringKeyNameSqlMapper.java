package com.gushuley.utils.orm.sql;

import java.sql.*;

import com.gushuley.utils.orm.ORMException;
import com.gushuley.utils.orm.impl.*;


public abstract class AbstractStringKeyNameSqlMapper<C extends AbstractStringKeyNameObject>
		extends AbstractKeyNameSqlMapper<C, String> {

	public AbstractStringKeyNameSqlMapper(String string, String string2, String string3, SqlAttribute... attrs) {
		super(string, string2, string3, attrs);
	}
	
	@Override
	protected void setKeyValue(PreparedStatement stm, int n, String key)
			throws SQLException {
		stm.setString(n, key);
	}
	
	public String createKey(ResultSet rs) throws SQLException {
		return rs.getString(this.idColumn);
	}
	
	@Override
	protected String getSelectSql() {
		StringBuilder attrs = new StringBuilder();
		if (attributes != null) {
			for (SqlAttribute a : attributes) {
				attrs.append(", " + a.getColumn());
			}
		}
		return "SELECT " + idColumn + " ," + nameColumn + attrs + " FROM " + getTableName()
				+ " WHERE " + idColumn + " = ?";
	}
	
	@Override
	protected GetQueryCallback<C> getDeleteQueryCB() {
		return new GetQueryCallback<C>() {
			public String getSql() throws ORMException {
				return "DELETE FROM " + getTableName() + 
					" WHERE" + idColumn + " = ?";
			}
	
			public void setParams(PreparedStatement stm, C obj)
					throws SQLException {
				setKeyValue(stm, 1, obj.getKey());
			}

			public void executeStep(Connection cnn, C obj) throws SQLException {
			}
		};
	}


	@Override
	protected GetQueryCallback<C> getUpdateQueryCB() {
		return new GetQueryCallback<C>() {
			public String getSql() throws ORMException {
				StringBuilder attrs = new StringBuilder();
				if (attributes != null) {
					for (SqlAttribute a : attributes) {
						attrs.append(", " + a.getColumn() + " = ?");
					}
				}
				return "UPDATE " + getTableName() + " SET " + nameColumn + " = ? "
						+ attrs + " WHERE " + idColumn + " = ?";
			}
	
			public void setParams(PreparedStatement stm, C obj)
					throws SQLException {
				stm.setString(1, obj.getName());
				int i = 0;
				if (attributes != null) {
					for (SqlAttribute a : attributes) {
						stm.setString(2 + i, obj.getAttribute(a.getCaption()));
						i++;
					}
				}
				setKeyValue(stm, 3 + i, obj.getKey());
			}

			public void executeStep(Connection cnn, C obj) throws SQLException {
			}
		};
	}
}
