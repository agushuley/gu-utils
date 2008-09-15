package com.gushuley.utils.orm.impl;

import java.sql.*;
import java.util.Map;

public class GeneralConnectionWrapper implements Connection {
	public GeneralConnectionWrapper(Connection inner) {
		this.inner = inner;
	}

	private final Connection inner;

	public Connection getInner() {
		return inner;
	}
	
	public void clearWarnings() throws SQLException {
		inner.clearWarnings();
	}

	public void close() throws SQLException {
		inner.close();
	}

	public void commit() throws SQLException {
		inner.commit();
	}

	public Statement createStatement() throws SQLException {
		return inner.createStatement();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return inner.createStatement(resultSetType, resultSetConcurrency);
	}

	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return inner.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public boolean getAutoCommit() throws SQLException {
		return inner.getAutoCommit();
	}

	public String getCatalog() throws SQLException {
		return inner.getCatalog();
	}

	public int getHoldability() throws SQLException {
		return inner.getHoldability();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return inner.getMetaData();
	}

	public int getTransactionIsolation() throws SQLException {
		return inner.getTransactionIsolation();
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return inner.getTypeMap();
	}

	public SQLWarning getWarnings() throws SQLException {
		return inner.getWarnings();
	}

	public boolean isClosed() throws SQLException {
		return inner.isClosed();
	}

	public boolean isReadOnly() throws SQLException {
		return inner.isReadOnly();
	}

	public String nativeSQL(String sql) throws SQLException {
		return inner.nativeSQL(sql);
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		return inner.prepareCall(sql);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return inner.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return inner.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return inner.prepareStatement(sql);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		return inner.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		return inner.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		return inner.prepareStatement(sql, columnNames);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return inner.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return inner.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		inner.releaseSavepoint(savepoint);
	}

	public void rollback() throws SQLException {
		inner.rollback();
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		inner.rollback(savepoint);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		inner.setAutoCommit(autoCommit);
	}

	public void setCatalog(String catalog) throws SQLException {
		inner.setCatalog(catalog);
	}

	public void setHoldability(int holdability) throws SQLException {
		inner.setHoldability(holdability);
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		inner.setReadOnly(readOnly);
	}

	public Savepoint setSavepoint() throws SQLException {
		return inner.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return inner.setSavepoint(name);
	}

	public void setTransactionIsolation(int level) throws SQLException {
		inner.setTransactionIsolation(level);
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		inner.setTypeMap(map);
	}
/*
	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return inner.createArrayOf(typeName, elements);
	}

	@Override
	public Blob createBlob() throws SQLException {
		return inner.createBlob();
	}

	@Override
	public Clob createClob() throws SQLException {
		return inner.createClob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return inner.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return inner.createSQLXML();
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return inner.createStruct(typeName, attributes);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return inner.getClientInfo();
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		return inner.getClientInfo(name);
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		return inner.isValid(timeout);
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		inner.setClientInfo(properties);
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		inner.setClientInfo(name, value);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return inner.isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return inner.unwrap(iface);
	} */
}