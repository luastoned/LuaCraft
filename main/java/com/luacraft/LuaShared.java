package com.luacraft;

import com.luacraft.classes.LuaScriptedItem;
import com.luacraft.library.LuaGlobals;
import com.luacraft.library.LuaLibHTTP;
import com.luacraft.library.LuaLibLanguage;
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

import net.minecraftforge.common.MinecraftForge;

public class LuaShared extends LuaCraftState {
	private LuaEventManager luaEvent;
	private LuaPacketManager packet;

	public void initialize() {
		packet = new LuaPacketManager(this);
		luaEvent = new LuaEventManager(this);

		print("Registering packet manager");
		LuaCraft.channel.register(packet);
		print("Registering shared event manager");
		MinecraftForge.EVENT_BUS.register(luaEvent);

		loadLibraries();
		runScripts();
	}

	public void runScripts() {
		loadExtensions();

		print("Loading autorun");
		super.autorun(); // Load all files within autorun
		super.autorun("shared"); // Failsafe, incase someone thinks they need a shared folder
	}

	public void close() {
		print("Unregistering packet manager");
		LuaCraft.channel.unregister(packet);
		packet = null;
		print("Unregistering shared event manager");
		MinecraftForge.EVENT_BUS.unregister(luaEvent);
		luaEvent = null;
		super.close();
	}

	private void loadExtensions() {
		// Load all packed modules from our Jar
		includePackedFile("/lua/modules/hook.lua");
		includePackedFile("/lua/modules/net.lua");

		// Load all packed extensions from our Jar
		includePackedFile("/lua/extensions/math.lua");
		includePackedFile("/lua/extensions/player.lua");
		includePackedFile("/lua/extensions/string.lua");
		includePackedFile("/lua/extensions/table.lua");

		includeDirectory("extensions"); // Load any extensions a user made
	}

	private void loadLibraries() {
		print("Loading shared Lua...");

		openLib(Library.BASE);
		openLib(Library.PACKAGE);
		openLib(Library.TABLE);
		openLib(Library.IO);
		openLib(Library.OS);
		openLib(Library.STRING);
		openLib(Library.MATH);
		openLib(Library.DEBUG);
		openLib(Library.BIT);
		openLib(Library.JIT);
		openLib(Library.FFI);

		// Set the registry to support _R
		pushValue(LuaState.REGISTRYINDEX);
		setGlobal("_R");

		// Disable the output/input buffer
		load("io.stdout:setvbuf('no')", "=LuaShared.LoadLibraries");
		call(0, 0);
		load("io.stderr:setvbuf('no')", "=LuaShared.LoadLibraries");
		call(0, 0);

		String lua = LuaCraft.getRootLuaDirectory();

		// Set the package path to the correct location
		getGlobal("package");
		pushString(lua + "modules/?.lua;" + lua + "modules/bin/?.lua;" + lua + "modules/?/init.lua");
		setField(-2, "path");
		pop(1);

		// Set the package path to the correct location
		getGlobal("package");
		pushString(lua + "modules/?.dll;" + lua + "modules/bin/?.dll;" + lua + "modules/bin/loadall.dll");
		setField(-2, "cpath");
		pop(1);

		// Libs
		LuaGlobals.Init(this);
		LuaLibHTTP.Init(this);
		LuaLibLanguage.Init(this);
		LuaLibSQL.Init(this);
		LuaLibThread.Init(this);
		LuaLibUtil.Init(this);

		// Meta
		LuaAngle.Init(this);
		LuaBlock.Init(this);
		LuaByteBuf.Init(this);
		LuaChannel.Init(this);
		LuaColor.Init(this);
		LuaContainer.Init(this);
		LuaDamageSource.Init(this);
		LuaDataWatcher.Init(this);
		LuaEntity.Init(this);
		LuaEntityDamageSource.Init(this);
		LuaEntityItem.Init(this);
		LuaScriptedItem.Init(this);
		LuaItemStack.Init(this);
		LuaLiving.Init(this);
		LuaLivingBase.Init(this);
		LuaNBTTag.Init(this);
		LuaPlayer.Init(this);
		LuaResource.Init(this);
		LuaThread.Init(this);
		LuaSQLDatabase.Init(this);
		LuaSQLQuery.Init(this);
		LuaVector.Init(this);
		LuaWorld.Init(this);
	}
}
