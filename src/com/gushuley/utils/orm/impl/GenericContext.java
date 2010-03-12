package com.gushuley.utils.orm.impl;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.gushuley.utils.orm.*;


public class GenericContext 
implements ORMContext {
	private final Map<String, InstancesCountConnectionWrapper> cnns = new HashMap<String, InstancesCountConnectionWrapper>();
	public final Map<Class<? extends ORMObject<?>>, Class<? extends Mapper2<?,?,?>>> 
		mappersForClasses = new HashMap<Class<? extends ORMObject<?>>, Class<? extends Mapper2<?,?,?>>>();
	private final Logger log = Logger.getLogger(getClass());
	private final Map<String, String> properties = new HashMap<String, String>();
	private boolean closed = false;

	public GenericContext(String... props) {
		for (int i = 0; i < props.length / 2; i++) {
			properties.put(props[i * 2], props[i * 2 + 1]);
		}
	}
	
	public void registerMapper(Class<? extends ORMObject<?>> aClass, Class<? extends Mapper2<?,?,?>> mapper) {
		mappersForClasses.put(aClass, mapper);
	}
	
	public Connection getConnection(String key, boolean isMutable) throws ORMException {
		synchronized (cnns) {
			try {
				InstancesCountConnectionWrapper cnn = cnns.get(key);
				if (cnn == null) {
					final Connection i;
					if (key.startsWith("java:")) {
						i = ((DataSource) new InitialContext().lookup(key)).getConnection();				
					} else {
						i = DriverManager.getConnection(key);
					}
					i.setAutoCommit(false);
					try {
						validateConnection(key, i);
					} catch (ORMException e) {
						i.close();
						throw e;
					}
					cnn = new InstancesCountConnectionWrapper(i);
					cnns.put(key, cnn);
				}
				if (isMutable) {
					cnn.setMutable(true);
				}
				cnn.lock();
				return cnn;
			} catch (NamingException e) {
				throw new ORMException(e);
			} catch (SQLException e) {				
				throw parseException(e);
			}
		}
	}
	
	public void releaseConnection(Connection cnn) throws ORMException {
		synchronized (cnns) {
			if (!cnns.containsValue(cnn)) {
				throw new ORMException("Connection is not owned by context");
			}
			for (Map.Entry<String, InstancesCountConnectionWrapper> e : cnns.entrySet()) {
				if (e.getValue() == cnn) {
					int r = e.getValue().release();
					if (closed) {
						if (r == 0 && !e.getValue().isMutable()) {
							cnns.entrySet().remove(e);
							try { e.getValue().close(); } catch (SQLException e1) { }							
						}
					}
				}
			}
		}
	}

	private final Map<Class<? extends Mapper2<?,?,?>>, Mapper2<?,?,?>> mappers = new HashMap<Class<? extends Mapper2<?,?,?>>, Mapper2<?,?,?>>();

	@SuppressWarnings("unchecked")
	public
	synchronized <T extends Mapper2<?,?,?>> T getMapperInstance(Class<? extends T> mapperClass) throws ORMException 
	{
		if (mappers.containsKey(mapperClass)) {
			return (T) mappers.get(mapperClass);
		}
		try {
			T mapper = mapperClass.newInstance();
			((Mapper2<?, ?, ORMContext>)mapper).setContext(this);
			mappers.put(mapperClass, mapper);
			return mapper;
		} catch (InstantiationException e) {
			throw new ORMException(e);
		} catch (IllegalAccessException e) {
			throw new ORMException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized <C extends ORMObject<?>, K, X extends ORMContext> Mapper2<C, K, X> getMapper2(Class<C> forClass) throws ORMException {
		Class<?> objectClass = forClass;
		
		while (ORMObject.class.isAssignableFrom(objectClass)) {
			if (mappersForClasses.containsKey(objectClass)) {
				try {
					return (Mapper2<C,K, X>) getMapperInstance(mappersForClasses.get(objectClass));
				} catch (ClassCastException ex) {
					throw new ORMException("Class " + forClass + " not implements Mapper2 interface");
				}
			}
			objectClass = objectClass.getSuperclass();
		}
		
		throw new ORMException("Cannot find mapper for class: " + forClass.getName());		
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <C extends ORMObject<?>, K>Mapper<C, K> getMapper(Class<C> forClass) throws ORMException {
		try {
			return (Mapper<C, K>) getMapper2(forClass);
		} catch (ClassCastException ex) {
			throw new ORMException("Class " + forClass + " not implements Mapper interface");
		}
	}

	public synchronized void update() throws ORMException {
		for (final Mapper2<?,?,?> mapper : mappers.values()) {
			mapper.commit();
		}
		for (final Mapper2<?,?,?> mapper : mappers.values()) {
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
					throw parseException(e);
				}
			}
		}
	}

	public synchronized void close() {
		closed = true;
		for (final Mapper2<?,?,?> mapper : mappers.values()) {
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

	public <C extends ORMObject<?>, K> C find(Class<C> objectClass, K key) throws ORMException {
		return getMapper2(objectClass).getById(key);
	}

	@SuppressWarnings("unchecked")
	public void add(ORMObject<?> o) throws ORMException {
		getMapper2((Class<ORMObject>)o.getClass()).add(o);
	}

	public <C extends ORMObject<?>, K> Collection<C> find(Class<C> objectClass, K... key) throws ORMException {
		final Collection<C> c = new ArrayList<C>();
		for (K k : key) {
			C o = find(objectClass, k);
			if (o != null) {
				c.add(o);
			}
		}
		
		return c;
	}

	public <C extends ORMObject<?>, K> Collection<C> findAll(Class<C> objectClass) throws ORMException {
		return getMapper2(objectClass).getAll();
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

	@SuppressWarnings("unchecked")
	public <C extends ORMObject<?>, I> I getMapper(Class<C> objectClass, Class<I> infClass) throws ORMException {
		Object i = getMapper2(objectClass);
		try {
			return (I) i;
		} catch (ClassCastException e) {
			throw new ORMException("Mapper " + i.getClass() + " does not implements interface " + infClass);
		}
	}

	public Map<String, String> getProperties() {
		return properties ;
	}
	
	protected void validateConnection(String key, Connection cnn) throws ORMException {
	}
	

	public ORMException parseException(SQLException ex) {
		return new ORMException(ex);
	}
	
	@Override
	public void releaseUnusedConnections() {
		synchronized (cnns) {
			for (Map.Entry<String, InstancesCountConnectionWrapper> e : cnns.entrySet()) {
				if (!e.getValue().isMutable()) {
					cnns.entrySet().remove(e);
					try { e.getValue().close(); } catch (SQLException e1) { }
				}
			}
		}
	}
}
