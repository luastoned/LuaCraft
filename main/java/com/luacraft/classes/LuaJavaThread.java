package com.luacraft.classes;

import com.luacraft.LuaClient;
import com.luacraft.LuaCraftState;
import com.luacraft.LuaServer;
import com.naef.jnlua.LuaUserdata;

public class LuaJavaThread extends Thread implements LuaUserdata {
	private LuaCraftState l;
	private String file;

	public LuaJavaThread(LuaCraftState state, String f) {
		if (state.getActualSide().isClient())
			l = new LuaClient();
		else
			l = new LuaServer();

		file = f;

		l.setSideOverride(state.getSideOverride());
	}

	public void run() {
		if (l.isOpen() && file != null) {
			try {
				l.includeFile(file);
			} catch (Exception e) {
				l.handleException(e);
			} finally {
				l.close();
			}
		}
	}

	public String getTypeName() {
		return "Thread";
	}
}