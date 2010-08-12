package com.gushuley.utils.orm;

public interface ORMObject<K> {
	public enum State { NEW, DIRTY, CLEAN, LOADING, DELETED }

	public abstract K getKey();

	public abstract State getORMState();

	public abstract void setORMState(State state);

	public void remove();

	public void setKey(K key);
}
