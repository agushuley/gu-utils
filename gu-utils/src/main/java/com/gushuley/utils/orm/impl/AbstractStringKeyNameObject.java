package com.gushuley.utils.orm.impl;

public abstract class AbstractStringKeyNameObject extends AbtsractKeyNameObject<String> {
	public AbstractStringKeyNameObject(String key) {
		super(key);
	}
	
	public String getId() {
		return getKey();
	}	
}
