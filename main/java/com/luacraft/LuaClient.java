package com.luacraft;

import java.io.File;
import java.util.List;

import com.luacraft.classes.FileMount;
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
import com.luacraft.meta.client.LuaRenderLivingBase;
import com.luacraft.meta.client.LuaVector;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaSyntaxException;

import net.minecraftforge.common.MinecraftForge;

public class LuaClient extends LuaShared {
	private LuaEventManagerClient luaClientEvent;

	public void initialize(boolean hooks) {
		if (hooks) {
			luaClientEvent = new LuaEventManagerClient(this);
			printSide("Registering client event manager");
			MinecraftForge.EVENT_BUS.register(luaClientEvent);
		}
		
		initializeShared(hooks);
		loadLibraries();
	}

	public void runScripts() {
		runSharedScripts();
		try {
			info("Loading lua/autorun/client/*.lua");
			autorun("client"); // Load all files within autorun/client
			// Load items
			info("Loading lua/items/*/shared.lua");
			List<File> files = FileMount.GetFilesIn("lua/items/*/shared.lua");
			for (File file : files) {
				LuaScriptLoader.loadItemScript(this, file);
			}
		} catch(LuaRuntimeException e) {
			handleLuaError(e);
		} catch(LuaSyntaxException e) {
			e.printStackTrace();
			error(e.getMessage());
		}
	}

	public void close() {
		super.close();
		if (luaClientEvent != null) {
			printSide("Unregistering client event manager");
			MinecraftForge.EVENT_BUS.unregister(luaClientEvent);
			luaClientEvent = null;
		}
	}

	private void loadLibraries() {
		printSide("Loading client libraries..");

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
		LuaRenderLivingBase.Init(this);
		LuaVector.Init(this);

		pushBoolean(true);
		setGlobal("CLIENT");
		pushBoolean(false);
		setGlobal("SERVER");
	}
}
