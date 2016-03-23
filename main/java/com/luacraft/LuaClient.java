package com.luacraft;

import com.luacraft.library.client.LuaGlobals;
import com.luacraft.library.client.LuaLibGame;
import com.luacraft.library.client.LuaLibInput;
import com.luacraft.library.client.LuaLibProfiler;
import com.luacraft.library.client.LuaLibRender;
import com.luacraft.library.client.LuaLibSurface;
import com.luacraft.meta.client.LuaByteBuf;
import com.luacraft.meta.client.LuaEntity;
import com.luacraft.meta.client.LuaFont;
import com.luacraft.meta.client.LuaLivingBase;
import com.luacraft.meta.client.LuaVector;

import net.minecraftforge.common.MinecraftForge;

public class LuaClient extends LuaShared {
	private LuaEventManagerClient luaClientEvent;

	public void initialize(boolean hooks) {
		initializeShared(hooks);
		loadLibraries();

		if (hooks) {
			luaClientEvent = new LuaEventManagerClient(this);
			print("Registering client event manager");
			MinecraftForge.EVENT_BUS.register(luaClientEvent);
		}
	}

	public void runScripts() {
		runSharedScripts();
		print("Loading autorun/client");
		autorun("client"); // Load all files within autorun/client
	}

	public void close() {
		super.close();
		if (luaClientEvent != null) {
			print("Unregistering client event manager");
			MinecraftForge.EVENT_BUS.unregister(luaClientEvent);
			luaClientEvent = null;
		}
	}

	private void loadLibraries() {
		print("Loading client Lua...");

		// Libs
		LuaGlobals.Init(this);
		LuaLibGame.Init(this);
		LuaLibInput.Init(this);
		LuaLibProfiler.Init(this);
		LuaLibRender.Init(this);
		LuaLibSurface.Init(this);

		// Meta
		LuaByteBuf.Init(this);
		LuaEntity.Init(this);
		LuaFont.Init(this);
		LuaLivingBase.Init(this);
		LuaVector.Init(this);

		pushBoolean(true);
		setGlobal("CLIENT");
		pushBoolean(false);
		setGlobal("SERVER");
	}
}
