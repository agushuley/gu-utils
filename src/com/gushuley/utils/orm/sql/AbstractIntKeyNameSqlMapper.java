package com.gushuley.utils.orm.sql;

import com.gushuley.utils.orm.ORMContext;
import com.gushuley.utils.orm.impl.AbstractIntKeyNameObject;

public abstract class AbstractIntKeyNameSqlMapper<C extends AbstractIntKeyNameObject> 
extends AbstractIntKeyNameSqlMapper2<C, ORMContext> 
{
	public AbstractIntKeyNameSqlMapper(String table, String idColumn,
			String nameColumn, SqlAttribute[] attributes) {
		super(table, idColumn, nameColumn, attributes);
	}
}
