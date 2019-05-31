package com.gushuley.utils.jmx;


public interface SelfManagedBeanMBean {
	String getName();
	
	void register(String name) throws JmxException;
	void unregister() throws JmxException;
}
