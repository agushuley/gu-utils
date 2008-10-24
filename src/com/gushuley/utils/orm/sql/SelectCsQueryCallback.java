package com.gushuley.utils.orm.sql;

import java.sql.*;

import com.gushuley.utils.orm.ORMException;

public interface SelectCsQueryCallback
extends SelectQueryCallback
{
	ResultSet getResultSet(CallableStatement stm) throws ORMException, SQLException;
}
