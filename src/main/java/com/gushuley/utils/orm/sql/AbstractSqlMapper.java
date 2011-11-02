package com.gushuley.utils.orm.sql;

import com.gushuley.utils.orm.ORMObject;
import com.gushuley.utils.orm.impl.GenericContext;


public abstract class AbstractSqlMapper<T extends ORMObject<K>, K> 
extends AbstractSqlMapper2<T, K, GenericContext>
{
	public AbstractSqlMapper(boolean ordered) {
		super(ordered);
	}

	public AbstractSqlMapper(boolean _short, boolean ordered) {
		super(_short, ordered);
	}
	
	public AbstractSqlMapper() {}
}
