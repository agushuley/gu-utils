package com.gushuley.utils.orm;

public class ORMException extends Exception {
	private static final long serialVersionUID = -6537456367472220414L;

	public ORMException() {
	}

	public ORMException(String message) {
		super(message);
	}

	public ORMException(Throwable cause) {
		super(cause);
	}

	public ORMException(String message, Throwable cause) {
		super(message, cause);
	}
}
