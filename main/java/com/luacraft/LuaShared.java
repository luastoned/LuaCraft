package com.luacraft;

import net.minecraftforge.common.MinecraftForge;

import com.luacraft.classes.LuaScriptedItem;
import com.luacraft.library.LuaGlobals;
import com.luacraft.library.LuaLibHTTP;
import com.luacraft.library.LuaLibSQL;
import com.luacraft.library.LuaLibThread;
import com.luacraft.library.LuaLibUtil;
import com.luacraft.meta.LuaAngle;
import com.luacraft.meta.LuaBlock;
import com.luacraft.meta.LuaByteBuf;
import com.luacraft.meta.LuaChannel;
import com.luacraft.meta.LuaColor;
import com.luacraft.meta.LuaContainer;
import com.luacraft.meta.LuaDamageSource;
import com.luacraft.meta.LuaDataWatcher;
import com.luacraft.meta.LuaEntity;
import com.luacraft.meta.LuaEntityDamageSource;
import com.luacraft.meta.LuaEntityItem;
import com.luacraft.meta.LuaItemStack;
import com.luacraft.meta.LuaLiving;
import com.luacraft.meta.LuaLivingBase;
import com.luacraft.meta.LuaNBTTag;
import com.luacraft.meta.LuaPlayer;
import com.luacraft.meta.LuaResource;
import com.luacraft.meta.LuaSQLDatabase;
import com.luacraft.meta.LuaSQLQuery;
import com.luacraft.meta.LuaThread;
import com.luacraft.meta.LuaVector;
import com.luacraft.meta.LuaWorld;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaState.Library;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class LuaShared {
	private static LuaEventManager luaEvent = null;
	private static LuaPacketManager packet = null;

	public void Initialize(final LuaCraftState l) {
		packet = new LuaPacketManager(l);
		luaEvent = new LuaEventManager(l);

		LoadLibraries(l);

		LuaCraft.channel.register(packet);
		MinecraftForge.EVENT_BUS.register(luaEvent);
		FMLCommonHandler.instance().bus().register(luaEvent);
	}

	public void Shutdown() {
		LuaCraft.channel.unregister(packet);
		MinecraftForge.EVENT_BUS.unregister(luaEvent);
		FMLCommonHandler.instance().bus().unregister(luaEvent);
		packet = null;
		luaEvent = null;
	}

	public void Autorun(final LuaCraftState l) {
		// Load all packed modules from our Jar
		l.includePackedFile("/lua/modules/hook.lua");
		l.includePackedFile("/lua/modules/net.lua");

		// Load all packed extensions from our Jar
		l.includePackedFile("/lua/extensions/math.lua");
		l.includePackedFile("/lua/extensions/player.lua");
		l.includePackedFile("/lua/extensions/string.lua");
		l.includePackedFile("/lua/extensions/table.lua");

		l.includeDirectory("extensions"); // Load any extensions a user made
		l.autorun("shared"); // Load all files within autorun
	}

	private static void LoadLibraries(final LuaCraftState l) {
		l.print("Loading Shared LuaState...");

		l.openLib(Library.BASE);
		l.openLib(Library.PACKAGE);
		l.openLib(Library.TABLE);
		l.openLib(Library.IO);
		l.openLib(Library.OS);
		l.openLib(Library.STRING);
		l.openLib(Library.MATH);
		l.openLib(Library.DEBUG);
		l.openLib(Library.BIT);
		l.openLib(Library.JIT);
		l.openLib(Library.FFI);

		// Set the registry to support _R
		l.pushValue(LuaState.REGISTRYINDEX);
		l.setGlobal("_R");

		// Disable the output/input buffer
		l.load("io.stdout:setvbuf('no')", "=LuaShared.LoadLibraries");
		l.call(0, 0);
		l.load("io.stderr:setvbuf('no')", "=LuaShared.LoadLibraries");
		l.call(0, 0);

		String lua = LuaCraft.getRootLuaDirectory();

		// Set the package path to the correct location
		l.getGlobal("package");
		l.pushString(lua + "modules/?.lua;" + lua + "modules/bin/?.lua;" + lua
				+ "modules/?/init.lua");
		l.setField(-2, "path");
		l.pop(1);

		// Set the package path to the correct location
		l.getGlobal("package");
		l.pushString(lua + "modules/?.dll;" + lua + "modules/bin/?.dll;" + lua
				+ "modules/bin/loadall.dll");
		l.setField(-2, "cpath");
		l.pop(1);

		// Libs
		LuaGlobals.Init(l);
		LuaLibHTTP.Init(l);
		LuaLibSQL.Init(l);
		LuaLibThread.Init(l);
		LuaLibUtil.Init(l);

		// Meta
		LuaAngle.Init(l);
		LuaBlock.Init(l);
		LuaByteBuf.Init(l);
		LuaChannel.Init(l);
		LuaColor.Init(l);
		LuaContainer.Init(l);
		LuaDamageSource.Init(l);
		LuaDataWatcher.Init(l);
		LuaEntity.Init(l);
		LuaEntityDamageSource.Init(l);
		LuaEntityItem.Init(l);
		LuaScriptedItem.Init(l);
		LuaItemStack.Init(l);
		LuaLiving.Init(l);
		LuaLivingBase.Init(l);
		LuaNBTTag.Init(l);
		LuaPlayer.Init(l);
		LuaResource.Init(l);
		LuaThread.Init(l);
		LuaSQLDatabase.Init(l);
		LuaSQLQuery.Init(l);
		LuaVector.Init(l);
		LuaWorld.Init(l);
	}
}
