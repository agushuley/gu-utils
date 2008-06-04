package com.gushuley.utils.orm.impl;

import com.gushuley.utils.orm.ORMError;
import com.gushuley.utils.orm.ORMObject;

public class GenericORMObject<K> implements ORMObject<K> {
	private State state = State.NEW; 

	private K key;

	public GenericORMObject(K key) {
		this.key = key;
	}

	/* (non-Javadoc)
	 * @see net.kyivstar.commons.orm.impl.ORMObject#getKey()
	 */
	public K getKey() {
		return key;
	}
	
	public void setKey(K key) {
		if (getORMState() != ORMObject.State.NEW) {
			throw new ORMError(getClass() + " Key mutator is avaliable only for NEW state. Current state " + getORMState());
		}
		this.key = key;
	}
	
	protected void markDirty() {
		if (state == State.CLEAN) {
			state = State.DIRTY;
		}
	}

	/* (non-Javadoc)
	 * @see net.kyivstar.commons.orm.impl.ORMObject#getState()
	 */
	public State getORMState() {
		return state;
	}

	/* (non-Javadoc)
	 * @see net.kyivstar.commons.orm.impl.ORMObject#setState(net.kyivstar.commons.orm.impl.AbstractORMObject.State)
	 */
	public void setORMState(State state) {
		this.state = state;
	}
	
	protected void checkRo(String field) {
		if (state != State.LOADING) {
			throw new IllegalAccessError("Field " + field + " of class " + getClass().getName() + " is read only!");
		}
	}

	public void remove() {
		state = State.DELETED;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": " + getKey().toString() + "]";
	}
}
