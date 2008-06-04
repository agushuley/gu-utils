package com.gushuley.utils;


import java.sql.*;
import java.util.*;
import java.util.Date;

import org.apache.log4j.Logger;

public final class Tools {

	public static <T> T coalesce(T... items) {
		for (T item : items) {
			if (item != null) {
				return item;
			}
		}
		return null;
	}

	public static void setDate(PreparedStatement stm, int i, Date date,
			boolean isNull) throws SQLException {
		if (isNull) {
			stm.setNull(i, Types.DATE);
		} else {
			stm.setDate(i, new java.sql.Date(date.getTime()));
		}
	}
	
	public static void setDate(PreparedStatement stm, int i, Date date)
			throws SQLException {
		setDate(stm, i, date, date == null);
	}

	public static void setTimestamp(PreparedStatement stm, int i, Date date, boolean isNull) throws SQLException {
		if (isNull) {
			stm.setNull(i, Types.TIMESTAMP);
		} else {
			stm.setTimestamp(i, new Timestamp(date.getTime()));
		}	
	}
	

	public static void setTimestamp(PreparedStatement stm, int i, Date date) throws SQLException {
		setTimestamp(stm, i, date, date == null);
	}
	
	public static boolean isEmpty(String description) {
		return description == null || description.trim().equals("");
	}

	private static Map<Class<?>, Logger> loggers = new HashMap<Class<?>, Logger>();

	public static Logger getLogger(Class<?> clazz) {
		synchronized (loggers) {
			if (!loggers.containsKey(clazz)) {
				loggers.put(clazz, Logger.getLogger(clazz));
			}
		}
		return loggers.get(clazz);
	}

	public static boolean isBitSetted(int mask, int bit) {
		return ((1 << bit) & mask) == (1 << bit);
	}

	public static int setBit(int value, int bit) {
		return value | (1 << bit);
	}

	public static int clearBit(int value, int bit) {
		return value & ~(1 << bit);
	}

	public static int setBitValue(int mask, Byte bit, boolean value) {
		if (value) {
			return setBit(mask, bit);
		}
		else {
			return clearBit(mask, bit);			
		}
	}

	public static boolean afterEq(Date date, Date dateFrom) {
		return date.after(dateFrom) || date.equals(dateFrom);
	}

	public static boolean beforeEq(Date date, Date dateTo) {
		return date.before(dateTo) || date.equals(dateTo);
	}
	
	public static boolean between(Date date, Date dateFrom, Date dateTo) {
		return afterEq(date, dateFrom) && beforeEq(date, dateTo);
	}

	public static boolean intersect(Date r1from, Date r1to, Date r2from, Date r2to) {
		return between(r1from, r2from, r2to) || between(r1to, r2from, r2to);
	}

	public static boolean between(int i, int from, int to) {
		return i >= from && i <= to;
	}

	public static boolean intersect(int r1from, int r1to, int r2from, int r2to) {
		return between(r1from, r2from, r2to) || between(r1to, r2from, r2to);
	}

	public static <T> boolean isInSet(T value, T... set) {
		for (T i : set) {
			if (i.equals(value)) {
				return true;
			}
		}
		return false;
	}

	public static Date min(Date d1, Date d2) {
		if (d2.before(d1)) {
			return d2;
		}
		return d1;
	}

	public static void setNumeric(PreparedStatement stm, int i, Number value)
			throws SQLException {
		if (value != null) {
			stm.setObject(i, value, Types.NUMERIC);
		} else {
			stm.setNull(i, Types.NUMERIC);
		}
	}
	
	public static <T> T[] asArray(T... arr) {
		return arr;
	}	
	
	public interface ExtractCallback<T1, O1> {
		T1 extractElement(O1 o);
		T1[] createArray(int size);
	}
	
	public static <T, O> T[] extractToArray(Collection<O> src, ExtractCallback<T, O> callback) {
		T[] target = callback.createArray(src.size());
		int i = 0;
		for (O o : src) {
			target[i] = callback.extractElement(o);
			i++;
		}
		return target;
	}
}
