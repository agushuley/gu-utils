package com.gushuley.utils.scheduler.dom;

import com.gushuley.utils.orm.impl.GenericORMObject;

public class JobDef extends GenericORMObject<String> {
	public enum Type {
		Weekly(0),
		Monthly(1);
		
		private Type(int id) {
			this.id = id;
		}

		private int id;
		public int getId() {
			return id;
		}
		
		public static Type parse(int id) {
			for (Type t : Type.values()) {
				if (t.getId() == id) return t;
			}
			return null;
		}
	}
	
	public JobDef(String key) {
		super(key);
	}

	private String className;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		checkRo("className");
		this.className = className;
	}

	private int startHour;

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		checkRo("startHour");
		this.startHour = startHour;
	}

	private int startMinute;

	public int getStartMinute() {
		return startMinute;
	}

	public void setStartMinute(int startMinute) {
		checkRo("startMinute");
		this.startMinute = startMinute;
	}
	
	private int dayMask;

	public int getDayMask() {
		return dayMask;
	}

	public void setDayMask(int dayMask) {
		checkRo("dayMask");
		this.dayMask = dayMask;
	}
	
	private String properties;

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		checkRo("properties");
		this.properties = properties;
	}
	
	private Type type = Type.Weekly;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		checkRo("type");
		this.type = type;
	}
}
