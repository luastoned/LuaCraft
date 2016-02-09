package com.luacraft.meta;

import java.sql.SQLException;

import com.luacraft.LuaUserdata;
import com.luacraft.classes.LuaJavaQuery;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;

public class LuaSQLQuery {

	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaQuery self = (LuaJavaQuery) l.checkUserdata(1, LuaJavaQuery.class, "SQLQuery");
			l.pushString(String.format("SQLQuery: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Start
	 * @info Starts processing the query
	 * @arguments nil
	 * @return nil
	 */

	public static JavaFunction Start = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaQuery self = (LuaJavaQuery) l.checkUserdata(1, LuaJavaQuery.class, "SQLQuery");
			self.start();
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetString
	 * @info Sets the string at a specified index within the prepared statement
	 * @arguments [[Number]]:index, [[String]]:value
	 * @return nil
	 */

	public static JavaFunction SetString = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaQuery self = (LuaJavaQuery) l.checkUserdata(1, LuaJavaQuery.class, "SQLQuery");
			try {
				self.statement.setString(l.checkInteger(2), l.checkString(3));
			} catch (SQLException e) {
				throw new LuaRuntimeException(e);
			}
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetInt
	 * @info Sets the number at a specified index within the prepared statement
	 * @arguments [[Number]]:index, [[Number]]:value
	 * @return nil
	 */

	public static JavaFunction SetInt = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaQuery self = (LuaJavaQuery) l.checkUserdata(1, LuaJavaQuery.class, "SQLQuery");
			try {
				self.statement.setInt(l.checkInteger(2), l.checkInteger(3));
			} catch (SQLException e) {
				throw new LuaRuntimeException(e);
			}
			return 0;
		}
	};

	public static void Init(LuaState l) {
		l.newMetatable("SQLQuery");
		{
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			LuaUserdata.SetupBasicMeta(l);
			LuaUserdata.SetupMeta(l, false);

			l.newMetatable("Object");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(Start);
			l.setField(-2, "Start");
			l.pushJavaFunction(SetString);
			l.setField(-2, "SetString");
			l.pushJavaFunction(SetInt);
			l.setField(-2, "SetInt");
		}
		l.pop(1);
	}
}
