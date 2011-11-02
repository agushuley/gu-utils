package com.gushuley.utils.jmx;


import javax.management.*;

public abstract class SelfManagedBean implements SelfManagedBeanMBean {
	ObjectName name = null;
	public String getName() {
		if (name == null) {
			return null;
		}
		return name.toString();
	}

	public void register(String newName) throws JmxException {
		try {
			ObjectName aName = new ObjectName(newName);
			ServiceLocator.locateServer().registerMBean(this, aName);
			this.name = aName;
		}
		catch (NullPointerException e) {
			throw new JmxException("Object name required");
		} catch (MalformedObjectNameException e) {
			throw new JmxException(e);
		} catch (InstanceAlreadyExistsException e) {
			throw new JmxException("Instance of object exists!", e);
		} catch (MBeanRegistrationException e) {
			throw new JmxException(e);
		} catch (NotCompliantMBeanException e) {
			throw new JmxException("Class " + this.getClass().getName() + " not compliant with management interfaces", e);
		}	
	}

	public void unregister() throws JmxException {
		try {
			ServiceLocator.locateServer().unregisterMBean(name);
		} catch (InstanceNotFoundException e) {
		} catch (MBeanRegistrationException e) {
			throw new JmxException(e);
		}		
	}
}
