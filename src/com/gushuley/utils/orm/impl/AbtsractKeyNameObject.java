package com.gushuley.utils.orm.impl;

import java.util.*;

public class AbtsractKeyNameObject<C> extends GenericORMObject<C> {

	private String name = "";
	private Map<String, String> attributes = new HashMap<String, String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty();
	}

	public AbtsractKeyNameObject(C key) {
		super(key);
	}

	public String getAttribute(String key) {
		return attributes.get(key);
	}
	
	public void setAttribute(String key, String value) {
		attributes.put(key, value);
		markDirty();
	}
	
	public Map<String, String> getAttributes() {
		Map<String, String> items = new HashMap<String, String>();
		items.putAll(attributes);
		return items;
	}
}