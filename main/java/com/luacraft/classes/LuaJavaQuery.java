package com.luacraft.classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaUserdata;

public class LuaJavaQuery extends Thread implements LuaUserdata {

	public PreparedStatement statement;

	private LuaState l;
	private int callbackRef;
	
	private int newCallbackRef(int index)
	{
		int ref;
		l.newMetatable("PendingQueries");
		{
			l.pushValue(index);
			ref = l.ref(-2);
		}
		l.pop(1);
		return ref;
	}

	public LuaJavaQuery(LuaState luaState, PreparedStatement query, int index)
	{
		l = luaState;
		statement = query;
		callbackRef = newCallbackRef(index);
	}

	public void pushCallbackFunc()
	{
		l.newMetatable("PendingQueries");
		l.rawGet(-1, callbackRef);
		l.remove(-2);
	}

	public void run() {
		synchronized (l)
		{
			ResultSet result = null;
			try {				
				if (statement.execute())
				{
					l.newTable();

					int row = 1;
					result = statement.getResultSet();
					while (result.next())
					{
						ResultSetMetaData data = result.getMetaData();

						l.pushInteger(row++);
						l.newTable();

						for (int i=0; i < data.getColumnCount(); i++)
						{
							int column = i + 1;
							l.pushString(result.getString(column));
							l.setField(-2, data.getColumnName(column));
						}

						l.setTable(-3);
					}
				} else
					l.pushNil();

				pushCallbackFunc();
				if (l.isFunction(-1))
				{
					l.pushValue(-2); // Push the data value
					l.remove(-3); // Since we pushed a copy, remove the original
					l.call(1, 0);
				}
			} catch (SQLException e)
			{
				pushCallbackFunc();
				if (l.isFunction(-1))
				{
					l.pushNil();
					l.pushString(e.getMessage());
					l.call(2, 0);
				}
			} finally {
				try
				{
					if (result != null)
						result.close();
				} catch (SQLException e) {}
				l.newMetatable("PendingQueries");
				l.unref(-1, callbackRef);
				l.setTop(0); // Clears the stack
			}
		}
	}

	public String getTypeName() {
		return "SQLQuery";
	}
}
