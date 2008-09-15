package com.gushuley.utils.orm.impl;

import java.util.*;

import com.gushuley.utils.orm.*;


public abstract class AbstractMapper2<T extends ORMObject<K>, K, C extends ORMContext>
		implements Mapper2<T, K, C> {

	private final Map<K, T> registry;

	protected C ctx = null;

	public AbstractMapper2(boolean ordered) {
		super();
		if (ordered) {
			registry = new TreeMap<K, T>();
		}
		else {
			registry = new HashMap<K, T>();			
		}
	}

	public void setContext(C context) {
		ctx = context;
	}

	public C getContext() {
		return ctx;
	}

	public void clear() {
		registry.clear();
	}

	public Map<K, T> getRegistry() {
		return registry;
	}

	public void add(T obj) throws ORMException {
		synchronized (registry) {
			if (registry.containsKey(obj.getKey())) {
				throw new ORMException("Duplicate object key");
			}
			registry.put(obj.getKey(), obj);
			obj.setORMState(ORMObject.State.NEW);
		}
	}

}