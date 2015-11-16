package com.luacraft.classes;

import java.io.File;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.naef.jnlua.LuaUserdata;

import net.minecraftforge.fml.relauncher.Side;

public class LuaJavaThread extends Thread implements LuaUserdata {
	private LuaCraftState l = null;
	private String file = null;

	public LuaJavaThread(LuaCraftState state, String f) {
		l = new LuaCraftState();
		file = f;

		l.setSideOverride(state.getSideOverride());

		if (state.getActualSide() == Side.CLIENT)
			LuaCraft.clientLibs.Initialize(l);
		else
			LuaCraft.serverLibs.Initialize(l);
	}

	public void run() {
		if (l.isOpen() && file != null) {
			try {
				l.includeFile(file);
			} catch (Exception e) {
				l.handleException(e);
			} finally {
				if (l.getActualSide() == Side.CLIENT)
					LuaCraft.clientLibs.Shutdown();
				else
					LuaCraft.serverLibs.Shutdown();
				l.close();
			}
		}
	}

	public String getTypeName() {
		return "Thread";
	}
}