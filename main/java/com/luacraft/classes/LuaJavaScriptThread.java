package com.luacraft.classes;

import com.luacraft.LuaClient;
import com.luacraft.LuaCraftState;
import com.luacraft.LuaServer;
import com.naef.jnlua.LuaRuntimeException;

public class LuaJavaScriptThread extends LuaJavaThread {
	private LuaCraftState l;
	private LuaCraftState state;
	private String file;

	public LuaJavaScriptThread(LuaCraftState s, String f) {
		state = s;
		file = f;
	}

	public void run() {
		if (state.getActualSide().isClient()) {
			l = new LuaClient();
			l.setSideOverride(state.getSideOverride());
			((LuaClient) l).initialize(false);
		} else {
			l = new LuaServer();
			l.setSideOverride(state.getSideOverride());
			((LuaServer) l).initialize(false);
		}

		if (l.isOpen() && file != null) {
			try {
				l.includeFile(file);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.close();
			}
		}
	}
}