package com.luacraft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.luacraft.library.client.LuaGlobals;
import com.luacraft.library.client.LuaLibGame;
import com.luacraft.library.client.LuaLibInput;
import com.luacraft.library.client.LuaLibProfiler;
import com.luacraft.library.client.LuaLibRender;
import com.luacraft.library.client.LuaLibSurface;
import com.luacraft.meta.client.LuaByteBuf;
import com.luacraft.meta.client.LuaEntity;
import com.luacraft.meta.client.LuaFont;
import com.luacraft.meta.client.LuaModelResource;
import com.luacraft.meta.client.LuaVector;

public class LuaClient extends LuaShared
{
	private static LuaEventManagerClient luaEvent = null;

	public void Initialize(final LuaCraftState l)
	{
		luaEvent = new LuaEventManagerClient(l);
		super.Initialize(l);
		
		LoadLibraries(l);

		MinecraftForge.EVENT_BUS.register(luaEvent);
		FMLCommonHandler.instance().bus().register(luaEvent);
	}
	
	public void Autorun(final LuaCraftState l)
	{
		super.Autorun(l);
		l.autorun("client"); // Load all files within autorun/client
	}

	public void Shutdown()
	{
		MinecraftForge.EVENT_BUS.unregister(luaEvent);
		FMLCommonHandler.instance().bus().unregister(luaEvent);
		luaEvent = null;
		super.Shutdown();
	}

	private static void LoadLibraries(final LuaCraftState l)
	{
		l.print("Loading Client LuaState...");
		
		// Libs
		LuaGlobals.Init(l);
		LuaLibGame.Init(l);
		LuaLibInput.Init(l);
		LuaLibProfiler.Init(l);
		LuaLibRender.Init(l);
		LuaLibSurface.Init(l);

		// Meta
		LuaByteBuf.Init(l);
		LuaEntity.Init(l);
		LuaFont.Init(l);
		LuaModelResource.Init(l);
		LuaVector.Init(l);

		l.pushBoolean(true);
		l.setGlobal("CLIENT");
		l.pushBoolean(false);
		l.setGlobal("SERVER");
	}
}
