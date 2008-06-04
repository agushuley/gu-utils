package com.gushuley.utils.orm.sql;

public class SqlAttribute {
	public SqlAttribute(String caption, String column) {
		this.caption = caption;
		this.column = column;
	}

	private String caption;

	private String column;

	public String getCaption() {
		return caption;
	}

	public String getColumn() {
		return column;
	}
}
