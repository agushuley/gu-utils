package com.gushuley.utils.orm.impl;

import com.gushuley.utils.orm.Key;

public abstract class AbstractKey implements Key {
	public abstract Object[] getValues();

	public boolean equals(Object obj) {
		Object values[] = getValues();

		if (obj == null) return false;
		if (!(obj instanceof Key)) return false;
		
		Key k = (Key) obj;
		if (k.getValues().length != values.length) return false;
		
		for (int i = 0; i < k.getValues().length; i++) {
			if (!k.getValues()[i].equals(values[i]))
				return false;
		}
		
		return true;
	}

	@Override
	public int hashCode() {
		Object values[] = getValues();
		       
		int hashCode = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				hashCode = hashCode ^ values[i].hashCode();
			}
		}
		return hashCode;
	}

	@Override
	public String toString() {
		Object[] o = getValues();
		String value = "[" + getClass().getSimpleName();
		for (int i = 0; i < o.length; i++) {		
			if (i != 0) {
				value += ", ";
			}
			value += o[i];
		}
		value += "]";
		return value;
	}
}
