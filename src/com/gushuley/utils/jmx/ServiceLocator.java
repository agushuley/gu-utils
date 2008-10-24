/**
 * 
 */
package com.gushuley.utils.jmx;

import java.sql.*;
import java.util.*;

import javax.management.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * @author Andriy
 * @docRoot ����������� ����� - ��������� ������ � ���"�� JBoss
 */
public final class ServiceLocator {
	static final Logger log = Logger.getLogger(ServiceLocator.class);

	public static <C> C getObjectInterface(Class<C> _class, String name)
			throws JmxException {
		try {
			return getObjectInterface(_class, new ObjectName(name));
		} catch (MalformedObjectNameException e) {
			throw new JmxException(e);
		}
	}

	public static <C> C getObjectInterface(Class<C> _class, ObjectName name) {
		// Checking is instance MBean existing on server
		final Set<ObjectInstance> list = locateServer().queryMBeans(name, null);
		if (list.size() == 0) {
			return null;
		}
		return (C) MBeanServerInvocationHandler
			.newProxyInstance(locateServer(), name, _class, false);
	}

	public static MBeanServer locateServer() {
		return (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);
	}

	@SuppressWarnings("unchecked")
	public static Collection<ObjectInstance> listNames(String baseName)
			throws JmxException {
		try {
			List<ObjectInstance> list = new ArrayList();
			list.addAll(locateServer().queryMBeans(
					new ObjectName(baseName + ",*"), null));
			return list;
		} catch (MalformedObjectNameException e) {
			throw new JmxException(e);
		} catch (NullPointerException e) {
			throw new JmxException("Name not be null", e);
		}
	}

	public static Connection getConnection(String string) throws SQLException {
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			Connection cnn = ((DataSource)ctx.lookup(string)).getConnection();
			cnn.setAutoCommit(false);
			return cnn;
		} catch (NamingException e) {
			throw new SQLException("Jndi name locating error");
		}
	}
}
