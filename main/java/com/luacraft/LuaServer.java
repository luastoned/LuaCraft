package com.luacraft;

import com.luacraft.library.LuaLibCommand;
import com.luacraft.library.server.LuaGlobals;
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
		print("Loading autorun/server");
		try {
			autorun("server"); // Load all files within autorun/server
		} catch(LuaRuntimeException e) {
			handleLuaError(e);
		} catch(LuaSyntaxException e) {
			e.printStackTrace();
			error(e.getMessage());
		}
	}

	private void loadLibraries() {
		print("Loading server Lua...");

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