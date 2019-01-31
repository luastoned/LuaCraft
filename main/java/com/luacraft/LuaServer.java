package com.luacraft;

import com.luacraft.library.server.LuaGlobals;
import com.luacraft.library.server.LuaLibCommand;
import com.luacraft.library.server.LuaLibGame;
import com.luacraft.meta.server.LuaByteBuf;
import com.luacraft.meta.server.LuaPlayer;
import com.luacraft.meta.server.LuaPropertyManager;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaSyntaxException;

public class LuaServer extends LuaShared {

	public void initialize(boolean hooks) {
		initializeShared(hooks);
		loadLibraries();
	}

	public void runScripts() {
		runSharedScripts();
		info("Loading lua/autorun/server/*.lua");
		try {
			autorun("server"); // Load all files within autorun/server
		} catch(LuaRuntimeException e) {
			handleLuaRuntimeError(e);
		} catch(LuaSyntaxException e) {
			error(e.getMessage());
			e.printStackTrace();
		}
	}

	private void loadLibraries() {
		printSide("Loading server libraries..");

		// Libs
		LuaGlobals.Init(this);
		LuaLibCommand.Init(this);
		LuaLibGame.Init(this);

		// Meta
		LuaPlayer.Init(this);
		LuaByteBuf.Init(this);
		LuaPropertyManager.Init(this);

		pushBoolean(false);
		setGlobal("CLIENT");
		pushBoolean(true);
		setGlobal("SERVER");
	}
}