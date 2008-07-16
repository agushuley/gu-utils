package com.gushuley.utils.orm;

import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

public interface ORMContext {

	public abstract Connection getConnection(String key, boolean isMutable) throws ORMException;

	public abstract void releaseConnection(Connection cnn) throws ORMException;

	public <C extends ORMObject<?>, K> 
		Mapper<C, K> getMapper(Class<C> objectClass) throws ORMException;

	public <C extends ORMObject<?>, I> 
		I getMapper(Class<C> objectClass, Class<I> infClass) throws ORMException;

	public abstract void update() throws ORMException;

	public abstract void commit() throws ORMException;

	public abstract void close();
	
	public <C extends ORMObject<?>, K> C find(Class<C> objectClass, K key) throws ORMException;
	
	public <C extends ORMObject<?>, K> Collection<C> find(Class<C> objectClass, K... key) throws ORMException;

	public void add(ORMObject<?> o) throws ORMException;
	
	public <C extends ORMObject<?>, K> Collection<C> findAll(Class<C> objectClass) throws ORMException;
	
	public Map<String, String> getProperties();
}