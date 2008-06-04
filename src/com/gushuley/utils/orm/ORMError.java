package com.gushuley.utils.orm;

public class ORMError extends Error {	
	private static final long serialVersionUID = 1000893451334481226L;

	public ORMError() {
	}

	public ORMError(String message) {
		super(message);
	}

	public ORMError(Throwable cause) {
		super(cause);
	}

	public ORMError(String message, Throwable cause) {
		super(message, cause);
	}
}
