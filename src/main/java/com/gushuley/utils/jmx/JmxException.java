package com.gushuley.utils.jmx;

public class JmxException extends Exception {
	private static final long serialVersionUID = 152132264143162535L;

	public JmxException(String message, Throwable cause) {
		super(message, cause);
	}

	public JmxException(String message) {
		super(message);
	}

	public JmxException(Throwable cause) {
		super(cause);
	}
}
