package com.gushuley.utils.orm.sql;

import com.gushuley.utils.orm.ORMContext;
import com.gushuley.utils.orm.ORMObject;


public abstract class AbstractSqlMapper<T extends ORMObject<K>, K> 
extends AbstractSqlMapper2<T, K, ORMContext>
{
	public AbstractSqlMapper(boolean ordered) {
		super(ordered);
	}

	public AbstractSqlMapper(boolean _short, boolean ordered) {
		super(_short, ordered);
	}
	
	public AbstractSqlMapper() {}
}
