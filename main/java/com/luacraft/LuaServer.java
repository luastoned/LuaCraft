package com.luacraft;

import com.luacraft.library.server.LuaGlobals;
import com.luacraft.library.server.LuaLibCommand;
import com.luacraft.library.server.LuaLibGame;
import com.luacraft.meta.server.LuaByteBuf;
import com.luacraft.meta.server.LuaPlayer;
import com.luacraft.meta.server.LuaPropertyManager;

public class LuaServer extends LuaShared {
	public void Initialize(final LuaCraftState l) {
		super.Initialize(l);
		LoadLibraries(l);
	}

	public void Autorun(final LuaCraftState l) {
		super.Autorun(l);
		l.autorun("server"); // Load all files within autorun/server
	}

	private static void LoadLibraries(final LuaCraftState l) {
		l.print("Loading Server LuaState...");

		// Libs
		LuaGlobals.Init(l);
		LuaLibCommand.Init(l);
		LuaLibGame.Init(l);

		// Meta
		LuaPlayer.Init(l);
		LuaByteBuf.Init(l);
		LuaPropertyManager.Init(l);

		l.pushBoolean(false);
		l.setGlobal("CLIENT");
		l.pushBoolean(true);
		l.setGlobal("SERVER");
	}
}