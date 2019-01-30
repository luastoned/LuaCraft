package com.luacraft.library;

import java.net.URL;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.luacraft.classes.Color;
import com.luacraft.classes.LuaJavaHTTPRequest;
import com.luacraft.console.ConsoleManager;
import com.luacraft.console.ConsoleManager.MessageCallbacks;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaType;

import net.minecraft.util.text.TextFormatting;

public class LuaLibConsole {

	private static String easyMsgC(LuaState l, int stackPos, Color defColor) {
		StringBuilder message = new StringBuilder();
		
		Color color = defColor;
		
		MessageCallbacks console = ConsoleManager.get(((LuaCraftState) l).getActualSide());

		for (int i = stackPos; i <= l.getTop(); i++) {
			if (l.isNoneOrNil(i))
				continue;

			if (l.isUserdata(i, Color.class)) {
				color = (Color) l.checkUserdata(i, Color.class, "Color");
			} else {
				l.getGlobal("tostring");
				l.pushValue(i);
				l.call(1, 1);
				String text = l.checkString(-1);
				message.append(text);
				console.msg(color.toJavaColor(), text);
				l.pop(1);
			}
		}
		
		console.msg("\n");
		
		return message.toString();
	}
	
	/**
	 * @author Jake
	 * @library console
	 * @function print
	 * @info Prints a message to the console
	 * @arguments [[Color]]:color, [[String]]:string, ...
	 * @return nil
	 */

	public static JavaFunction print = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaCraft.getLogger().info(easyMsgC(l, 1, new Color(ConsoleManager.PRINT)));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library console
	 * @function log
	 * @info Prints information to the console
	 * @arguments [[Color]]:color, [[String]]:string, ...
	 * @return nil
	 */

	public static JavaFunction info = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaCraft.getLogger().info(easyMsgC(l, 1, new Color(ConsoleManager.INFO)));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library console
	 * @function warn
	 * @info Prints a warning to the console
	 * @arguments [[Color]]:color, [[String]]:string, ...
	 * @return nil
	 */

	public static JavaFunction warn = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaCraft.getLogger().warn(easyMsgC(l, 1, new Color(ConsoleManager.WARNING)));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library console
	 * @function log
	 * @info Prints an error to the console
	 * @arguments [[Color]]:color, [[String]]:string, ...
	 * @return nil
	 */

	public static JavaFunction error = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaCraft.getLogger().error(easyMsgC(l, 1, new Color(ConsoleManager.ERROR)));
			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newTable();
		{
			l.pushJavaFunction(print);
			l.setField(-2, "print");
			l.pushJavaFunction(print);
			l.setField(-2, "log");
			l.pushJavaFunction(info);
			l.setField(-2, "info");
			l.pushJavaFunction(warn);
			l.setField(-2, "warn");
			l.pushJavaFunction(error);
			l.setField(-2, "error");
		}
		l.setGlobal("console");
	}

}
