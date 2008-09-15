package com.gushuley.utils.orm;

import java.util.*;

public interface Mapper2<T extends ORMObject<?>, K, C extends ORMContext> {

	public K createKey() throws ORMException;

	public void setContext(C context);

	public C getContext();

	public void clear();

	public Map<K, T> getRegistry();

	public T getById(K id) throws ORMException;

	public void refresh(T object) throws ORMException;
	
	public Collection<T> getAll() throws ORMException;

	public void add(T obj) throws ORMException;

	void commit() throws ORMException;

	public void setClean();
}