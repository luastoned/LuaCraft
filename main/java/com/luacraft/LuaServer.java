package com.luacraft;

import com.luacraft.library.server.LuaGlobals;
import com.luacraft.library.server.LuaLibCommand;
import com.luacraft.library.server.LuaLibGame;
import com.luacraft.meta.server.LuaByteBuf;
import com.luacraft.meta.server.LuaPlayer;
import com.luacraft.meta.server.LuaPropertyManager;

public class LuaServer extends LuaShared {

	public void initialize() {
		super.initialize();
		loadLibraries();
		runScripts();
	}

	public void runScripts() {
		super.runScripts();

		super.autorun();
		print("Loading autorun/server");
		super.autorun("server"); // Load all files within autorun/server
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