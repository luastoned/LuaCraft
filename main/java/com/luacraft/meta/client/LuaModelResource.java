package com.luacraft.meta.client;

import com.luacraft.LuaCraftState;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaModelResource {
	@Deprecated
	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushString("Deprecated as of Minecraft 1.9");
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("ModelResource");
		{
			l.pushValue(-1);
			l.setField(-2, "__index");

			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			l.newMetatable("Resource");
			l.setField(-2, "__basemeta");
		}
		l.pop(1);
	}
}
