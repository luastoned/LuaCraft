package com.luacraft.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.luacraft.LuaCraftState;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaLibSQL {

	/**
	 * @author Jake
	 * @library sql
	 * @function Connect Connect to a database via JDBC
	 * @arguments [[String]]:JDBC URL, [ [[String]]:username,
	 *            [[String]]:password ]
	 * @return [[SQLDatabase]]:database, [ [[String]]:error ]
	 */

	public static JavaFunction Connect = new JavaFunction() {
		public int invoke(LuaState l) {
			String url = l.checkString(1);
			Connection dataBase;
			try {
				if (l.isString(2) && l.isString(3))
					dataBase = DriverManager.getConnection(url, l.toString(2),
							l.toString(3));
				else
					dataBase = DriverManager.getConnection(url);

				l.pushUserdataWithMeta(dataBase, "SQLDatabase");
				return 1;
			} catch (SQLException e) {
				l.pushNil();
				l.pushString(e.getMessage());
				return 2;
			}
		}
	};

	/**
	 * @author Jake
	 * @library sql
	 * @function GetQueries Get a table of all pending queries
	 * @arguments nil
	 * @return [[Table]]:queries
	 */

	public static JavaFunction GetQueries = new JavaFunction() {
		public int invoke(LuaState l) {
			l.newMetatable("PendingQueries");
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			l.warning("Failed to load sqlite");
		}

		l.newTable();
		{
			l.pushJavaFunction(Connect);
			l.setField(-2, "Connect");
			l.pushJavaFunction(GetQueries);
			l.setField(-2, "GetPendingQueries");
		}
		l.setGlobal("sql");
	}

}
