package com.gushuley.utils.orm.impl;

import java.util.*;

public class ORMObjectsListWrapper<T> extends
		ORMObjectsCollectionWrapper<T> implements List<T> {
	public ORMObjectsListWrapper(GenericORMObject<?> owner, Collection<T> inner, boolean ro) {
		super(owner, inner, ro);
	}

	@SuppressWarnings("unchecked")
	protected List<T> getList() {
		return (List<T>) getInner();
	}

	public void add(int index, T element) {
		if (isRo())
			getOwner().checkRo("collection");
		getList().add(index, element);
		getOwner().markDirty();
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		if (isRo())
			getOwner().checkRo("collection");
		try {
			return getList().addAll(index, c);
		} finally {
			getOwner().markDirty();
		}
	}

	public T get(int index) {
		return getList().get(index);
	}

	public int indexOf(Object o) {
		return getList().indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return getList().lastIndexOf(o);
	}

	public ListIterator<T> listIterator() {
		return getList().listIterator();
	}

	public ListIterator<T> listIterator(int index) {
		return getList().listIterator(index);
	}

	public T remove(int index) {
		if (isRo())
			getOwner().checkRo("collection");
		try {
			return getList().remove(index);
		} finally {
			getOwner().markDirty();
		}
	}

	public T set(int index, T element) {
		if (isRo())
			getOwner().checkRo("collection");
		try {
			return getList().set(index, element);
		} finally {
			getOwner().markDirty();
		}
	}

	public List<T> subList(int fromIndex, int toIndex) {
		return getList().subList(fromIndex, toIndex);
	}
	
	public void sort(Comparator<T> comparator) {
		Collections.sort(getList(), comparator);
	}
}
