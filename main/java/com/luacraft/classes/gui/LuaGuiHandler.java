package com.luacraft.classes.gui;

import com.luacraft.LuaCraftState;

public class LuaGuiHandler extends LuaCraftState {
	protected int reference;
	protected LuaCraftState l;

	public LuaGuiHandler(LuaCraftState luaState) {
		l = luaState;

		l.newMetatable("Panels");
		{
			l.newTable();
			reference = l.ref(-2);
		}
		l.pop(1);
	}

	public void pushField(String field) {
		l.newMetatable("Panels");
		l.rawGet(-1, reference);
		l.getField(-1, field);
		l.setTop(-1);
	}

	public void destroy() {
		l.newMetatable("Panels");
		l.unref(-1, reference);
		l.setTop(0);
	}
}
