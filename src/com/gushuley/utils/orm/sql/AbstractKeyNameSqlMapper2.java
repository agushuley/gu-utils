package com.gushuley.utils.orm.sql;

import java.sql.*;

import com.gushuley.utils.orm.ORMContext;
import com.gushuley.utils.orm.ORMException;
import com.gushuley.utils.orm.impl.*;


public abstract class AbstractKeyNameSqlMapper2<C extends AbtsractKeyNameObject<K>, K, X extends ORMContext>
extends AbstractSqlMapper2<C, K, X> 
{
	public AbstractKeyNameSqlMapper2(String table, String idColumn,
			String nameColumn, SqlAttribute... attributes) {
		super(true);
		this.aTableName = table;
		this.idColumn = idColumn;
		this.nameColumn = nameColumn;
		this.attributes = attributes;
	}

	private final String aTableName;
	protected final String idColumn;
	protected final String nameColumn;
	protected final SqlAttribute[] attributes;

	protected abstract void setKeyValue(PreparedStatement stm, int n, K key) throws SQLException;

	@Override
	protected GetQueryCallback<C> getDeleteQueryCB() {
		return new GetQueryCallback<C>() {
			public String getSql() throws ORMException {
				return "DELETE FROM " + getTableName() + " WHERE " + idColumn
						+ " = ?";
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
	protected GetQueryCallback<C> getInsertQueryCB() {
		return new GetQueryCallback<C>() {
			public String getSql() throws ORMException {
				StringBuilder attrs = new StringBuilder(), attrs2 = new StringBuilder();
				if (attributes != null) {
					for (SqlAttribute a : attributes) {
						attrs.append(", " + a.getColumn());
						attrs2.append(", ?");
					}
				}
				return "INSERT INTO " + getTableName() + " (" + idColumn + " ,"
						+ nameColumn + attrs + ") " + 
						" VALUES (?, ?" + attrs2 + ")";
			}
	
			public void setParams(PreparedStatement stm, C obj)
					throws SQLException {
				setKeyValue(stm, 1, obj.getKey());
				stm.setString(2, obj.getName());
				if (attributes != null) {
					int i = 0;
					for (SqlAttribute a : attributes) {
						stm.setString(3 + i, obj.getAttribute(a.getCaption()));
						i++;
					}
				}
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
				setKeyValue(stm, 2 + i, obj.getKey());
			}

			public void executeStep(Connection cnn, C obj) throws SQLException {
			}
		};
	}
	
	@Override
	protected String getSelectAllSql() {
		StringBuilder attrs = new StringBuilder();
		if (attributes != null) {
			for (SqlAttribute a : attributes) {
				attrs.append(", " + a.getColumn());
			}
		}
		return "SELECT " + idColumn + " ," + nameColumn + attrs + " FROM " + getTableName() + " ORDER BY " + idColumn;
	}

	@Override
	protected void setSelectAllStatementParams(PreparedStatement stm) throws ORMException {
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
	protected void setSelectStatementParams(PreparedStatement stm, K id) throws SQLException {
		setKeyValue(stm, 1, id);
	}

	@Override
	protected void loadInstance(C obj, ResultSet rs) throws SQLException {
		obj.setName(rs.getString(nameColumn));
		if (attributes != null) {
			int i = 0;
			for (SqlAttribute attr : attributes) {
				obj.setAttribute(attr.getCaption(), rs.getString(i + 3));
				i++;
			}
		}
	}

	public K createKey() throws ORMException {
		return null;
	}

	protected String getTableName() {
		return aTableName;
	}

}