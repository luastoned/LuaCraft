package com.luacraft.meta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.luacraft.LuaCraft;
import com.luacraft.LuaUserdata;
import com.luacraft.classes.LuaJavaQuery;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaSQLDatabase {
	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			Connection self = (Connection) l.checkUserdata(1, Connection.class, "SQLDatabase");
			l.pushString(String.format("SQLDatabase: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Disconnect
	 * @info Closes the connection to the database
	 * @arguments nil
	 * @return [[Boolean]]:success, [ [[String]]:error ]
	 */

	public static JavaFunction Disconnect = new JavaFunction() {
		public int invoke(LuaState l) {
			Connection self = (Connection) l.checkUserdata(1, Connection.class, "SQLDatabase");
			try {
				self.close();
				l.pushBoolean(true);
				return 1;
			} catch (SQLException e) {
				l.pushBoolean(false);
				l.pushString(e.getMessage());
				return 2;
			}
		}
	};

	/**
	 * @author Jake
	 * @function Query
	 * @info Query the database
	 * @arguments [[String]]:query, [ [[Function]]:callback ]
	 * @return [[SQLQuery]]:query, [ [[String]]:error ]
	 */

	public static JavaFunction Query = new JavaFunction() {
		public int invoke(LuaState l) {
			Connection self = (Connection) l.checkUserdata(1, Connection.class, "SQLDatabase");
			try {
				PreparedStatement statement = self.prepareStatement(l.checkString(2));
				LuaJavaQuery query = new LuaJavaQuery(l, statement, 3);
				l.pushUserdataWithMeta(query, "SQLQuery");
				return 1;
			} catch (SQLException e) {
				l.pushBoolean(false);
				l.pushString(e.getMessage());
				return 2;
			}
		}
	};

	public static void Init(LuaState l) {
		l.newMetatable("SQLDatabase");
		{
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");
			
			LuaUserdata.SetupBasicMeta(l);

			l.newMetatable("Object");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(Disconnect);
			l.setField(-2, "Disconnect");
			l.pushJavaFunction(Query);
			l.setField(-2, "Query");
		}
		l.pop(1);
	}
}
