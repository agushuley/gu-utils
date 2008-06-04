package com.gushuley.utils.orm.impl;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.gushuley.utils.orm.*;


public class GenericContext implements ORMContext {
	private Map<String, InstancesCountConnectionWrapper> cnns = new HashMap<String, InstancesCountConnectionWrapper>();
	private Map<Class<? extends ORMObject<?>>, Class<? extends Mapper<?,?>>> 
		mappersForClasses = new HashMap<Class<? extends ORMObject<?>>, Class<? extends Mapper<?,?>>>();

	public void registerMapper(Class<? extends ORMObject<?>> aClass, Class<? extends Mapper<?,?>> mapper) {
		mappersForClasses.put(aClass, mapper);
	}
	
	public Connection getConnection(String key, boolean isMutable) throws ORMException {
		synchronized (cnns) {
			try {
				InstancesCountConnectionWrapper cnn = cnns.get(key);
				if (cnn == null) {
					Connection i;
					if (key.startsWith("java:")) {
						i = ((DataSource) new InitialContext().lookup(key)).getConnection();				
					} else {
						i = DriverManager.getConnection(key);
					}					
					i.setAutoCommit(false);
					cnn = new InstancesCountConnectionWrapper(i);
					cnns.put(key, cnn);
				}
				if (isMutable) {
					cnn.setMutable(isMutable);
				}
				cnn.lock();
				return cnn;
			} catch (NamingException e) {
				throw new ORMException(e);
			} catch (SQLException e) {
				throw new ORMException(e);
			}
		}
	}
	
	public void releaseConnection(Connection cnn) throws ORMException {
		synchronized (cnns) {
			if (!cnns.containsValue(cnn)) {
				throw new ORMException("Connection is not owned by context");
			}
			Set<String> toRelease = new HashSet<String>();
			for (Map.Entry<String, InstancesCountConnectionWrapper> e : cnns.entrySet()) {
				if (e.getValue() == cnn) {
					int r = e.getValue().release();
					if (r == 0 && !e.getValue().isMutable()) {
						toRelease.add(e.getKey());
					}
				}
			}
			for (String key : toRelease) {
				InstancesCountConnectionWrapper w = cnns.get(key);
				if (w != null) {
					cnns.remove(key);
					try {
						w.rollback();
						w.close();
					} catch (SQLException e) {
						throw new ORMException(e);
					}
				}
			}		
		}
	}

	private Map<Class<? extends Mapper<?,?>>, Mapper<?,?>> mappers = new HashMap<Class<? extends Mapper<?,?>>, Mapper<?,?>>();

	@SuppressWarnings("unchecked")
	private synchronized <T extends Mapper<?,?>> T getMapperInstance(
			Class<? extends T> mapperClass) throws ORMException {
		if (mappers.containsKey(mapperClass)) {
			return (T) mappers.get(mapperClass);
		}
		try {
			T mapper = mapperClass.newInstance();
			mapper.setContext(this);
			mappers.put(mapperClass, mapper);
			return mapper;
		} catch (InstantiationException e) {
			throw new ORMException(e);
		} catch (IllegalAccessException e) {
			throw new ORMException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized <C extends ORMObject<?>, K> 
		Mapper<C, K> getMapper(Class<C> forClass) throws ORMException {
		Class<?> objectClass = forClass;
		
		while (ORMObject.class.isAssignableFrom(objectClass)) {
			if (mappersForClasses.containsKey(objectClass)) {
				Mapper<C, K> mapperInstance = (Mapper<C,K>) getMapperInstance(mappersForClasses.get(objectClass));
				return mapperInstance;
			}
			objectClass = (Class<C>) objectClass.getSuperclass();
		}
		
		throw new ORMException("Cannot find mapper for class: " + forClass.getName());		
	}

	public synchronized void update() throws ORMException {
		for (Mapper<?,?> mapper : mappers.values()) {
			mapper.commit();
		}
		for (Mapper<?,?> mapper : mappers.values()) {
			mapper.setClean();
		}
	}

	public synchronized void commit() throws ORMException {		
		update();
		synchronized (cnns) {
			for (Connection cnn : cnns.values()) {
				try {
					cnn.commit();
				} catch (SQLException e) {
					throw new ORMException(e);
				}
			}
		}
	}

	public synchronized void close() {
		for (Mapper<?,?> mapper : mappers.values()) {
			mapper.clear();
		}		
		mappers.clear();
		synchronized (cnns) {
			for (Connection cnn : cnns.values()) {
				try {
					cnn.rollback();
					cnn.close();
				} catch (SQLException e) {
					log.error("", e);
				}
			}
			cnns.clear();
		}
	}

	private Logger log = Logger.getLogger(getClass());

	public <C extends ORMObject<?>, K> C find(Class<C> objectClass, K key) throws ORMException {
		return getMapper(objectClass).getById(key);
	}

	@SuppressWarnings("unchecked")
	public void add(ORMObject<?> o) throws ORMException {
		getMapper((Class<ORMObject>)o.getClass()).add(o);
	}

	public <C extends ORMObject<?>, K> Collection<C> find(Class<C> objectClass, K... key) throws ORMException {
		Collection<C> c = new ArrayList<C>();
		for (K k : key) {
			C o = find(objectClass, k);
			if (o != null) {
				c.add(o);
			}
		}
		
		return c;
	}

	public <C extends ORMObject<?>, K> Collection<C> findAll(Class<C> objectClass) throws ORMException {
		return getMapper(objectClass).getAll();
	}
	
	private class InstancesCountConnectionWrapper extends GeneralConnectionWrapper {	
		public InstancesCountConnectionWrapper(Connection inner) {
			super(inner);
		}

		private boolean mutable = false;
		
		public boolean isMutable() {
			return mutable;
		}

		public void setMutable(boolean mutable) {
			this.mutable = mutable;
		}
		
		private int count = 0;

		public synchronized int lock() {
			count = count + 1;
			return count;
		}
		
		public synchronized int release() {
			if (count > 0) {
				count = count - 1;
			}
			return count;
		}
	}
}
