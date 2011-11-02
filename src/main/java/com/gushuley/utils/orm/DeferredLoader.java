package com.gushuley.utils.orm;

import java.util.Collection;

public interface DeferredLoader<S, D> {
	Collection<D> load(S obj) throws ORMException;
}
