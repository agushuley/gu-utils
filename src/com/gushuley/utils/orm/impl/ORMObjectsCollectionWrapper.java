package com.gushuley.utils.orm.impl;

import java.util.*;

public class ORMObjectsCollectionWrapper<T> implements Collection<T> {
	public ORMObjectsCollectionWrapper(GenericORMObject<?> owner, Collection<T> inner, boolean ro) {
		this.owner = owner;
		this.inner = inner;
		this.ro = ro;
	}

	public boolean add(T o) {		
		if (ro) owner.checkRo("collection");
		try {
			return inner.add(o);
		} 
		finally {
			owner.markDirty();
		}		
	}

	public boolean addAll(Collection<? extends T> c) {
		if (ro) owner.checkRo("collection");
		try {
			return inner.addAll(c);
		} 
		finally {
			owner.markDirty();
		}		
	}

	public void clear() {
		if (ro) owner.checkRo("collection");
		inner.clear();
		owner.markDirty();
	}

	public boolean contains(Object o) {
		return inner.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return c.containsAll(c);
	}

	public boolean isEmpty() {
		return inner.isEmpty();
	}

	public Iterator<T> iterator() {
		return inner.iterator();
	}

	public boolean remove(Object o) {
		if (ro) owner.checkRo("collection");
		try {
			return inner.remove(o);
		} 
		finally {
			owner.markDirty();
		}		
	}

	public boolean removeAll(Collection<?> c) {
		if (ro) owner.checkRo("collection");
		try {
			return inner.removeAll(c);
		} 
		finally {
			owner.markDirty();
		}		
	}

	public boolean retainAll(Collection<?> c) {
		if (ro) owner.checkRo("collection");
		try {
			return inner.retainAll(c);
		} 
		finally {
			owner.markDirty();
		}		
	}

	public int size() {
		return inner.size();
	}

	public Object[] toArray() {
		return inner.toArray();
	}

	public <T1> T1[] toArray(T1[] a) {
		return inner.toArray(a);
	}

	private Collection<T> inner;

	public Collection<T> getInner() {
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
}
