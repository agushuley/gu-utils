package com.gushuley.utils.orm.impl;

import java.util.Collection;

import com.gushuley.utils.orm.ORMObject;


public final class OrmTools {
	public static int getInt(ORMObject<Integer> obj) {
		if (obj == null) return 0;
		return obj.getKey();
	}

	public static String getString(ORMObject<String> obj) {
		if (obj == null) return null;
		return obj.getKey();
	}

	public static <T extends ORMObject<?>> T getObject(T obj, Collection<T> objs) {
		if (obj != null) {
			return obj;
		}
		if (objs != null && objs.size() > 0) {
			return objs.iterator().next();
		}
		return null;
	}
}
