package com.gushuley.utils.orm.sql;

import java.sql.*;

import com.gushuley.utils.orm.ORMException;

public interface SelectQueryCallback
extends ExecCallback<PreparedStatement>
{
	void onRow(ResultSet rs) throws ORMException, SQLException ;
}
