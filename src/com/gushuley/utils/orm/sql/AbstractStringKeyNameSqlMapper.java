package com.gushuley.utils.orm.sql;

import com.gushuley.utils.orm.ORMContext;
import com.gushuley.utils.orm.impl.AbstractStringKeyNameObject;

public abstract class AbstractStringKeyNameSqlMapper<C extends AbstractStringKeyNameObject> 
extends AbstractStringKeyNameSqlMapper2<C, ORMContext> 
{
	public AbstractStringKeyNameSqlMapper(String table, String idColumn,
			String nameColumn, SqlAttribute[] attributes) {
		super(table, idColumn, nameColumn, attributes);
	}
}
