package com.gushuley.utils.orm.impl;

import java.util.*;

public class ORMObjectsMapWrapper<T, V> 
implements Map<T, V> {
	public ORMObjectsMapWrapper(GenericORMObject<?> owner, Map<T, V> inner, boolean ro) {
		this.owner = owner;
		this.inner = inner;
		this.ro = ro;
	}

	private Map<T, V> inner;

	public Map<T, V> getInner() {
		return inner;
	}

	private GenericORMObject<?> owner;

	public GenericORMObject<?> getOwner() {
		return owner;
	}

	private boolean ro;

	public boolean isRo() {
		return ro;
	}

	public boolean containsKey(Object key) {
		return inner.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return inner.containsValue(value);
	}

	public Set<java.util.Map.Entry<T, V>> entrySet() {
		return inner.entrySet();
	}

	public V get(Object key) {
		return inner.get(key);
	}

	public Set<T> keySet() {
		return inner.keySet();
	}

	public V put(T key, V value) {
		if (ro) owner.checkRo("collection");
		try {
			return inner.put(key, value);
		} finally {
			owner.markDirty();
		}		
	}

	public void putAll(Map<? extends T, ? extends V> t) {
		if (ro) owner.checkRo("collection");
		try {
			inner.putAll(t);
		} finally {
			owner.markDirty();
		}		
	}

	public Collection<V> values() {
		return inner.values();
	}

	public void clear() {
		if (ro) owner.checkRo("collection");
		try {
			inner.clear();
		} finally {
			owner.markDirty();
		}	
	}

	public boolean isEmpty() {
		return inner.isEmpty();
	}

	public V remove(Object key) {
		if (ro) owner.checkRo("collection");
		try {
			return inner.remove(key);
		} finally {
			owner.markDirty();
		}	
	}

	public int size() {
		return inner.size();
	}
}
