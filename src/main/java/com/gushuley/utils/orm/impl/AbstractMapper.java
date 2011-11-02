package com.gushuley.utils.orm.impl;

import com.gushuley.utils.orm.ORMContext;
import com.gushuley.utils.orm.ORMObject;

public abstract class AbstractMapper<T extends ORMObject<K>, K> extends AbstractMapper2<T, K, ORMContext>{
	public AbstractMapper(boolean ordered) {
		super(ordered);
	}
}
