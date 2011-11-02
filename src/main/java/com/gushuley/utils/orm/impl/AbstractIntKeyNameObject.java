package com.gushuley.utils.orm.impl;

public abstract class AbstractIntKeyNameObject extends AbtsractKeyNameObject<Integer> {
	public AbstractIntKeyNameObject(int key) {
		super(key);
	}
	
	public int getId() {
		return getKey();
	}
}
