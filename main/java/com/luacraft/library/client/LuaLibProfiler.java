package com.luacraft.library.client;

import com.luacraft.LuaCraftState;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;

public class LuaLibProfiler {
	private static Minecraft client = null;
	public static Profiler profiler = null;

	/**
	 * @author Jake
	 * @library profiler
	 * @function Start
	 * @info Starts a profiler section
	 * @arguments [[String]]:name
	 * @return nil
	 */

	public static JavaFunction Start = new JavaFunction() {
		public int invoke(LuaState l) {
			profiler.startSection(l.checkString(1));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library profiler
	 * @function End
	 * @info Ends a profiler section
	 * @arguments nil
	 * @return nil
	 */

	public static JavaFunction End = new JavaFunction() {
		public int invoke(LuaState l) {
			profiler.endSection();
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library profiler
	 * @function Clear
	 * @info Clears all profilers
	 * @arguments nil
	 * @return nil
	 */

	public static JavaFunction Clear = new JavaFunction() {
		public int invoke(LuaState l) {
			profiler.clearProfiling();
			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		client = l.getMinecraft();
		profiler = client.mcProfiler;

		l.newTable();
		{
			l.pushJavaFunction(Start);
			l.setField(-2, "Start");
			l.pushJavaFunction(End);
			l.setField(-2, "End");
			l.pushJavaFunction(Clear);
			l.setField(-2, "Clear");
		}
		l.setGlobal("profiler");
	}
}
