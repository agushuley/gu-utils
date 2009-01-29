package com.gushuley.utils.orm.sql;

import com.gushuley.utils.orm.impl.AbstractIntKeyNameObject;
import com.gushuley.utils.orm.impl.GenericContext;

public abstract class AbstractIntKeyNameSqlMapper<C extends AbstractIntKeyNameObject> 
extends AbstractIntKeyNameSqlMapper2<C, GenericContext> 
{
	public AbstractIntKeyNameSqlMapper(String table, String idColumn,
			String nameColumn, SqlAttribute[] attributes) {
		super(table, idColumn, nameColumn, attributes);
	}
}
