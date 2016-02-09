package com.luacraft.classes;

import com.luacraft.LuaClient;
import com.luacraft.LuaCraftState;
import com.luacraft.LuaServer;
import com.naef.jnlua.LuaRuntimeException;

public class LuaJavaScriptThread extends LuaJavaThread {
	private LuaCraftState l;
	private String file;

	public LuaJavaScriptThread(LuaCraftState state, String f) {
		if (state.getActualSide().isClient()) {
			l = new LuaClient();
			((LuaClient) l).initialize(false);
		} else {
			l = new LuaServer();
			((LuaServer) l).initialize(false);
		}

		file = f;
	}

	public void run() {
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

	public String getTypeName() {
		return "Thread";
	}
}