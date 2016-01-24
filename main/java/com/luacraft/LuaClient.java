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
import com.luacraft.meta.client.LuaModelResource;
import com.luacraft.meta.client.LuaVector;

import net.minecraftforge.common.MinecraftForge;

public class LuaClient extends LuaShared {
	private LuaEventManagerClient luaClientEvent;

	public void initialize() {
		super.initialize();
		loadLibraries();

		luaClientEvent = new LuaEventManagerClient(this);

		print("Registering client event manager");
		MinecraftForge.EVENT_BUS.register(luaClientEvent);

		runScripts();
	}

	public void runScripts() {
		super.runScripts();

		super.autorun();
		print("Loading autorun/client");
		super.autorun("client"); // Load all files within autorun/client
	}

	public void close() {
		super.close();
		print("Unregistering client event manager");
		MinecraftForge.EVENT_BUS.unregister(luaClientEvent);
		luaClientEvent = null;
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
		LuaModelResource.Init(this);
		LuaVector.Init(this);

		pushBoolean(true);
		setGlobal("CLIENT");
		pushBoolean(false);
		setGlobal("SERVER");
	}
}
